package net.segoia.event.conditions;

import java.util.Map;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;


public class StrictEventMatchCondition extends LooseEventMatchCondition {
    /**
     * The event needs to have this type to be true
     */
    private String et;
    /**
     * The parameters need to have these values
     */
    private Map<String, Object> params;

    public StrictEventMatchCondition(String id) {
	super(id);
    }

    @Override
    public boolean test(EventContext input) {
	Event event = input.getEvent();
	if (!event.getEt().equals(et)) {
	    return false;
	}
	if (params != null) {
	    for (String pn : params.keySet()) {
		if (!params.get(pn).equals(event.getParam(pn))) {
		    return false;
		}
	    }
	}

	return true;
    }

    public String getEt() {
	return et;
    }

    public void setEt(String et) {
	this.et = et;
    }

    public Map<String, Object> getParams() {
	return params;
    }

    public void setParams(Map<String, Object> params) {
	this.params = params;
    }

    @Override
    public String toString() {
	return "StrictEventMatchCondition [getId()=" + getId() + ", et=" + et + ", params=" + params + "]";
    }

}
