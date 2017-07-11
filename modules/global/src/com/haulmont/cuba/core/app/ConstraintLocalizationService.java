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
    String getLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage, Locale locale);

    @Nullable
    String getLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage, String localeCode);

    String putLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage,
                                  Locale locale, String message);

    String putLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage,
                                  String localeCode, String message);

    @Nullable
    String getLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage, Locale locale);

    @Nullable
    String getLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage, String localeCode);

    String putLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage,
                                  Locale locale, String message);

    String putLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage,
                                  String localeCode, String message);
}