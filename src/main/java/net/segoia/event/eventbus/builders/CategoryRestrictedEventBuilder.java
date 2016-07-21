package net.segoia.event.eventbus.builders;

public class CategoryRestrictedEventBuilder extends EventBuilder{
    
    public CategoryRestrictedEventBuilder(EventBuilderContext context, String category) {
	super(context);
	category(category);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.builders.EventBuilder#name(java.lang.String)
     */
    @Override
    public EventBuilder name(String name) {
	// TODO Auto-generated method stub
	return super.name(name);
    }

    
    
}
