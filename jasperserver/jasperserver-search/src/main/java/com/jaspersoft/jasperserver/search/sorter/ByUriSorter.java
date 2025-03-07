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

package com.jaspersoft.jasperserver.search.sorter;

import java.io.Serializable;

import com.jaspersoft.jasperserver.api.common.domain.ExecutionContext;
import com.jaspersoft.jasperserver.api.search.SearchCriteria;
import com.jaspersoft.jasperserver.api.search.SearchSorter;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 * <p></p>
 *
 * @author Zakhar.Tomchenco
 * @version $Id$
 */
public class ByUriSorter extends SearchSorter implements Serializable {
    @Override
    protected void addOrder(String type, ExecutionContext context, SearchCriteria criteria) {
        criteria.addOrder(Order.asc("parent")).addOrder(Order.asc("name")).addOrder(Order.asc("id"));
    }

    @Override
    protected void addProjection(String type, ExecutionContext context, SearchCriteria criteria) {
        criteria.addProjection(Projections.property("parent")).addProjection(Projections.property("name"));
    }
}
