package net.segoia.event.eventbus.builders;

public class CustomEventBuilder extends EventBuilder {

    public CustomEventBuilder() {
	super(new EventBuilderContext());
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.builders.EventBuilder#scope(java.lang.String)
     */
    @Override
    public ScopeRestrictedEventBuilder scope(String scope) {
	return new ScopeRestrictedEventBuilder(context, scope);
    }

    

}
