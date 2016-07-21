package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.Event;

public class ScopeRestrictedEventBuilder extends EventBuilder{
    
    public ScopeRestrictedEventBuilder(EventBuilderContext context, String scope) {
	super(context);
	scope(scope);
    }
    
        

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.builders.EventBuilder#category(java.lang.String)
     */
    @Override
    public CategoryRestrictedEventBuilder category(String category) {
	return new CategoryRestrictedEventBuilder(context, category);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.builders.EventBuilder#name(java.lang.String)
     */
    @Override
    public EventBuilder name(String name) {
	// TODO Auto-generated method stub
	return super.name(name);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.builders.EventBuilder#build()
     */
    @Override
    public Event build() {
	// TODO Auto-generated method stub
	return super.build();
    }


}
