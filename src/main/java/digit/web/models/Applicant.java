package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * Details of the user applying for death registration
 */
@ApiModel(description = "Details of the user applying for death registration")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2023-08-10T20:00:06.666+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Applicant {

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("uuid")
    private String uuid = null;

    @JsonProperty("userName")
    private String userName = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("userType")
    private String userType = null;

    @JsonProperty("gender")
    private String gender = null;

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("salutation")
    private String salutation = null;

    @JsonProperty("pan")
    private String pan = null;

    @JsonProperty("aadhaarNumber")
    private String aadhaarNumber = null;

    @JsonProperty("mobileNumber")
    private String mobileNumber = null;

    @JsonProperty("emailId")
    private String emailId = null;

    @JsonProperty("roles")
    @Valid
    private List<Role> roles = new ArrayList<>();

    @JsonProperty("permanentAddress")
    private String permanentAddress = null;

    @JsonProperty("permanentCity")
    private String permanentCity = null;

    @JsonProperty("permanentPincode")
    private String permanentPincode = null;

    @JsonProperty("correspondencePincode")
    private String correspondencePincode = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("locale")
    private String locale = null;

    @JsonProperty("signature")
    private String signature = null;

    @JsonProperty("accountLocked")
    private Boolean accountLocked = null;

    @JsonProperty("fatherOrSpouseName")
    private String fatherOrSpouseName = null;

    @JsonProperty("bloodGroup")
    private String bloodGroup = null;

    @JsonProperty("identificationMark")
    private String identificationMark = null;

    @JsonProperty("photo")
    private String photo = null;


    public Applicant addRolesItem(Role rolesItem) {
        this.roles.add(rolesItem);
        return this;
    }

}

