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
package com.jaspersoft.jasperserver.remote.resources.converters;

import com.jaspersoft.jasperserver.api.common.domain.ExecutionContext;
import com.jaspersoft.jasperserver.api.metadata.common.domain.util.ToClientConversionOptions;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.BeanReportDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientBeanDataSource;
import com.jaspersoft.jasperserver.remote.exception.IllegalParameterValueException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 * @version $Id$
 */
@Service
public class BeanDataSourceResourceConverter extends ResourceConverterImpl<BeanReportDataSource, ClientBeanDataSource> {
    @Override
    protected BeanReportDataSource resourceSpecificFieldsToServer(ExecutionContext ctx, ClientBeanDataSource clientObject, BeanReportDataSource resultToUpdate, List<Exception> exceptions, ToServerConversionOptions options) throws IllegalParameterValueException {
        resultToUpdate.setBeanMethod(clientObject.getBeanMethod());
        resultToUpdate.setBeanName(clientObject.getBeanName());
        return resultToUpdate;
    }

    @Override
    protected ClientBeanDataSource resourceSpecificFieldsToClient(ClientBeanDataSource client, BeanReportDataSource serverObject, ToClientConversionOptions options) {
        client.setBeanMethod(serverObject.getBeanMethod());
        client.setBeanName(serverObject.getBeanName());
        return client;
    }
}
