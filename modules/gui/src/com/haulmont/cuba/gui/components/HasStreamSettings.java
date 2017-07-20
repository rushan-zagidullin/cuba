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

/**
 * Marker interface to indicate that the implementing class has stream settings (such as cache time, buffer size
 * or file name).
 */
public interface HasStreamSettings {
    /**
     * Sets the length of cache expiration time.
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
