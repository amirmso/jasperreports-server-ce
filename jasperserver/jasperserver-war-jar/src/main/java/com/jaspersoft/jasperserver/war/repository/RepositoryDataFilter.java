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

package com.jaspersoft.jasperserver.war.repository;

/**
 * DO NOT REMOVE: this class is used by {@link com.tonbeller.jpivot.table.RepoFolderList}
 *
 * @author Sergey Prilukin
 */
public interface RepositoryDataFilter {
    
    /**
     * Returns true if uri could be visible in context of current user and false otherwise
     * @param uri - uri of resource or folder
     * @return true if uri could be visible in context of current user and false otherwise
     */
    public boolean filter(String uri);
}
