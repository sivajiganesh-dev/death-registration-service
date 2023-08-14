package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * A Object holds the basic data for a Death Registration Application
 */
@ApiModel(description = "A Object holds the basic data for a Death Registration Application")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2023-08-10T20:00:06.666+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeathRegistrationApplication {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    private String applicationNumber = null;

    @JsonProperty("deceasedFirstName")
    private String deceasedFirstName = null;

    @JsonProperty("deceasedLastName")
    private String deceasedLastName = null;

    @JsonProperty("placeOfDeath")
    private String placeOfDeath = null;

    @JsonProperty("timeOfDeath")
    private Long timeOfDeath = null;

    @JsonProperty("addressOfDeceased")
    private Address addressOfDeceased = null;

    @JsonProperty("applicant")
    private Applicant applicant = null;

    @JsonProperty("applicantId")
    private String applicantId = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @Valid
    @JsonProperty("workflow")
    private Workflow workflow = null;
}

