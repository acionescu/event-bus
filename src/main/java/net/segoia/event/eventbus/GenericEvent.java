package net.segoia.event.eventbus;

public class GenericEvent extends Event{

    public GenericEvent() {
	super();
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(Class<?> clazz) {
	super(clazz);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name, Event cause, String topic) {
	super(scope, category, name, cause, topic);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name, Event cause) {
	super(scope, category, name, cause);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name, String topic) {
	super(scope, category, name, topic);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name) {
	super(scope, category, name);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String et, String topic) {
	super(et, topic);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String et) {
	super(et);
	// TODO Auto-generated constructor stub
    }

}
