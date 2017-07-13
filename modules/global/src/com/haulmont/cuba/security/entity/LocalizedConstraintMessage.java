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

package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Localized messages for security constraint.
 */
@Entity(name = "sec$LocalizedConstraintMessage")
@Table(name = "SEC_LOCALIZED_CONSTRAINT_MESSAGE")
@SystemLevel
public class LocalizedConstraintMessage extends StandardEntity {

    @Column(name = "ENTITY_NAME", length = 255, nullable = false)
    protected String entityName;

    @Column(name = "OPERATION_TYPE", length = 50, nullable = false)
    protected String operationType;

    @Lob
    @Column(name = "VALUES_")
    protected String values;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public ConstraintOperationType getOperationType() {
        return ConstraintOperationType.fromId(operationType);
    }

    public void setOperationType(ConstraintOperationType operationType) {
        this.operationType = operationType != null ? operationType.getId() : null;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
