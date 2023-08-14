package digit.repository.querybuilder;

import digit.web.models.DeathApplicationSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Component
public class DeathApplicationQueryBuilder {

    private static final String BASE_DTR_QUERY = " SELECT "
        + "dtr.id as did,"
        + " dtr.tenantid as dtenantid,"
        + " dtr.applicantid as dapplicantid,"
        + " dtr.applicationno as dapplicationno,"
        + " dtr.firstname as dfirstname,"
        + " dtr.lastname as dlastname,"
        + " dtr.timeofdeath as dtimeofdeath,"
        + " dtr.placeofdeath as dplaceofdeath,"
        + " dtr.createdby as dcreatedby,"
        + " dtr.lastmodifiedby as dlastmodifiedby,"
        + " dtr.createdtime as dcreatedtime,"
        + " dtr.lastmodifiedtime as dlastmodifiedtime, ";

    private static final String ADDRESS_SELECT_QUERY = " add.id as aid,"
        + " add.tenantid as atenantid,"
        + " add.city as acity,"
        + " add.pincode as pincode,"
        + " add.latitude as latitude,"
        + " add.longitude as longitude,"
        + " add.addressid as addressid,"
        + " add.addressnumber as addressno,"
        + " add.addressline1 as addressline1,"
        + " add.addressline2 as addressline2,"
        + " add.landmark as landmark,"
        + " add.detail as adetail,"
        + " add.createdby as acreatedby,"
        + " add.createdtime as acreatedtime,"
        + " add.lastmodifiedby as alastmodifiedby,"
        + " add.lastmodifiedtime as alastmodifiedtime,"
        + " add.registrationid as deathregid ";

    private static final String FROM_TABLES = " FROM egov_dt_registration dtr LEFT JOIN egov_dt_address add ON dtr.id = add.registrationid ";

    private final String ORDERBY_CREATEDTIME = " ORDER BY dtr.createdtime DESC ";

    public String getDeathApplicationSearchQuery(DeathApplicationSearchCriteria criteria,
        List<Object> preparedStmtList) {
        StringBuilder query = new StringBuilder(BASE_DTR_QUERY);
        query.append(ADDRESS_SELECT_QUERY);
        query.append(FROM_TABLES);

        if (!ObjectUtils.isEmpty(criteria.getTenantId())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.tenantid = ? ");
            preparedStmtList.add(criteria.getTenantId());
        }
        if (!ObjectUtils.isEmpty(criteria.getStatus())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.status = ? ");
            preparedStmtList.add(criteria.getStatus());
        }
        if (!CollectionUtils.isEmpty(criteria.getIds())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.id IN ( ").append(createQuery(criteria.getIds())).append(" ) ");
            addToPreparedStatement(preparedStmtList, criteria.getIds());
        }
        if (!ObjectUtils.isEmpty(criteria.getApplicationNumber())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" dtr.applicationno = ? ");
            preparedStmtList.add(criteria.getApplicationNumber());
        }

        // order death registration applications based on their createdtime in latest first manner
        query.append(ORDERBY_CREATEDTIME);

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
        });
    }
}