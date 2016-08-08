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
import net.segoia.event.eventbus.peers.events.PeerRegisterRequestEvent;

public class ConditionTest {

    @Test
    public void testAggregated() {
	Condition defaultEventsCond = new AndCondition(
		new OrCondition(LooseEventMatchCondition.build("PEER", null),
			LooseEventMatchCondition.build("STATUS-APP", null)),
		new NotCondition(LooseEventMatchCondition.build("STATUS-APP", "REQUEST")));

	EventContext ec = new EventContext(new PeerRegisterRequestEvent("bla", new TrueCondition()), null);
	
	Assert.assertFalse(LooseEventMatchCondition.build("STATUS-APP", "REQUEST").test(ec));
	
	Assert.assertTrue(defaultEventsCond
		.test(ec));

    }

}
