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

import java.util.List;

import com.google.gson.JsonObject;

import net.segoia.event.eventbus.EventListener;

public class EventListenerJsonConfig{
    
    private EventListener instance;
    private List<String> conditions;
    private int priority;
    
    
    /**
     * @return the conditions
     */
    public List<String> getConditions() {
        return conditions;
    }
    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * @param conditions the conditions to set
     */
    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    /**
     * @return the instance
     */
    public EventListener getInstance() {
        return instance;
    }
    /**
     * @param instance the instance to set
     */
    public void setInstance(EventListener instance) {
        this.instance = instance;
    }
    
    
}
