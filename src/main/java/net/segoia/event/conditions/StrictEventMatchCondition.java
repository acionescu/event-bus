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
package net.segoia.event.conditions;

import java.util.Map;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;


public class StrictEventMatchCondition extends LooseEventMatchCondition {
    /**
     * The event needs to have this type to be true
     */
    private String et;
    /**
     * The parameters need to have these values
     */
    private Map<String, Object> params;

    public StrictEventMatchCondition(String id) {
	this(id,id);
    }
    
    

    public StrictEventMatchCondition(String id, String et) {
	super(id);
	this.et = et;
    }


    @Override
    public boolean test(EventContext input) {
	Event event = input.getEvent();
	if (!event.getEt().equals(et)) {
	    return false;
	}
	if (params != null) {
	    for (String pn : params.keySet()) {
		if (!params.get(pn).equals(event.getParam(pn))) {
		    return false;
		}
	    }
	}

	return true;
    }

    public String getEt() {
	return et;
    }

    public void setEt(String et) {
	this.et = et;
    }

    public Map<String, Object> getParams() {
	return params;
    }

    public void setParams(Map<String, Object> params) {
	this.params = params;
    }

    @Override
    public String toString() {
	return "StrictEventMatchCondition [getId()=" + getId() + ", et=" + et + ", params=" + params + "]";
    }

}
