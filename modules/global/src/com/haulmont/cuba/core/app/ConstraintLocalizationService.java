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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.LocalizedConstraintMessage;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Service allowing
 */
public interface ConstraintLocalizationService {

    String NAME = "cuba_ConstraintLocalizationService";

    @Nullable
    LocalizedConstraintMessage getLocalizedConstraintMessage(String entityName,
                                                             ConstraintOperationType operationType);

    @Nullable
    String getLocalizedCaption(String messages, Locale locale);

    @Nullable
    String getLocalizedCaption(String messages, String localeCode);

    String putLocalizedCaption(String messages, Locale locale, String message);

    String putLocalizedCaption(String messages, String localeCode, String message);

    @Nullable
    String getLocalizedMessage(String messages, Locale locale);

    @Nullable
    String getLocalizedMessage(String messages, String localeCode);

    String putLocalizedMessage(String messages, Locale locale, String message);

    String putLocalizedMessage(String messages, String localeCode, String message);
}