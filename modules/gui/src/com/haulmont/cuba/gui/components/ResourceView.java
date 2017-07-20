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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.FileDescriptor;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.EventObject;
import java.util.function.Supplier;

public interface ResourceView extends Component, Component.HasCaption {

    /**
     * @return {@link Resource} instance
     */
    Resource getSource();

    /**
     * Sets the given {@link Resource} to the component.
     *
     * @param resource Resource instance
     */
    void setSource(Resource resource);

    /**
     * Creates the  resource with the given <code>type</code> and sets it to the component.
     *
     * @param type  resource class to be created
     * @return created  resource instance
     */
    <R extends Resource> R setSource(Class<R> type);

    /**
     * Creates  resource implementation by its type.
     *
     * @param type  resource type
     * @return  resource instance with given type
     */
    <R extends Resource> R createResource(Class<R> type);

    /**
     * Marker interface to indicate that the implementing class can be used as a resource.
     */
    interface Resource {
    }

    /**
     * Marker interface to indicate that the implementing class supports MIME type setting.
     */
    interface HasMimeType {
        /**
         * Sets the mime type of the resource.
         *
         * @param mimeType the MIME type to be set
         */
        void setMimeType(String mimeType);

        /**
         * @return resource MIME type
         */
        String getMimeType();
    }

    /**
     * Marker interface to indicate that the implementing class has stream settings (such as cache time, buffer size
     * or file name).
     */
    interface HasStreamSettings {
        /**
         * Sets the length of cache expiration time.
         *
         * <p>
         * This gives the adapter the possibility cache streams sent to the client. The caching may be made in adapter
         * or at the client if the client supports caching. Zero or negative value disables the caching of this stream.
         * </p>
         *
         * @param cacheTime the cache time in milliseconds
         */
        void setCacheTime(long cacheTime);

        /**
         * @return resource cache time
         */
        long getCacheTime();

        /**
         * Sets the size of the download buffer used for this resource.
         *
         * @param bufferSize the size of the buffer in bytes
         */
        void setBufferSize(int bufferSize);

        /**
         * @return buffer size
         */
        int getBufferSize();

        /**
         * Sets the filename.
         *
         * @param fileName the filename to set
         */
        void setFileName(String fileName);

        /**
         * @return resource file name
         */
        String getFileName();
    }

    /**
     * A resource which represents an  which can be loaded from the given <code>URL</code>.
     */
    interface UrlResource extends Resource, HasMimeType {
        UrlResource setUrl(URL url);

        URL getUrl();
    }

    /**
     * A resource that represents an  stored in the file system as the given <code>File</code>.
     */
    interface FileResource extends Resource, HasStreamSettings {
        FileResource setFile(File file);

        File getFile();
    }

    /**
     * A resource that represents a theme , e.g., <code>VAADIN/themes/yourtheme/some/path/.png</code>.
     */
    interface ThemeResource extends Resource {
        /**
         * @param path path to the theme , e.g. "some/path/.png"
         * @return current ThemeResource instance
         */
        ThemeResource setPath(String path);

        String getPath();
    }

    /**
     * A resource that represents an , which can be obtained from the <code>FileStorage</code> using the given
     * <code>FileDescriptor</code>.
     */
    interface FileDescriptorResource extends Resource, HasMimeType, HasStreamSettings {
        FileDescriptorResource setFileDescriptor(FileDescriptor fileDescriptor);

        FileDescriptor getFileDescriptor();
    }

    /**
     * A resource that represents an  stored in the directory of your application, e.g.:
     * <code>${catalina.base}/webapps/appName/static/.png</code>.
     */
    interface RelativePathResource extends Resource, HasMimeType {
        /**
         * @param path path to the , e.g. "static/.png"
         * @return current RelativePathResource instance
         */
        RelativePathResource setPath(String path);

        String getPath();
    }

    /**
     * A resource that represents an  located in classpath with the given <code>path</code>.
     * <p>
     * For obtaining resources the {@link com.haulmont.cuba.core.global.Resources} infrastructure interface is using.
     * <p>
     * For example if your  is located in the web module and has the following path: "com/company/app/web/s/.png",
     * ClassPathResource's path should be: "/com/company/app/web/s/.png".
     */
    interface ClasspathResource extends Resource, HasMimeType, HasStreamSettings {
        ClasspathResource setPath(String path);

        String getPath();
    }

    /**
     * A resource that is a streaming representation of an .
     */
    interface StreamResource extends Resource, HasMimeType, HasStreamSettings {
        StreamResource setStreamSupplier(Supplier<InputStream> streamSupplier);

        Supplier<InputStream> getStreamSupplier();
    }

    /**
     * Adds a listener that will be notified when a source of an  is changed.
     */
    void addSourceChangeListener(SourceChangeListener listener);

    /**
     * Removes a listener that will be notified when a source of an  is changed.
     */
    void removeSourceChangeListener(SourceChangeListener listener);

    /**
     * Listener that will be notified when a source of an  is changed.
     */
    @FunctionalInterface
    interface SourceChangeListener {
        void sourceChanged(SourceChangeEvent event);
    }

    /**
     * SourceChangeEvent is fired when a source of an  is changed.
     */
    class SourceChangeEvent extends EventObject {
        protected Resource oldSource;
        protected Resource newSource;

        public SourceChangeEvent(Object source, Resource oldSource, Resource newSource) {
            super(source);

            this.oldSource = oldSource;
            this.newSource = newSource;
        }

        @Override
        public ResourceView getSource() {
            return (ResourceView) super.getSource();
        }

        public Resource getOldSource() {
            return oldSource;
        }

        public Resource getNewSource() {
            return newSource;
        }
    }
}
