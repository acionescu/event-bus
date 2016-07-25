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

import net.segoia.event.eventbus.EventContext;

public class ConditionWrapper extends Condition{
    private String nestedConditionId;
    private Condition nestedCondition;
    

    public ConditionWrapper(String id, String nestedConditionId) {
	super(id);
	this.nestedConditionId = nestedConditionId;
    }

    @Override
    public boolean test(EventContext input) {
	if(nestedCondition == null){
	    loadNestedCondition(input);
	}
	return nestedCondition.test(input);
    }
    
    private void loadNestedCondition(EventContext input){
//	nestedCondition = input.getReportContext().getEngine().getConfig().getCondition(nestedConditionId);
	
	
	if(nestedCondition == null){
	    throw new RuntimeException("Condition with id "+nestedConditionId +" not found.");
	}
	
	throw new UnsupportedOperationException();
    }

}
