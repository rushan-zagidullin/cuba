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

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.gui.components.ResourceView;
import com.haulmont.cuba.web.gui.components.imageresources.*;
import com.vaadin.server.DownloadStream;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.ui.AbstractEmbedded;

import java.util.Map;

public abstract class WebAbstractResourceView<T extends AbstractEmbedded> extends WebAbstractComponent<T>
        implements ResourceView {

    protected ResourceView.Resource resource;

    protected static final Map<Class<? extends Resource>, Class<? extends Resource>> resourcesClasses;

    static {
        ImmutableMap.Builder<Class<? extends Resource>, Class<? extends Resource>> builder =
                new ImmutableMap.Builder<>();

        builder.put(UrlResource.class, WebUrlResource.class);
        builder.put(ClasspathResource.class, WebClasspathResource.class);
        builder.put(ThemeResource.class, WebThemeResource.class);
        builder.put(FileDescriptorResource.class, WebFileDescriptorResource.class);
        builder.put(FileResource.class, WebFileResource.class);
        builder.put(StreamResource.class, WebStreamResource.class);
        builder.put(RelativePathResource.class, WebRelativePathResource.class);

        resourcesClasses = builder.build();
    }

    protected Runnable imageResourceUpdateHandler;

    protected WebAbstractResourceView() {
        imageResourceUpdateHandler = () -> {
            com.vaadin.server.Resource vRes = this.resource == null ? null : ((WebAbstractResource) this.resource).getResource();
            component.setSource(vRes);
        };
    }

    @Override
    public Resource getSource() {
        return resource;
    }

    @Override
    public void setSource(Resource resource) {
        if (SharedUtil.equals(this.resource, resource)) {
            return;
        }
        updateValue(resource);
    }

    protected void updateValue(Resource value) {
        Resource oldValue = this.resource;
        if (oldValue != null) {
            ((WebAbstractResource) oldValue).setResourceUpdatedHandler(null);
        }

        this.resource = value;

        com.vaadin.server.Resource vResource = null;
        if (value != null && ((WebAbstractResource) value).hasSource()) {
            vResource = ((WebAbstractResource) value).getResource();
        }
        component.setSource(vResource);

        if (value != null) {
            ((WebAbstractResource) value).setResourceUpdatedHandler(imageResourceUpdateHandler);
        }

        getEventRouter().fireEvent(SourceChangeListener.class, SourceChangeListener::sourceChanged,
                new SourceChangeEvent(this, oldValue, this.resource));
    }

    @Override
    public <R extends Resource> R setSource(Class<R> type) {
        R resource = createResource(type);

        updateValue(resource);

        return resource;
    }

    @Override
    public <R extends Resource> R createResource(Class<R> type) {
        Class<? extends ResourceView.Resource> imageResourceClass = resourcesClasses.get(type);
        if (imageResourceClass == null) {
            throw new IllegalStateException(String.format("Can't find image resource class for '%s'", type.getTypeName()));
        }

        try {
            return type.cast(imageResourceClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Error creating the '%s' image resource instance",
                    type.getTypeName()), e);
        }
    }

    @Override
    public void addSourceChangeListener(SourceChangeListener listener) {
        getEventRouter().addListener(SourceChangeListener.class, listener);
    }

    @Override
    public void removeSourceChangeListener(SourceChangeListener listener) {
        getEventRouter().removeListener(SourceChangeListener.class, listener);
    }

    public abstract static class WebAbstractResource implements WebResource {
        protected com.vaadin.server.Resource resource;
        protected Runnable resourceUpdateHandler;

        protected boolean hasSource = false;

        @Override
        public com.vaadin.server.Resource getResource() {
            if (resource == null) {
                createResource();
            }
            return resource;
        }

        protected boolean hasSource() {
            return hasSource;
        }

        protected void fireResourceUpdateEvent() {
            resource = null;

            if (resourceUpdateHandler != null) {
                resourceUpdateHandler.run();
            }
        }

        protected void setResourceUpdatedHandler(Runnable resourceUpdated) {
            this.resourceUpdateHandler = resourceUpdated;
        }

        protected abstract void createResource();
    }

    public abstract static class WebAbstractStreamSettingsResource extends WebAbstractResource implements HasStreamSettings {
        protected long cacheTime = DownloadStream.DEFAULT_CACHETIME;
        protected int bufferSize;
        protected String fileName;

        @Override
        public void setCacheTime(long cacheTime) {
            this.cacheTime = cacheTime;

            if (resource != null) {
                ((com.vaadin.server.StreamResource) resource).setCacheTime(cacheTime);
            }
        }

        @Override
        public long getCacheTime() {
            return cacheTime;
        }

        @Override
        public void setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;

            if (resource != null) {
                ((com.vaadin.server.StreamResource) resource).setBufferSize(bufferSize);
            }
        }

        @Override
        public int getBufferSize() {
            return bufferSize;
        }

        @Override
        public void setFileName(String fileName) {
            this.fileName = fileName;

            if (resource != null) {
                ((com.vaadin.server.StreamResource) resource).setFilename(fileName);
            }
        }

        @Override
        public String getFileName() {
            return fileName;
        }
    }
}
