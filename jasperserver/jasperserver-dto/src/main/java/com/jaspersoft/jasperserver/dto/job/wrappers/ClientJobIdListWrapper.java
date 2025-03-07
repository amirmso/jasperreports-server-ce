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
package com.jaspersoft.jasperserver.dto.job.wrappers;

import com.jaspersoft.jasperserver.dto.common.DeepCloneable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

import static com.jaspersoft.jasperserver.dto.utils.ValueObjectUtils.checkNotNull;
import static com.jaspersoft.jasperserver.dto.utils.ValueObjectUtils.copyOf;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
@XmlRootElement(name = "jobIdList")
public class ClientJobIdListWrapper implements DeepCloneable<ClientJobIdListWrapper>{

    private List<Long> ids;

    public ClientJobIdListWrapper() {
    }

    public ClientJobIdListWrapper(List<Long> ids) {
        if (ids == null) {
            return;
        }
        this.ids = new LinkedList<Long>(ids);
    }

    public ClientJobIdListWrapper(ClientJobIdListWrapper other) {
        checkNotNull(other);

        ids = copyOf(other.getIds());
    }

    @XmlElement(name = "jobId")
    public List<Long> getIds() {
        return ids;
    }

    public ClientJobIdListWrapper setIds(List<Long> ids) {
        this.ids = ids;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientJobIdListWrapper that = (ClientJobIdListWrapper) o;

        if (ids != null ? !ids.equals(that.ids) : that.ids != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ids != null ? ids.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientJobIdListWrapper{" +
                "ids=" + ids +
                '}';
    }

    @Override
    public ClientJobIdListWrapper deepClone() {
        return new ClientJobIdListWrapper(this);
    }
}
