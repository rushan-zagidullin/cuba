/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component(PersistentAttributesLoadChecker.NAME)
public class GlobalPersistentAttributesLoadChecker implements PersistentAttributesLoadChecker {

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Metadata metadata;

    protected enum PropertyLoadedState {
        YES,
        NO,
        UNKNOWN
    }

    @Override
    public boolean isLoaded(Object entity, String property) {
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        MetaProperty metaProperty = metaClass.getPropertyNN(property);

        if (!metadataTools.isPersistent(metaProperty)) {
            List<String> relatedProperties = metadataTools.getRelatedProperties(metaProperty);
            if (relatedProperties.isEmpty()) {
                return true;
            } else {
                for (String relatedProperty : relatedProperties) {
                    if (!isLoaded(entity, metaClass.getPropertyPath(relatedProperty)))
                        return false;
                }
                return true;
            }
        }

        PropertyLoadedState isLoaded = isLoadedCommonCheck(entity, property);
        if (isLoaded != PropertyLoadedState.UNKNOWN) {
            return isLoaded == PropertyLoadedState.YES;
        }

        return isLoadedSpecificCheck(entity, property, metaClass, metaProperty);
    }

    public boolean isLoaded(Object entity, MetaPropertyPath metaPropertyPath) {
        String[] propertyPath = metaPropertyPath.getPath();
        if (!isLoaded(entity, metaPropertyPath.getMetaProperties()[0].getName())) {
            return false;
        }
        if (propertyPath.length > 1) {
            Object value = ((Instance) entity).getValue(propertyPath[0]);
            if (!(value instanceof Instance)) {
                throw new IllegalArgumentException("");
            }
            MetaProperty[] nextMetaProperties = Arrays.copyOfRange(metaPropertyPath.getMetaProperties(), 1, propertyPath.length );
            if (!isLoaded(value, new MetaPropertyPath(nextMetaProperties[0].getDomain(), nextMetaProperties))) {
                return false;
            }
        }
        return true;
    }

    protected PropertyLoadedState isLoadedCommonCheck(Object entity, String property) {
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;

            String[] inaccessibleAttributes = BaseEntityInternalAccess.getInaccessibleAttributes(baseGenericIdEntity);
            if (inaccessibleAttributes != null) {
                for (String inaccessibleAttr : inaccessibleAttributes) {
                    if (inaccessibleAttr.equals(property))
                        return PropertyLoadedState.NO;
                }
            }

            if (entity instanceof FetchGroupTracker) {
                FetchGroup fetchGroup = ((FetchGroupTracker) entity)._persistence_getFetchGroup();
                if (fetchGroup != null) {
                    boolean inFetchGroup = fetchGroup.getAttributeNames().contains(property);
                    if (!inFetchGroup) {
                        // definitely not loaded
                        return PropertyLoadedState.NO;
                    } else {
                        // requires additional check specific for the tier
                        return PropertyLoadedState.UNKNOWN;
                    }
                }
            }
        }

        return PropertyLoadedState.UNKNOWN;
    }

    protected boolean isLoadedSpecificCheck(Object entity, String property, MetaClass metaClass, MetaProperty metaProperty) {
        return checkIsLoadedWithGetter(entity, property);
    }

    protected boolean checkIsLoadedWithGetter(Object entity, String property) {
        if (entity instanceof Instance) {
            try {
                Object value = ((Instance) entity).getValue(property);
                if (value instanceof Collection) {//check for IndirectCollection behaviour, should fail if property is not loaded
                    ((Collection) value).size();
                }
                return true;
            } catch (Exception ignored) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("Unable to check if the attribute is loaded: the entity is of unknown type");
        }
    }
}