/*
 * Copyright (C) 2005 - 2020 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.jasperserver.dto.adhoc.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.jaspersoft.jasperserver.dto.adhoc.query.el.ClientExpression;
import com.jaspersoft.jasperserver.dto.adhoc.query.field.ClientQueryField;
import com.jaspersoft.jasperserver.dto.executions.ClientMultiLevelQueryExecution;
import com.jaspersoft.jasperserver.dto.resources.ClientReference;
import org.junit.Test;

import java.math.BigDecimal;

import static com.jaspersoft.jasperserver.dto.adhoc.query.ClientQueryBuilder.asc;
import static com.jaspersoft.jasperserver.dto.adhoc.query.ClientQueryBuilder.field;
import static com.jaspersoft.jasperserver.dto.adhoc.query.ClientQueryBuilder.source;
import static com.jaspersoft.jasperserver.dto.adhoc.query.el.ClientExpressions.range;
import static com.jaspersoft.jasperserver.dto.adhoc.query.el.ClientExpressions.variable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;


/**
 * @author Grant Bacon <gbacon@tibco.com>
 * @version $Id$
 * @date 1/27/16 3:47PM
 */
public class QueryExecutionRequestELSerializationToJSONTest extends QueryExecutionRequestTest {
    private static final String FIXTURES_PATH = "query/request/el/serialization/";

    // StringUtils is not available here
    public static final String EMPTY = "";

    // From FilterIT
    public static final String QUERY_EXECUTOR = "queryExecutions";

    public static final String SALES_STORE_REGION_SALES_CITY = "sales_fact_ALL.sales__store.sales__store__region.sales__store__region__sales_city";
    public static final String STORE_SALES = "sales_fact_ALL.sales_fact_ALL__store_sales_2013";
    public static final String STORE_NUMBER = "sales_fact_ALL.sales__store.sales__store__store_contact.sales__store__store_contact__store_number";
    public static final String SALES_PRODUCT_SKU = "sales_fact_ALL.sales__product.sales__product__SKU";
    public static final String EMPLOYEE_BIRTH_DATE = "employee.employee__employee_private.employee__employee_private__birth_date";
    public static final String EMPLOYEE_HIRE_DATE = "employee.employee__hire_date";

    public static final String TYPE_INTEGER = "java.lang.Integer";
    public static final String TYPE_LONG = "java.lang.Long";
    public static final String TYPE_DOUBLE = "java.lang.Double";
    public static final String TYPE_STRING = "java.lang.String";
    public static final String TYPE_DATE = "java.util.Date";
    public static final String TYPE_TIMESTAMP = "java.sql.Timestamp";
    // From MissingFilterIT
    public static String SALES = "sales1";
    public static ClientQueryField SALES_FIELD = field(SALES, source(STORE_SALES, TYPE_DOUBLE));

    // From FilterByIntegerVariableIT
    public static String NUMBER = "store_number";
    public static ClientQueryField STORE_NUMBER_FIELD = field(NUMBER, source(STORE_NUMBER, TYPE_INTEGER));

    // From ComparisonFiltersIT, ComparisonExpressionAsStringFiltersIT
    private static final String TABLE_VIEW_URI =
            "/public/Samples/Ad_Hoc_Views/04__Product_Results_by_Store_Type";
    public static String CITY = "city1";
    public static ClientQueryField CITY_FIELD = field(CITY, source(SALES_STORE_REGION_SALES_CITY, TYPE_STRING));

    // From FilterByLongVariableIT
    public static String PRODUCT_SKU = "product_SKU";
    public static ClientQueryField PRODUCT_SKU_FIELD = field(PRODUCT_SKU, source(SALES_PRODUCT_SKU, TYPE_LONG));

    protected ClientMultiLevelQueryExecution qer(ClientMultiLevelQuery query) {
        return new ClientMultiLevelQueryExecution(query, new ClientReference(DATASOURCE_URI));
    }

    @Test
    public void ensureRequestFrom_ComparisonFiltersIT_shouldFindSanFrancisco() throws Exception {
        ClientQueryBuilder qb =
                MultiLevelQueryBuilder.select(field("city1", source(SALES_STORE_REGION_SALES_CITY, TYPE_STRING)))
                        .where(variable("city1").eq("San Francisco"));

        JsonNode actualJson = toJsonObject(qer((ClientMultiLevelQuery) qb.build()));
        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_ComparisonFiltersIT_shouldFindSanFranciscoQuery.json"));

        assertThat(actualJson, is(expectedJson));

    }

    @Test
    public void ensureRequestFrom_ComparisonExpressionAsStringFiltersIT_shouldFindSanFrancisco() throws Exception {
        ClientQueryBuilder qb =
                MultiLevelQueryBuilder.select(field("city1", source(SALES_STORE_REGION_SALES_CITY, TYPE_STRING)))
                        .where("city1 == 'San Francisco'");

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_ComparisonExpressionAsStringFiltersIT_shouldFindSanFranciscoQuery.json"));
        JsonNode actualJson = toJsonObject(qer((ClientMultiLevelQuery) qb.build()));

        assertThat(actualJson, is(expectedJson));

    }

    @Test
    public void ensureRequestFrom_MissingFilterIT_shouldRetrunAllIfFilterIsNull() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(CITY_FIELD).where((ClientExpression) null).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_MissingFilterIT_shouldRetrunAllIfFilterIsNullQuery.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterEqual() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(SALES_FIELD).where(variable(SALES).eq(new BigDecimal("1.0"))).orderBy(asc(SALES)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterEqualQuery.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterNotEqual() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(SALES_FIELD).where(variable(SALES).notEq(1.0)).orderBy(asc(SALES))
                        .build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterNotEqualQuery.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertEquals(expectedJson, actualJson);

    }

    @Test
    public void ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterAllThatGreaterThan20() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(SALES_FIELD)
                        .where(variable(SALES).gt(20.0)).orderBy(asc(SALES)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterAllThatGreaterThan20Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterAllThatLessThan0_51() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(SALES_FIELD)
                        .where(variable(SALES).lt(new BigDecimal("0.51"))).orderBy(asc(SALES)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterAllThatLessThan0_51Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByFloatVariableIT_shouldFilterAllThatEqualsOrGreaterThen21() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(SALES_FIELD)
                        .where(variable(SALES).gtOrEq(new Float("21.0"))).orderBy(asc(SALES)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByFloatVariableIT_shouldFilterAllThatEqualsOrGreaterThen21Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterAllThatLessOrEqualsThen0_51() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(SALES_FIELD)
                        .where(variable(SALES).ltOrEq(new Double("0.51"))).orderBy(asc(SALES)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByDecimalVariableIT_shouldFilterAllThatLessOrEqualsThen0_51Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterEqual() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(STORE_NUMBER_FIELD).where(variable(NUMBER).eq(1)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterEqualQuery.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterNotEqual() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(STORE_NUMBER_FIELD).where(variable(NUMBER).notEq(1))
                        .build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterNotEqualQuery.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatGreaterThan23() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(STORE_NUMBER_FIELD)
                        .where(variable(NUMBER).gt(23)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatGreaterThan23Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatLessThan1() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(STORE_NUMBER_FIELD)
                        .where(variable(NUMBER).lt(1)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatLessThan1Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatEqualsOrGreaterThen23() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(STORE_NUMBER_FIELD)
                        .where(variable(NUMBER).gtOrEq(23)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatEqualsOrGreaterThen23Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertEquals(expectedJson, actualJson);

    }

    @Test
    public void ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatLessOrEqualsThen1() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(STORE_NUMBER_FIELD)
                        .where(variable(NUMBER).ltOrEq(1)).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByIntegerVariableIT_shouldFilterAllThatLessOrEqualsThen1Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_FilterByStringVariableIT_shouldFilterEqual() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(CITY_FIELD).where(variable(CITY).eq("San Francisco")).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_FilterByStringVariableIT_shouldFilterEqualQuery.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

    @Test
    public void ensureRequestFrom_InRangeFilterSpecifiedAsJsonIT_shouldFilterSalesBetween1and1p0001() throws Exception {
        ClientMultiLevelQuery query =
                MultiLevelQueryBuilder.select(SALES_FIELD).where(variable(SALES).in(range(new Double("1.0"), new Double("1.0001")))).build();

        JsonNode expectedJson = toJsonObject(fixture(FIXTURES_PATH + "ensureRequestFrom_InRangeFilterSpecifiedAsJsonIT_shouldFilterSalesBetween1and1p0001Query.json"));
        JsonNode actualJson = toJsonObject(qer(query));

        assertThat(actualJson, is(expectedJson));
    }

}