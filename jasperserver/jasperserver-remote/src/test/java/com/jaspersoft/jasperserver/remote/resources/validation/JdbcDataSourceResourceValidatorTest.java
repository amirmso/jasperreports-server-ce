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

package com.jaspersoft.jasperserver.remote.resources.validation;

import com.jaspersoft.jasperserver.api.common.domain.ExecutionContext;
import com.jaspersoft.jasperserver.api.common.domain.impl.ExecutionContextImpl;
import com.jaspersoft.jasperserver.api.common.service.JdbcDriverService;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.JdbcReportDataSource;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.client.AwsReportDataSourceImpl;
import com.jaspersoft.jasperserver.api.metadata.user.service.ProfileAttributesResolver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * <p></p>
 *
 * @author Zakhar.Tomchenco
 * @version $Id$
 */
public class JdbcDataSourceResourceValidatorTest {
    @InjectMocks
    private final JdbcDataSourceResourceValidator validator = new JdbcDataSourceResourceValidator();
    @Mock
    private JdbcDriverService jdbcDriverService;
    @Mock
    private ProfileAttributesResolver profileAttributesResolver;

    private JdbcReportDataSource dataSource;
    ExecutionContext ctx = ExecutionContextImpl.getRuntimeExecutionContext();

    @BeforeClass
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        reset(jdbcDriverService);
        reset(profileAttributesResolver);
        when(jdbcDriverService.isRegistered(anyString())).thenReturn(true);
        when(profileAttributesResolver.containsAttribute(anyString())).thenReturn(false);

        dataSource = new AwsReportDataSourceImpl();
        dataSource.setLabel("tets");

        dataSource.setDriverClass("fas");
        dataSource.setConnectionUrl("jdbc:ajfa");
        dataSource.setTimezone("America/New_York");
    }

    @Test
    public void testValidate() throws Exception {
        validator.validate(ctx, dataSource);
    }

    @Test
    public void testValidate_no_driverClass() throws Exception {
        dataSource.setDriverClass(null);

        final List<Exception> exceptions = validator.validate(ctx, dataSource);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_unknownDriver() throws Exception {
        reset(jdbcDriverService);

        final List<Exception> exceptions = validator.validate(ctx, dataSource);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_noConnection() throws Exception {
        dataSource.setConnectionUrl(null);

        final List<Exception> exceptions = validator.validate(ctx, dataSource);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_invalidTimezione() throws Exception {
        dataSource.setTimezone("#$%^&*(OL)");

        final List<Exception> exceptions = validator.validate(ctx, dataSource);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_driverContainsAttribute() {
        reset(jdbcDriverService);
        when(profileAttributesResolver.containsAttribute(dataSource.getDriverClass())).thenReturn(true);

        validator.validate(ctx, dataSource);
    }

    @Test
    public void testValidate_connectionUrlContainsAttribute() {
        dataSource.setConnectionUrl("{attribute('name', 'category')}");
        when(profileAttributesResolver.containsAttribute(dataSource.getConnectionUrl())).thenReturn(true);

        validator.validate(ctx, dataSource);
    }
}
