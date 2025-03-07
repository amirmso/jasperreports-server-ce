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
import com.jaspersoft.jasperserver.api.common.domain.impl.ExecutionContextImpl;
import com.jaspersoft.jasperserver.api.metadata.common.domain.ContentResource;
import com.jaspersoft.jasperserver.api.metadata.common.domain.FileResource;
import com.jaspersoft.jasperserver.api.metadata.common.domain.client.ContentResourceImpl;
import com.jaspersoft.jasperserver.api.metadata.common.domain.client.FileResourceImpl;
import com.jaspersoft.jasperserver.api.metadata.user.domain.ObjectPermission;
import com.jaspersoft.jasperserver.api.metadata.user.domain.client.ObjectPermissionImpl;
import com.jaspersoft.jasperserver.dto.resources.ClientFile;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * <p></p>
 *
 * @author Zakhar.Tomchenco
 * @version $Id$
 */
public class BinaryDataResourceConverterTest {
    @InjectMocks
    private BinaryDataResourceConverter converter = new BinaryDataResourceConverter();

    @Mock private ContentResourceConverter contentResourceConverter;
    @Mock private FileResourceConverter fileResourceConverter;
    @Mock private Set<String> fileResourceTypes;

    private final ClientFile file = new ClientFile();
    private final ObjectPermission repositoryPermission = new ObjectPermissionImpl();
    private ExecutionContext ctx  = ExecutionContextImpl.getRuntimeExecutionContext();

    @BeforeClass
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void fillSampleObjects() throws Exception{
        file.setLabel("test");
        reset(contentResourceConverter, fileResourceConverter, fileResourceTypes);
        when(fileResourceConverter.toServer(any(ExecutionContext.class), any(ClientFile.class), any(FileResource.class), any(ToServerConversionOptions.class))).thenReturn(new FileResourceImpl());
        when(contentResourceConverter.toServer(any(ExecutionContext.class), any(ClientFile.class), any(ContentResource.class), any(ToServerConversionOptions.class))).thenReturn(new ContentResourceImpl());
    }

    @Test
    public void testToServer_file() throws Exception {
        file.setType(ClientFile.FileType.prop);

        converter.toServer(ctx, file, new FileResourceImpl(), null);

        verify(fileResourceConverter).toServer(nullable(ExecutionContext.class), eq(file), nullable(FileResource.class), nullable(ToServerConversionOptions.class));
    }

    @Test
    public void testToServer_file_no_update() throws Exception {
        file.setType(ClientFile.FileType.prop);
        when(fileResourceTypes.contains(any())).thenReturn(true);

        converter.toServer(ctx, file, null);

        verify(fileResourceConverter).toServer(nullable(ExecutionContext.class), eq(file), nullable(FileResource.class), nullable(ToServerConversionOptions.class));
    }

    @Test
    public void testToServer_content() throws Exception {
        file.setType(ClientFile.FileType.css);

        converter.toServer(ctx, file, new ContentResourceImpl(), null);

        verify(contentResourceConverter).toServer(nullable(ExecutionContext.class), eq(file), nullable(ContentResource.class), nullable(ToServerConversionOptions.class));
    }

    @Test
    public void testToServer_content_no_update() throws Exception {
        file.setType(ClientFile.FileType.css);
        when(fileResourceTypes.contains(any())).thenReturn(false);

        converter.toServer(ctx, file, null);

        verify(contentResourceConverter).toServer(nullable(ExecutionContext.class), eq(file), nullable(ContentResource.class), nullable(ToServerConversionOptions.class));
    }
}
