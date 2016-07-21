package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.Event;

public abstract class EventBuilder {

    protected EventBuilderContext context;

    public EventBuilder(EventBuilderContext context) {
	super();
	this.context = context;
    }

    protected EventBuilder scope(String scope) {
	context.setScope(scope);
	return this;
    }

    protected EventBuilder category(String category) {
	context.setCategory(category);
	return this;
    }

    protected EventBuilder name(String name) {
	context.setName(name);
	return this;
    }

    public EventBuilder topic(String topic) {
	context.setTopic(topic);
	return this;
    }

    public Event build() {

	Event causeEvent = context.getCauseEvent();
	String topic = context.getTopic();

	if (causeEvent != null) {
	    return build(context.getScope(), context.getCategory(), context.getName(), causeEvent, topic);
	}
	return build(context.getScope(), context.getCategory(), context.getName(), topic);
    }

    private Event build(String scope, String category, String name) {

	return build(scope, category, name, null, null);
    }

    private Event build(String scope, String category, String name, Event cause) {
	return build(scope, category, name, cause, null);
    }

    private Event build(String scope, String category, String name, String topic) {
	return build(scope, category, name, null, topic);
    }

    private Event build(String scope, String category, String name, Event cause, String topic) {
	return new Event(scope, category, name, cause, topic);
    }
}
