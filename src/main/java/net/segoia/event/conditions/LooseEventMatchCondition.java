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

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public class LooseEventMatchCondition extends Condition{
    
    public LooseEventMatchCondition(String id) {
	super(id);
    }
    
    public LooseEventMatchCondition(String id, boolean extract) {
	super(id);
	if(extract) {
	    String scope = null;
		String category = null;
		String name = null;
		
		String[] etArray = id.split(":");
		int alen = etArray.length;

		if (alen > 0) {
		    scope = (etArray[0].isEmpty()) ? null : etArray[0];
		}
		if (alen > 1) {
		    category = (etArray[1].isEmpty()) ? null : etArray[1];
		}
		if (alen > 2) {
		    name = (etArray[2].isEmpty()) ? null : etArray[2];
		}

		
		    setScope(scope);
		    setCategory(category);
		    setName(name);

	}
    }



    private String scope;
    private String category;
    private String name;
    
    
    @Override
    public boolean test(EventContext input) {
	Event e = input.getEvent();
	if(scope != null && !scope.equals(e.getScope())) {
	    return false;
	}
	if(category !=null && !category.equals(e.getCategory())) {
	    return false;
	}
	if(name != null && !name.equals(e.getName())) {
	    return false;
	}
	
	return true;
    }


    /**
     * @return the scope
     */
    public String getScope() {
        return scope;
    }


    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param scope the scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
    }


    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    

}
