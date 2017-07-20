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
     * Creates the resource with the given <code>type</code> and sets it to the component.
     *
     * @param type resource class to be created
     * @return new resource instance
     */
    <T extends Resource> T setSource(Class<T> type);

    /**
     * Creates resource implementation by its type.
     *
     * @param type resource type
     * @return new resource instance with given type
     */
    <T extends Resource> T createResource(Class<T> type);

    /**
     * Sets this component's alternate text that can be presented instead of the component's normal content for
     * accessibility purposes.
     *
     * @param alternateText a short, human-readable description of this component's content
     */
    void setAlternateText(String alternateText);

    /**
     * Gets this component's alternate text that can be presented instead of the component's normal content for
     * accessibility purposes.
     *
     * @return alternate text
     */
    String getAlternateText();

    /**
     * Adds a listener that will be notified when a source is changed.
     */
    void addSourceChangeListener(SourceChangeListener listener);

    /**
     * Removes a listener that will be notified when a source is changed.
     */
    void removeSourceChangeListener(SourceChangeListener listener);

    /**
     * Listener that will be notified when a source is changed.
     */
    @FunctionalInterface
    interface SourceChangeListener {
        void sourceChanged(SourceChangeEvent event);
    }

    /**
     * SourceChangeEvent is fired when a source is changed.
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

    /**
     * A resource that can be loaded from the given <code>URL</code>.
     */
    interface UrlResource extends Resource, HasMimeType {
        UrlResource setUrl(URL url);

        URL getUrl();
    }

    /**
     * A resource that is stored in the file system as the given <code>File</code>.
     */
    interface FileResource extends Resource, HasStreamSettings {
        FileResource setFile(File file);

        File getFile();
    }

    /**
     * A resource that can be obtained from the <code>FileStorage</code> using the given <code>FileDescriptor</code>.
     */
    interface FileDescriptorResource extends Resource, HasMimeType, HasStreamSettings {
        FileDescriptorResource setFileDescriptor(FileDescriptor fileDescriptor);

        FileDescriptor getFileDescriptor();
    }

    /**
     * A resource that is located in classpath with the given <code>path</code>.
     * <p>
     * For obtaining resources the {@link com.haulmont.cuba.core.global.Resources} infrastructure interface is using.
     * <p>
     * For example if your resource is located in the web module and has the following path: "com/company/app/web/images/image.png",
     * ClassPathResource's path should be: "/com/company/app/web/images/image.png".
     */
    interface ClasspathResource extends Resource, HasMimeType, HasStreamSettings {
        ClasspathResource setPath(String path);

        String getPath();
    }

    /**
     * A resource that is stored in the directory of your application, e.g.:
     * <code>${catalina.base}/webapps/appName/static/image.png</code>.
     */
    interface RelativePathResource extends Resource, HasMimeType {
        /**
         * @param path path to the resource, e.g. "static/image.png"
         * @return current RelativePathResource instance
         */
        RelativePathResource setPath(String path);

        String getPath();
    }

    interface StreamResource extends Resource, HasMimeType, HasStreamSettings {
        StreamResource setStreamSupplier(Supplier<InputStream> streamSupplier);

        Supplier<InputStream> getStreamSupplier();
    }

    /**
     * Marker interface to indicate that the implementing class can be used as a resource.
     */
    interface Resource {
    }

    /**
     * A theme resource, e.g. <code>VAADIN/themes/yourtheme/some/path/image.png</code>.
     */
    interface ThemeResource extends Resource {
        /**
         * @param path path to the theme resource, e.g. "some/path/image.png"
         * @return current ThemeResource instance
         */
        ThemeResource setPath(String path);

        String getPath();
    }
}
