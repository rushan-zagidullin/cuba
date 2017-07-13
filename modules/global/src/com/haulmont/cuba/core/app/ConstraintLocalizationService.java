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

    /**
     * Tries to find an instance of {@link LocalizedConstraintMessage} by given entity name and operation type.
     *
     * @param entityName    the entity name
     * @param operationType the operation type
     * @return an instance of {@link LocalizedConstraintMessage} with given entity name and operation type
     * or null if nothing found.
     */
    @Nullable
    LocalizedConstraintMessage findLocalizedConstraintMessage(String entityName,
                                                              ConstraintOperationType operationType);

    /**
     * Gets caption value from all messages for given locale.
     *
     * @param values all localized messages from a {@link LocalizedConstraintMessage} instance
     * @param locale   the locale
     * @return caption value from all messages for given locale
     */
    @Nullable
    String getLocalizedCaption(String values, Locale locale);

    /**
     * Gets caption value from all values for given locale code.
     *
     * @param values   all localized values from a {@link LocalizedConstraintMessage} instance
     * @param localeCode the locale code
     * @return caption value from all values for given locale code
     */
    @Nullable
    String getLocalizedCaption(String values, String localeCode);

    /**
     * Add caption value to passed values with given locale.
     *
     * @param values all localized values from a {@link LocalizedConstraintMessage} instance
     * @param locale   the locale
     * @param value    the value to add
     * @return values copy with added value
     */
    String putLocalizedCaption(String values, Locale locale, String value);

    /**
     * Add caption value to passed values with given locale code.
     *
     * @param values   all localized values from a {@link LocalizedConstraintMessage} instance
     * @param localeCode the locale code
     * @param value      the value to add
     * @return values copy with added value
     */
    String putLocalizedCaption(String values, String localeCode, String value);

    /**
     * Gets message value from all values for given locale.
     *
     * @param values all localized values from a {@link LocalizedConstraintMessage} instance
     * @param locale   the locale
     * @return message value from all values for given locale
     */
    @Nullable
    String getLocalizedMessage(String values, Locale locale);

    /**
     * Gets message value from all values for given locale code.
     *
     * @param values   all localized values from a {@link LocalizedConstraintMessage} instance
     * @param localeCode the locale code
     * @return message value from all values for given locale code
     */
    @Nullable
    String getLocalizedMessage(String values, String localeCode);

    /**
     * Add message value to passed values with given locale.
     *
     * @param values all localized values from a {@link LocalizedConstraintMessage} instance
     * @param locale   the locale
     * @param value    the value to add
     * @return values copy with added value
     */
    String putLocalizedMessage(String values, Locale locale, String value);

    /**
     * Add message value to passed values with given locale code.
     *
     * @param values   all localized values from a {@link LocalizedConstraintMessage} instance
     * @param localeCode the locale code
     * @param value      the value to add
     * @return values copy with added value
     */
    String putLocalizedMessage(String values, String localeCode, String value);
}