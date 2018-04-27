package net.segoia.event.eventbus.peers.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

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

    
    public static void main(String[] args) {
	Charset c = Charset.forName("UTF-8");
	
	System.out.println(c);
    }
}
