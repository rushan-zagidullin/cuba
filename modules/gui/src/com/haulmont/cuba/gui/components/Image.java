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

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.EventObject;

/**
 * The Image component is intended for displaying graphic content.
 * <p>
 * It can be bound to a datasource or configured manually.
 */
public interface Image extends Component, Component.HasCaption {
    String NAME = "image";

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
     * Creates the image resource with the given <code>type</code> and sets it to the component.
     *
     * @param type image resource class to be created
     * @return created image resource instance
     */
    <T extends Resource> T setSource(Class<T> type);

    /**
     * Sets datasource and its property.
     */
    void setDatasource(Datasource datasource, String property);

    /**
     * @return datasource instance
     */
    Datasource getDatasource();

    /**
     * @return datasource property path
     */
    MetaPropertyPath getMetaPropertyPath();

    /**
     * Creates image resource implementation by its type.
     *
     * @param type image resource type
     * @return image resource instance with given type
     */
    <T extends Resource> T createResource(Class<T> type);

    /**
     * @return image scale mode
     */
    ScaleMode getScaleMode();

    /**
     * Applies the given scale mode to the image.
     *
     * @param scaleMode scale mode
     */
    void setScaleMode(ScaleMode scaleMode);

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
     * Adds a listener that will be notified when a source of an image is changed.
     */
    void addSourceChangeListener(SourceChangeListener listener);

    /**
     * Removes a listener that will be notified when a source of an image is changed.
     */
    void removeSourceChangeListener(SourceChangeListener listener);

    /**
     * Listener that will be notified when a source of an image is changed.
     */
    @FunctionalInterface
    interface SourceChangeListener {
        void sourceChanged(SourceChangeEvent event);
    }

    /**
     * SourceChangeEvent is fired when a source of an image is changed.
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
        public Image getSource() {
            return (Image) super.getSource();
        }

        public Resource getOldSource() {
            return oldSource;
        }

        public Resource getNewSource() {
            return newSource;
        }
    }

    /**
     * Defines image scale mode.
     */
    enum ScaleMode {
        /**
         * The image will be stretched according to the size of the component.
         */
        FILL,
        /**
         * The image will be compressed or stretched to the minimum measurement of the component while preserving the
         * proportions.
         */
        CONTAIN,
        /**
         * The content changes size by comparing the difference between NONE and CONTAIN, in order to find the smallest
         * concrete size of the object.
         */
        SCALE_DOWN,
        /**
         * The image will have a real size.
         */
        NONE
    }
}