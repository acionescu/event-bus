package net.segoia.event.eventbus.builders;

public class ClusterCategoryEventBuilder extends CategoryRestrictedEventBuilder{

    public ClusterCategoryEventBuilder(EventBuilderContext context) {
	super(context, "CLUSTER");
    }
    
    public EventBuilder newNode() {
	return super.name("NEW_NODE");
	
    }

}
