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
import com.jaspersoft.jasperserver.api.metadata.common.domain.ListOfValues;
import com.jaspersoft.jasperserver.api.metadata.common.domain.client.ListOfValuesImpl;
import com.jaspersoft.jasperserver.api.metadata.common.domain.client.ListOfValuesItemImpl;
import com.jaspersoft.jasperserver.api.metadata.user.service.ProfileAttributesResolver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * <p></p>
 *
 * @author Zakhar.Tomchenco
 * @version $Id$
 */
public class ListOfValuesValidatorTest {
    @InjectMocks
    final ListOfValuesValidator validator = new ListOfValuesValidator();
    @Mock
    private ProfileAttributesResolver profileAttributesResolver;

    ListOfValues lov;
    ExecutionContext ctx = ExecutionContextImpl.getRuntimeExecutionContext();

    @BeforeClass
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void setUp() {
        lov = new ListOfValuesImpl();
        lov.setLabel("aa");
    }

    @Test
    public void testValidate() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setLabel("test");
        item.setValue("lala");
        lov.addValue(item);

        validator.validate(ctx, lov);
    }

    @Test
    public void testValidate_nullItemLabel() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setValue("lala");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_nullItemValue() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setLabel("lala");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_emptyItemLabel() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setLabel("");
        item.setValue("lala");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_emptyItemValue() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setLabel("lala");
        item.setValue("");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_spasesItemValue() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setLabel("lala");
        item.setValue("      ");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_spasesItemLabel() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setLabel("     ");
        item.setValue("a");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_ItemLabelTooLong() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setLabel("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        item.setValue("a");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_ItemLabelUnsupportedSymbols() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setValue("1234567890");
        item.setLabel("<>");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }

    @Test
    public void testValidate_ItemValueUnsupportedSymbols() throws Exception {
        ListOfValuesItemImpl item = new ListOfValuesItemImpl();
        item.setValue("<>");
        item.setLabel("a");
        lov.addValue(item);

        final List<Exception> exceptions = validator.validate(ctx, lov);

        assertNotNull(exceptions);
        assertFalse(exceptions.isEmpty());
    }
}
