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

package com.haulmont.cuba.gui.app.security.constraintloc.edit;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.security.entity.LocalizedConstraintMessage;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;
import java.util.Map;

public class ConstraintLocalizationEdit extends AbstractEditor<LocalizedConstraintMessage> {
    @Named("fieldGroup.operationType")
    protected LookupField operationTypeField;

    @Inject
    protected LookupField localesSelect;

    @Inject
    protected TextField caption;

    @Inject
    protected ResizableTextArea message;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    protected LocalizationValueChangeListener captionValueChangeListener;
    protected LocalizationValueChangeListener messageValueChangeListener;

    @Override
    protected void postInit() {
        operationTypeField.setTextInputAllowed(false);

        initCaptionField();
        initMessageField();
        initLocalesField();
    }

    protected void initLocalesField() {
        Map<String, Locale> locales = globalConfig.getAvailableLocales();
        localesSelect.setOptionsMap(locales);

        localesSelect.addValueChangeListener(createLocaleSelectValueChangeListener());

        localesSelect.setValue(userSessionSource.getLocale());
    }

    protected ValueChangeListener createLocaleSelectValueChangeListener() {
        return e -> {
            captionValueChangeListener.suspend();
            messageValueChangeListener.suspend();

            Locale selectedLocale = (Locale) e.getValue();
            caption.setValue(getItem().getLocalizedCaption(selectedLocale));
            message.setValue(getItem().getLocalizedMessage(selectedLocale));

            captionValueChangeListener.resume();
            messageValueChangeListener.resume();
        };
    }

    protected void initCaptionField() {
        captionValueChangeListener = createCaptionValueChangeListener();
        caption.addValueChangeListener(captionValueChangeListener);
    }

    protected LocalizationValueChangeListener createCaptionValueChangeListener() {
        return new LocalizationValueChangeListener() {
            @Override
            protected void updateValues(LocalizedConstraintMessage item, Locale selectedLocale, String value) {
                item.putLocalizedCaption(selectedLocale, value);
            }
        };
    }

    protected void initMessageField() {
        messageValueChangeListener = createMessageValueChangeListener();
        message.addValueChangeListener(messageValueChangeListener);
    }

    protected LocalizationValueChangeListener createMessageValueChangeListener() {
        return new LocalizationValueChangeListener() {
            @Override
            protected void updateValues(LocalizedConstraintMessage item, Locale selectedLocale, String value) {
                item.putLocalizedMessage(selectedLocale, value);
            }
        };
    }

    protected abstract class LocalizationValueChangeListener implements ValueChangeListener {
        protected boolean active = true;

        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (active) {
                Locale selectedLocale = localesSelect.getValue();
                updateValues(getItem(), selectedLocale, (String) e.getValue());
            }
        }

        public void suspend() {
            active = false;
        }

        public void resume() {
            active = true;
        }

        protected abstract void updateValues(LocalizedConstraintMessage item, Locale selectedLocale, String value);
    }
}
