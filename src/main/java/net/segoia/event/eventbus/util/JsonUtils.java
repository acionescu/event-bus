package net.segoia.event.eventbus.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class JsonUtils {
    public static Gson gs = new Gson();

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
	if (json == null) {
	    try {
		return classOfT.newInstance();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	try {
	    return gs.fromJson(json, classOfT);
	} catch (Exception e) {
	    System.out.println("json: '" + json + "'");
	    throw e;
	}
    }

    public static String toJson(Object obj) {
	return gs.toJson(obj);
    }
    
    public static Gson gs(){
	return gs;
    }
    
    public static String getString(JsonObject jo, String prop){
	JsonElement jsonElement = jo.get(prop);
	if(jsonElement == null){
	    return null;
	}
	return jsonElement.getAsString();
    }
   
    
}
