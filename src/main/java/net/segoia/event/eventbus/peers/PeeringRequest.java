package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.eventbus.constants.Events;

public class PeeringRequest {
    private EventBusNode requestingNode;
    /**
     * On what kind of events this node wants to listed
     * </br>
     * If left null, it will attempt to listen on all events
     * </br>
     * it may be rejected if the source node does not allow this kind of peering
     * </br>
     * By default it's set to listen only on EBUS:PEER: events
     */
    private Condition eventsCondition=LooseEventMatchCondition.build(Events.SCOPE.EBUS,Events.CATEGORY.PEER);
    
    public PeeringRequest(EventBusNode requestingNode) {
	super();
	this.requestingNode = requestingNode;
    }
    public PeeringRequest(EventBusNode requestingNode, Condition eventsCondition) {
	super();
	this.requestingNode = requestingNode;
	this.eventsCondition = eventsCondition;
    }
    /**
     * @return the requestingNode
     */
    public EventBusNode getRequestingNode() {
        return requestingNode;
    }
    /**
     * @return the eventsCondition
     */
    public Condition getEventsCondition() {
        return eventsCondition;
    }
    /**
     * @param requestingNode the requestingNode to set
     */
    public void setRequestingNode(EventBusNode requestingNode) {
        this.requestingNode = requestingNode;
    }
    /**
     * @param eventsCondition the eventsCondition to set
     */
    public void setEventsCondition(Condition eventsCondition) {
        this.eventsCondition = eventsCondition;
    }
    
}
