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

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.EventContextListener;
import net.segoia.event.eventbus.FilteringEventBus;

public class EventListenerJsonConfig{
    
    private EventContextListener instance;
    private Condition condition;
    /**
     * The priority of the condition filter among other event bus top level listeners
     * @see FilteringEventBus#registerListener(Condition, int, EventContextListener, int)
     */
    private int condPriority=-1;
    /**
     * The priority of this listener among other listeners registered for this condition, or among other top level 
     * listeners in case no condition was specified
     * @see FilteringEventBus#registerListener(Condition, int, EventContextListener, int)
     */
    private int priority=-1;
    
    
    
    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
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
    public EventContextListener getInstance() {
        return instance;
    }
    /**
     * @param instance the instance to set
     */
    public void setInstance(EventContextListener instance) {
        this.instance = instance;
    }


    /**
     * @return the condition
     */
    public Condition getCondition() {
        return condition;
    }


    /**
     * @param condition the condition to set
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }


    /**
     * @return the condPriority
     */
    public int getCondPriority() {
        return condPriority;
    }


    /**
     * @param condPriority the condPriority to set
     */
    public void setCondPriority(int condPriority) {
        this.condPriority = condPriority;
    }
    
    
}
