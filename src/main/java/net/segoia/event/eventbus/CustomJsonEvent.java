package net.segoia.event.eventbus;

import com.google.gson.JsonObject;

public class CustomJsonEvent extends CustomEvent<JsonObject>{

    public CustomJsonEvent(String et, JsonObject data) {
	super(et, data);
    }

}
