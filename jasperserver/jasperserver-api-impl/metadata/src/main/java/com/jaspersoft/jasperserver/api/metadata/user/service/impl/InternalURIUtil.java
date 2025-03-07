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
package com.jaspersoft.jasperserver.api.metadata.user.service.impl;

import com.jaspersoft.jasperserver.api.metadata.common.domain.InternalURI;
import com.jaspersoft.jasperserver.api.metadata.common.domain.PermissionUriProtocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.acls.model.ObjectIdentity;

/**
 * @author Volodya Sabadosh
 */
class InternalURIUtil {
    protected static final Log log = LogFactory.getLog(InternalURIUtil.class);

    static InternalURI getInternalUriFor(ObjectIdentity objectIdentity) {
        String identifier = objectIdentity.getIdentifier().toString();
        if (identifier.contains(PermissionUriProtocol.DOBLE_FOLDER_SEPARATOR)) {
            log.warn("InternalURIUtil: incorrect URI detected !!!");
            Thread.dumpStack();
        }

        InternalURI internalURI = objectIdentity instanceof InternalURI && !identifier.contains(PermissionUriProtocol.
                DOBLE_FOLDER_SEPARATOR) ? (InternalURI) objectIdentity : new InternalURIDefinition(identifier);

        // cut "repo:" or "attr:" from internalURI, because getObjectPermissionsForObject always adds "repo:" to uri
        for (PermissionUriProtocol protocol : PermissionUriProtocol.values()) {
            if (internalURI.getPath().startsWith(protocol.getProtocolPrefix())) {
                internalURI = new InternalURIDefinition(PermissionUriProtocol.removePrefix(internalURI.getPath()),
                        PermissionUriProtocol.getProtocol(internalURI.getPath()));
            }
        }
        return internalURI;
    }

}
