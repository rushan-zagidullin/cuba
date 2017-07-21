/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.FileDescriptorResource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.vaadin.server.StreamResource;
import org.apache.commons.lang.StringUtils;

public class WebFileDescriptorResource extends WebAbstractStreamSettingsResource
        implements WebResource, FileDescriptorResource {

    protected static final String FILE_STORAGE_EXCEPTION_MESSAGE = "Can't create FileDescriptorResource. " +
            "An error occurred while obtaining a file from the storage";

    protected FileDescriptor fileDescriptor;

    protected String mimeType;

    @Override
    public FileDescriptorResource setFileDescriptor(FileDescriptor fileDescriptor) {
        Preconditions.checkNotNullArgument(fileDescriptor);

        this.fileDescriptor = fileDescriptor;
        hasSource = true;

        fireResourceUpdateEvent();

        return this;
    }

    @Override
    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    @Override
    protected void createResource() {
        String name = StringUtils.isNotEmpty(fileName) ? fileName : fileDescriptor.getName();

        resource = new StreamResource(() -> {
            try {
                return new ByteArrayDataProvider(AppBeans.get(FileStorageService.class).loadFile(fileDescriptor))
                        .provide();
            } catch (FileStorageException e) {
                throw new RuntimeException(FILE_STORAGE_EXCEPTION_MESSAGE, e);
            }
        }, name);

        StreamResource streamResource = (StreamResource) this.resource;

        streamResource.setCacheTime(cacheTime);
        streamResource.setBufferSize(bufferSize);
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;

        if (resource != null) {
            ((StreamResource) resource).setMIMEType(mimeType);
        }
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }
}
