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
    public LocalizedConstraintMessage getLocalizedConstraintMessage(String entityName,
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
            // TODO: gg, is this needed?
            throw new IllegalStateException("Several entities with the same 'entity name/operation type' combination");
        }
    }

    @Nullable
    protected String getValue(LocalizedConstraintMessage localizedConstraintMessage, String localeCode, String key) {
        Preconditions.checkNotNullArgument(localizedConstraintMessage);
        Preconditions.checkNotNullArgument(localeCode);

        if (StringUtils.isEmpty(localizedConstraintMessage.getMessages())) {
            return null;
        }

        JSONObject localizationObject = new JSONObject(localizedConstraintMessage.getMessages());

        if (localizationObject.has(localeCode)) {
            JSONObject localeObject = localizationObject.getJSONObject(localeCode);
            return localeObject.has(key)
                    ? localeObject.getString(key)
                    : null;
        }

        return null;
    }

    protected String putValue(LocalizedConstraintMessage localizedConstraintMessage,
                              String localeCode, String key, String value) {
        Preconditions.checkNotNullArgument(localizedConstraintMessage);
        Preconditions.checkNotNullArgument(localeCode);


        JSONObject localizationObject = localizedConstraintMessage.getMessages() != null
                ? new JSONObject(localizedConstraintMessage.getMessages())
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
    public String getLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage, Locale locale) {
        Preconditions.checkNotNullArgument(locale);
        return getLocalizationCaption(localizedConstraintMessage, locale.toLanguageTag());
    }

    @Nullable
    @Override
    public String getLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage, String localeCode) {
        return getValue(localizedConstraintMessage, localeCode, CAPTION_KEY);
    }

    @Override
    public String putLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage,
                                         Locale locale, String value) {
        Preconditions.checkNotNullArgument(locale);
        return putLocalizationCaption(localizedConstraintMessage, locale.toLanguageTag(), value);
    }

    @Override
    public String putLocalizationCaption(LocalizedConstraintMessage localizedConstraintMessage,
                                         String localeCode, String value) {
        return putValue(localizedConstraintMessage, localeCode, CAPTION_KEY, value);
    }

    @Nullable
    @Override
    public String getLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage, Locale locale) {
        Preconditions.checkNotNullArgument(locale);
        return getLocalizationMessage(localizedConstraintMessage, locale.toLanguageTag());
    }

    @Nullable
    @Override
    public String getLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage, String localeCode) {
        return getValue(localizedConstraintMessage, localeCode, MESSAGE_KEY);
    }

    @Override
    public String putLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage,
                                         Locale locale, String value) {
        Preconditions.checkNotNullArgument(locale);
        return putLocalizationMessage(localizedConstraintMessage, locale.toLanguageTag(), value);
    }

    @Override
    public String putLocalizationMessage(LocalizedConstraintMessage localizedConstraintMessage,
                                         String localeCode, String value) {
        return putValue(localizedConstraintMessage, localeCode, MESSAGE_KEY, value);
    }
}