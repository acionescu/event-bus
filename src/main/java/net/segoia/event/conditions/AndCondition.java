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

import java.util.Arrays;

import net.segoia.event.eventbus.EventContext;

public class AndCondition extends AggregatedCondition {

    public AndCondition(String id, Condition... subconditions) {
	super(id, subconditions);
    }
    
    public AndCondition(Condition... subconditions) {

	this(buildId(subconditions), subconditions);
    }

    private static String buildId(Condition... subconditions) {
	StringBuffer out = new StringBuffer();
	out.append(subconditions[0].getId());
	Arrays.stream(subconditions).skip(1).map(c -> "&" + c.getId()).forEach(out::append);
	return out.toString();
    }


    @Override
    public boolean test(EventContext eventContext) {
	for (Condition f : subconditions) {
	    if (!f.test(eventContext)) {
		return false;
	    }
	}

	return true;
    }

}
