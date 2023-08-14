package digit.validators;

import digit.repository.DeathRegistrationRepository;
import digit.web.models.DeathApplicationSearchCriteria;
import digit.web.models.DeathRegistrationApplication;
import digit.web.models.DeathRegistrationRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class DeathApplicationValidator {

    @Autowired
    private DeathRegistrationRepository repository;

    public void validateDeathApplication(DeathRegistrationRequest deathRegistrationRequest) {
        deathRegistrationRequest.getDeathRegistrationApplications().forEach(application -> {
            if (ObjectUtils.isEmpty(application.getTenantId())) {
                throw new CustomException("EG_BT_APP_ERR",
                    "tenantId is mandatory for creating death registration applications");
            }
        });
    }

    public List<DeathRegistrationApplication> validateApplicationUpdateRequest(
        DeathRegistrationRequest deathRegistrationRequest) {
        List<String> ids = deathRegistrationRequest.getDeathRegistrationApplications().stream()
            .map(DeathRegistrationApplication::getId).collect(
                Collectors.toList());
        List<DeathRegistrationApplication> deathRegistrationApplications = repository.getApplications(
            DeathApplicationSearchCriteria.builder().ids(ids).build());
        if (deathRegistrationApplications.size() != ids.size()) {
            throw new CustomException("APPLICATION_DOES_NOT_EXIST",
                "One of the application ids does not exist.");
        }
        return deathRegistrationRequest.getDeathRegistrationApplications();
    }
}