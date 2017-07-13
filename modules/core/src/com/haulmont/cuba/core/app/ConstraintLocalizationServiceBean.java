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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.LocalizedConstraintMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

@Service(ConstraintLocalizationService.NAME)
public class ConstraintLocalizationServiceBean implements ConstraintLocalizationService {

    protected static final String CAPTION_KEY = "caption";
    protected static final String MESSAGE_KEY = "message";

    @Inject
    private DataManager dataManager;

    @Nullable
    @Override
    public LocalizedConstraintMessage findLocalizedConstraintMessage(String entityName,
                                                                     ConstraintOperationType operationType) {
        Preconditions.checkNotNullArgument(entityName);
        Preconditions.checkNotNullArgument(operationType);

        LoadContext<LocalizedConstraintMessage> loadContext = new LoadContext<>(LocalizedConstraintMessage.class);
        loadContext.setQueryString("select e from sec$LocalizedConstraintMessage e " +
                "where e.entityName = :name and e.operationType = :type")
                .setParameter("name", entityName)
                .setParameter("type", operationType);

        List<LocalizedConstraintMessage> localizations = dataManager.loadList(loadContext);

        if (CollectionUtils.isEmpty(localizations)) {
            return null;
        } else if (localizations.size() == 1) {
            return localizations.get(0);
        } else {
            throw new IllegalStateException("Several entities with the same 'entity name/operation type' combination");
        }
    }

    @Nullable
    protected String getValue(String values, String localeCode, String key) {
        Preconditions.checkNotNullArgument(localeCode);

        if (StringUtils.isEmpty(values)) {
            return null;
        }

        JSONObject localizationObject = new JSONObject(values);

        if (localizationObject.has(localeCode)) {
            JSONObject localeObject = localizationObject.getJSONObject(localeCode);
            return localeObject.has(key)
                    ? localeObject.getString(key)
                    : null;
        }

        return null;
    }

    protected String putValue(String values, String localeCode, String key, String value) {
        Preconditions.checkNotNullArgument(localeCode);


        JSONObject localizationObject = values != null
                ? new JSONObject(values)
                : new JSONObject();

        JSONObject localeObject = localizationObject.has(localeCode)
                ? localizationObject.getJSONObject(localeCode)
                : new JSONObject();

        localeObject.put(key, value);
        localizationObject.put(localeCode, localeObject);

        return localizationObject.toString();
    }

    @Nullable
    @Override
    public String getLocalizedCaption(String values, Locale locale) {
        Preconditions.checkNotNullArgument(locale);
        return getLocalizedCaption(values, locale.toLanguageTag());
    }

    @Nullable
    @Override
    public String getLocalizedCaption(String values, String localeCode) {
        return getValue(values, localeCode, CAPTION_KEY);
    }

    @Override
    public String putLocalizedCaption(String values, Locale locale, String value) {
        Preconditions.checkNotNullArgument(locale);
        return putLocalizedCaption(values, locale.toLanguageTag(), value);
    }

    @Override
    public String putLocalizedCaption(String values, String localeCode, String value) {
        return putValue(values, localeCode, CAPTION_KEY, value);
    }

    @Nullable
    @Override
    public String getLocalizedMessage(String values, Locale locale) {
        Preconditions.checkNotNullArgument(locale);
        return getLocalizedMessage(values, locale.toLanguageTag());
    }

    @Nullable
    @Override
    public String getLocalizedMessage(String values, String localeCode) {
        return getValue(values, localeCode, MESSAGE_KEY);
    }

    @Override
    public String putLocalizedMessage(String values, Locale locale, String value) {
        Preconditions.checkNotNullArgument(locale);
        return putLocalizedMessage(values, locale.toLanguageTag(), value);
    }

    @Override
    public String putLocalizedMessage(String values, String localeCode, String value) {
        return putValue(values, localeCode, MESSAGE_KEY, value);
    }
}