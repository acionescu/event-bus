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
package net.segoia.event.eventbus.test;

import org.junit.Test;

import junit.framework.Assert;
import junit.textui.TestRunner;
import net.segoia.event.conditions.AndCondition;
import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.conditions.NotCondition;
import net.segoia.event.conditions.OrCondition;
import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.peers.events.register.PeerRegisterRequestEvent;

public class ConditionTest {

    @Test
    public void testAggregated() {
	Condition defaultEventsCond = new AndCondition(
		new OrCondition(LooseEventMatchCondition.build("PEER", null),
			LooseEventMatchCondition.build("STATUS-APP", null)),
		new NotCondition(LooseEventMatchCondition.build("STATUS-APP", "REQUEST")));

	EventContext ec = new EventContext(new PeerRegisterRequestEvent("bla", new TrueCondition()));
	
	Assert.assertFalse(LooseEventMatchCondition.build("STATUS-APP", "REQUEST").test(ec));
	
	Assert.assertTrue(defaultEventsCond
		.test(ec));

    }

}
