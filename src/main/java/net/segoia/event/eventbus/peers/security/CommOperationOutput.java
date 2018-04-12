package net.segoia.event.eventbus.peers.security;

import java.io.UnsupportedEncodingException;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.util.JsonUtils;

public class CommOperationOutput extends OperationOutput {

    public CommOperationOutput(byte[] data) {
	super(data);
    }

    @Override
    public byte[] getFullData() {

	try {
	    return JsonUtils.toJson(this).getBytes("UTF-8");
	} catch (UnsupportedEncodingException e) {
	   
	    e.printStackTrace();
	}
	return null;
    }

}
