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

package com.jaspersoft.jasperserver.api.engine.common.virtualdatasourcequery;

import com.jaspersoft.jasperserver.api.common.virtualdatasourcequery.CustomDataSource;

/**
 * Not a complete collection of custom datasource properties.
 * These properties used in {@link CustomDataSource#getPropertyMap()}.
 */
public final class CustomDataSourceProps {

    /**
     * Property name for a custom datasource resource.
     */
    public static final String CDS_RESOURCE = "_cds_resource";

    private CustomDataSourceProps() {
        // non instantiable
    }

}
