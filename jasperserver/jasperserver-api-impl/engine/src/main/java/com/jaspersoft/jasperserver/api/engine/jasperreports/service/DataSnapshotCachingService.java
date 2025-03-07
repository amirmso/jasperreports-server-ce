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
package com.jaspersoft.jasperserver.api.engine.jasperreports.service;

import net.sf.jasperreports.data.cache.DataSnapshot;

import com.jaspersoft.jasperserver.api.common.domain.ExecutionContext;
import com.jaspersoft.jasperserver.api.metadata.data.cache.DataCacheSnapshot;
import com.jaspersoft.jasperserver.api.metadata.data.cache.DataSnapshotPersistentMetadata;
import com.jaspersoft.jasperserver.api.metadata.data.cache.DataSnapshotSavedId;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public interface DataSnapshotCachingService {

	DataSnapshotPersistentMetadata getSnapshotMetadata(ExecutionContext context, long snapshotId);

	void putSnapshotMetadata(ExecutionContext context, long snapshotId, DataSnapshotPersistentMetadata metadata);

	void invalidateSnapshot(ExecutionContext context, long snapshotId);
	
	DataSnapshot getSnapshotContents(ExecutionContext context, long contentsId);

	void putSnapshotContents(ExecutionContext context, long contentsId, DataSnapshot dataSnapshot);

	void put(ExecutionContext context, DataSnapshotSavedId savedId, DataCacheSnapshot snapshot);

}
