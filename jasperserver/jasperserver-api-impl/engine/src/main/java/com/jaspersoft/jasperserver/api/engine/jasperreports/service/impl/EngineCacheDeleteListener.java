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
package com.jaspersoft.jasperserver.api.engine.jasperreports.service.impl;

import com.jaspersoft.jasperserver.api.engine.common.service.EngineService;
import com.jaspersoft.jasperserver.api.metadata.common.service.RepositoryEventListenerSupport;
import com.jaspersoft.jasperserver.api.metadata.common.service.impl.FolderMoveEvent;
import com.jaspersoft.jasperserver.api.metadata.common.service.impl.RepositoryListener;
import com.jaspersoft.jasperserver.api.metadata.common.service.impl.ResourceCopiedEvent;
import com.jaspersoft.jasperserver.api.metadata.common.service.impl.ResourceMoveEvent;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class EngineCacheDeleteListener extends RepositoryEventListenerSupport implements RepositoryListener {

	private EngineService engine;
	
	public void onResourceDelete(Class resourceItf, String resourceURI) {
		engine.clearCaches(resourceItf, resourceURI);
	}

	public EngineService getEngine() {
		return engine;
	}

	public void setEngine(EngineService engine) {
		this.engine = engine;
	}

	public void folderMoved(FolderMoveEvent folderMove) {
		// NOOP
	}

	public void resourceMoved(ResourceMoveEvent resourceMove) {
		engine.clearCaches(resourceMove.getResourceType(), resourceMove.getOldResourceURI());
	}

	public void resourceCopied(ResourceCopiedEvent event) {
		// NOP
	}

}
