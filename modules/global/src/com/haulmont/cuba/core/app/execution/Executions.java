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

package com.haulmont.cuba.core.app.execution;


import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(Executions.NAME)
public class Executions {
    public static final String NAME = "cuba_Executions";

    protected static final String EXECUTIONS_ATTR = Executions.class.getName() + ".executions";

    @Inject
    protected TimeSource timeSource;
    @Inject
    protected UserSessionSource userSessionSource;


    public ExecutionContext start(String key, String group) {
        if (ExecutionContextHolder.getCurrentContext() != null) {
            throw new IllegalStateException("Execution context already started");
        }
        ExecutionContext context = new ExecutionContext(key, group, timeSource.currentTimestamp());


        UserSession userSession = userSessionSource.getUserSession();
        CopyOnWriteArrayList<ExecutionContext> executions = userSession.getLocalAttribute(EXECUTIONS_ATTR);
        if (executions == null) {
            userSession.setLocalAttributeIfAbsent(EXECUTIONS_ATTR, new CopyOnWriteArrayList<>());
            executions = userSession.getLocalAttribute(EXECUTIONS_ATTR);
        }
        executions.add(context);

        ExecutionContextHolder.setCurrentContext(context);

        return context;
    }

    public void end() {
        ExecutionContext context = ExecutionContextHolder.getCurrentContext();
        if (context == null) {
            throw new IllegalStateException("No execution context found");
        }

        UserSession userSession = userSessionSource.getUserSession();
        List<ExecutionContext> executions = userSession.getLocalAttribute(EXECUTIONS_ATTR);
        if (executions != null) {
            executions.remove(context);
        }

        ExecutionContextHolder.removeContext();
    }






}
