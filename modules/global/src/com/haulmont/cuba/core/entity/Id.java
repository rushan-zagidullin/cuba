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

package com.haulmont.cuba.core.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Convenient class for methods that receive Id of an entity as a parameter.
 *
 * @param <K> type of entity key
 * @param <T> entity type
 */
public final class Id<T extends Entity<K>, K> implements Serializable {
    private final K id;
    private final Class<T> entityClass;

    private Id(K id, Class<T> entityClass) {
        this.id = id;
        this.entityClass = entityClass;
    }

    /**
     * @return value of entity id
     */
    public K getValue() {
        return id;
    }

    /**
     * @return class of entity
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * @param entity entity instance
     * @param <K>    type of entity key
     * @param <T>    entity type
     * @return Id of the passed entity
     */
    public static <T extends Entity<K>, K> Id<T, K> of(T entity) {
        checkNotNullArgument(entity);
        checkNotNullArgument(entity.getId());

        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) entity.getClass();
        return new Id<>(entity.getId(), entityClass);
    }

    /**
     * @param id entity id
     * @param entityClass entity class
     * @param <K>    type of entity key
     * @param <T>    entity type
     * @return Id of the passed entity
     */
    public static <T extends Entity<K>, K> Id<T, K> of(K id, Class<T> entityClass) {
        checkNotNullArgument(id);
        checkNotNullArgument(entityClass);

        return new Id<>(id, entityClass);
    }

    /**
     * @param entities entity instances
     * @param <K>      type of entity key
     * @param <T>      entity type
     * @return list of ids of the passed entities
     */
    public static <T extends Entity<K>, K> List<Id<T, K>> of(Collection<T> entities) {
        return entities.stream()
                .map(entity -> {
                    checkNotNullArgument(entity);
                    checkNotNullArgument(entity.getId());

                    @SuppressWarnings("unchecked")
                    Class<T> entityClass = (Class<T>) entity.getClass();
                    return new Id<>(entity.getId(), entityClass);
                })
                .collect(Collectors.toList());
    }
}