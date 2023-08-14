package digit.repository.rowmapper;

import digit.web.models.Address;
import digit.web.models.AuditDetails;
import digit.web.models.DeathRegistrationApplication;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class DeathApplicationRowMapper implements
    ResultSetExtractor<List<DeathRegistrationApplication>> {

    public List<DeathRegistrationApplication> extractData(ResultSet rs)
        throws SQLException, DataAccessException {
        Map<String, DeathRegistrationApplication> deathRegistrationApplicationMap = new LinkedHashMap<>();

        while (rs.next()) {
            String uuid = rs.getString("dapplicationno");
            DeathRegistrationApplication deathRegistrationApplication = deathRegistrationApplicationMap.get(
                uuid);

            if (deathRegistrationApplication == null) {

                Long lastModifiedTime = rs.getLong("dlastmodifiedtime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }

                AuditDetails auditdetails = AuditDetails.builder()
                    .createdBy(rs.getString("dcreatedby"))
                    .createdTime(rs.getLong("dcreatedtime"))
                    .lastModifiedBy(rs.getString("dlastmodifiedby"))
                    .lastModifiedTime(lastModifiedTime)
                    .build();

                deathRegistrationApplication = DeathRegistrationApplication.builder()
                    .applicationNumber(rs.getString("dapplicationno"))
                    .tenantId(rs.getString("dtenantid"))
                    .id(rs.getString("did"))
                    .applicantId(rs.getString("dapplicantid"))
                    .deceasedFirstName(rs.getString("dfirstname"))
                    .deceasedLastName(rs.getString("dlastname"))
                    .placeOfDeath(rs.getString("dplaceofdeath"))
                    .timeOfDeath(rs.getLong("dtimeofdeath"))
                    .auditDetails(auditdetails)
                    .build();
            }
            addChildrenToProperty(rs, deathRegistrationApplication);
            deathRegistrationApplicationMap.put(uuid, deathRegistrationApplication);
        }
        return new ArrayList<>(deathRegistrationApplicationMap.values());
    }

    private void addChildrenToProperty(ResultSet rs,
        DeathRegistrationApplication deathRegistrationApplication)
        throws SQLException {
        addAddressToApplication(rs, deathRegistrationApplication);
    }

    private void addAddressToApplication(ResultSet rs,
        DeathRegistrationApplication deathRegistrationApplication) throws SQLException {

        Address address = Address.builder()
            .id(rs.getString("aid"))
            .tenantId(rs.getString("atenantid"))
            .city(rs.getString("acity"))
            .pincode(rs.getString("pincode"))
            .registrationId("deathregid")
            .build();

        AuditDetails auditdetails = AuditDetails.builder()
            .createdBy(rs.getString("acreatedby"))
            .createdTime(rs.getLong("acreatedtime"))
            .lastModifiedBy(rs.getString("alastmodifiedby"))
            .lastModifiedTime(rs.getLong("alastmodifiedtime"))
            .build();

        address.setAuditDetails(auditdetails);

        deathRegistrationApplication.setAddressOfDeceased(address);

    }

}
