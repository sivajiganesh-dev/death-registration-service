package digit.service;


import digit.config.MainConfiguration;
import digit.util.UserUtil;
import digit.web.models.Applicant;
import digit.web.models.CreateUserRequest;
import digit.web.models.DeathRegistrationApplication;
import digit.web.models.DeathRegistrationRequest;
import digit.web.models.User;
import digit.web.models.UserDetailResponse;
import digit.web.models.UserSearchRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
@Slf4j
public class UserService {

    private UserUtil userUtils;

    private MainConfiguration config;

    @Autowired
    public UserService(UserUtil userUtils, MainConfiguration config) {
        this.userUtils = userUtils;
        this.config = config;
    }

    /**
     * Calls user service to enrich user from search or upsert user
     *
     * @param request
     */
    public void callUserService(DeathRegistrationRequest request) {
        request.getDeathRegistrationApplications().forEach(application -> {
            if (!StringUtils.isEmpty(application.getApplicant().getId())) {
                enrichUser(application, request.getRequestInfo());
            } else {
                User user = createApplicantUser(application);
                User userCreated = upsertUser(user, request.getRequestInfo());
                application.getApplicant().setId(userCreated.getUuid());
                application.setApplicantId(userCreated.getUuid());
            }
        });
    }

    private User createApplicantUser(DeathRegistrationApplication application) {
        Applicant applicant = application.getApplicant();
        return User.builder().userName(applicant.getUserName())
            .fatherOrSpouseName(applicant.getFatherOrSpouseName())
            .mobileNumber(applicant.getMobileNumber())
            .emailId(applicant.getEmailId())
            .mobileNumber(applicant.getMobileNumber())
            .tenantId(applicant.getTenantId())
            .userType(applicant.getUserType())
            .roles(applicant.getRoles())
            .name(applicant.getName())
            .type(applicant.getUserType())
            .active(true)
            //.permanentAddress(application.getAddressOfDeceased().toString())
            //.correspondencePincode(application.getAddressOfDeceased().toString())
            .build();
    }

    private User upsertUser(User user, RequestInfo requestInfo) {

        String tenantId = user.getTenantId();
        User userServiceResponse = null;

        // Search on mobile number as user name
        UserDetailResponse userDetailResponse = searchUser(userUtils.getStateLevelTenant(tenantId),
            null, user.getMobileNumber());
        if (!userDetailResponse.getUser().isEmpty()) {
            User userFromSearch = userDetailResponse.getUser().get(0);
            log.info(userFromSearch.toString());
            if (!user.getUserName().equalsIgnoreCase(userFromSearch.getUserName())) {
                userServiceResponse = updateUser(requestInfo, user, userFromSearch);
            } else {
                userServiceResponse = userDetailResponse.getUser().get(0);
            }
        } else {
            userServiceResponse = createUser(requestInfo, tenantId, user);
        }

        // Enrich the accountId
        // user.setId(userServiceResponse.getUuid());
        return userServiceResponse;
    }


    public void enrichUser(DeathRegistrationApplication application, RequestInfo requestInfo) {
        String accountId = application.getApplicantId();
        String tenantId = application.getTenantId();

        UserDetailResponse userDetailResponse = searchUser(userUtils.getStateLevelTenant(tenantId),
            accountId, null);
        if (userDetailResponse.getUser().isEmpty()) {
            throw new CustomException("INVALID_ACCOUNTID", "No user exist for the given accountId");
        } else {
           User user =  userDetailResponse.getUser().get(0);
           Applicant applicant =  Applicant.builder().userName(user.getUserName())
                .fatherOrSpouseName(user.getFatherOrSpouseName())
                .mobileNumber(user.getMobileNumber())
                .emailId(user.getEmailId())
                .tenantId(user.getTenantId())
                .userType(user.getUserType())
                .roles(user.getRoles())
                .name(user.getName())
                .userType(user.getUserType())
               .build();
            application.setApplicant(applicant);
        }
    }



    /**
     * Creates the user from the given userInfo by calling user service
     *
     * @param requestInfo
     * @param tenantId
     * @param userInfo
     * @return
     */
    private User createUser(RequestInfo requestInfo, String tenantId, User userInfo) {

        userUtils.addUserDefaultFields(userInfo.getMobileNumber(), tenantId, userInfo);
        StringBuilder uri = new StringBuilder(config.getUserHost())
            .append(config.getUserContextPath())
            .append(config.getUserCreateEndpoint());

        CreateUserRequest user = new CreateUserRequest(requestInfo, userInfo);
        log.info(user.getUser().toString());
        UserDetailResponse userDetailResponse = userUtils.userCall(user, uri);

        return userDetailResponse.getUser().get(0);

    }

    /**
     * Updates the given user by calling user service
     *
     * @param requestInfo
     * @param user
     * @param userFromSearch
     * @return
     */
    private User updateUser(RequestInfo requestInfo, User user, User userFromSearch) {

        userFromSearch.setUserName(user.getUserName());
        userFromSearch.setActive(true);

        StringBuilder uri = new StringBuilder(config.getUserHost())
            .append(config.getUserContextPath())
            .append(config.getUserUpdateEndpoint());

        UserDetailResponse userDetailResponse = userUtils.userCall(
            new CreateUserRequest(requestInfo, userFromSearch), uri);

        return userDetailResponse.getUser().get(0);

    }

    /**
     * calls the user search API based on the given accountId and userName
     *
     * @param stateLevelTenant
     * @param accountId
     * @param userName
     * @return
     */
    public UserDetailResponse searchUser(String stateLevelTenant, String accountId,
        String userName) {

        UserSearchRequest userSearchRequest = new UserSearchRequest();
        userSearchRequest.setActive(true);
        userSearchRequest.setUserType("CITIZEN");
        userSearchRequest.setTenantId(stateLevelTenant);

        if (StringUtils.isEmpty(accountId) && StringUtils.isEmpty(userName)) {
            return null;
        }

        if (!StringUtils.isEmpty(accountId)) {
            userSearchRequest.setUuid(Collections.singletonList(accountId));
        }

        if (!StringUtils.isEmpty(userName)) {
            userSearchRequest.setUserName(userName);
        }

        StringBuilder uri = new StringBuilder(config.getUserHost()).append(
            config.getUserSearchEndpoint());
        return userUtils.userCall(userSearchRequest, uri);

    }

    /**
     * calls the user search API based on the given list of user uuids
     *
     * @param uuids
     * @return
     */
    private Map<String, User> searchBulkUser(List<String> uuids) {

        UserSearchRequest userSearchRequest = new UserSearchRequest();
        userSearchRequest.setActive(true);
        userSearchRequest.setUserType("CITIZEN");

        if (!CollectionUtils.isEmpty(uuids)) {
            userSearchRequest.setUuid(uuids);
        }

        StringBuilder uri = new StringBuilder(config.getUserHost()).append(
            config.getUserSearchEndpoint());
        UserDetailResponse userDetailResponse = userUtils.userCall(userSearchRequest, uri);
        List<User> users = userDetailResponse.getUser();

        if (CollectionUtils.isEmpty(users)) {
            throw new CustomException("USER_NOT_FOUND", "No user found for the uuids");
        }

        Map<String, User> idToUserMap = users.stream()
            .collect(Collectors.toMap(User::getUuid, Function.identity()));

        return idToUserMap;
    }

}