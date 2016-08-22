/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus.config.json;

import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.EventBus;
import net.segoia.event.eventbus.EventBusConfig;

public class EventBusJsonConfig extends EventBusConfig{
    
    /**
     * What implementation of {@link EventBus} to use
     */
    private String busClassName;
    private boolean debugEnabled;
    private Map<String,Condition> conditions;
    private Map<String, EventListenerJsonConfig> listeners;
        
    
    /**
     * @return the busClassName
     */
    public String getBusClassName() {
        return busClassName;
    }
    /**
     * @param busClassName the busClassName to set
     */
    public void setBusClassName(String busClassName) {
        this.busClassName = busClassName;
    }
    /**
     * @return the conditions
     */
    public Map<String, Condition> getConditions() {
        return conditions;
    }
    /**
     * @return the listeners
     */
    public Map<String, EventListenerJsonConfig> getListeners() {
        return listeners;
    }
    /**
     * @param conditions the conditions to set
     */
    public void setConditions(Map<String, Condition> conditions) {
        this.conditions = conditions;
    }
    /**
     * @param listeners the listeners to set
     */
    public void setListeners(Map<String, EventListenerJsonConfig> listeners) {
        this.listeners = listeners;
    }
    /**
     * @return the debugEnabled
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }
    /**
     * @param debugEnabled the debugEnabled to set
     */
    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
        
}
