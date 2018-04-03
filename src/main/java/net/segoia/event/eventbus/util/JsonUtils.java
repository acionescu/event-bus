/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentityType;
import net.segoia.event.eventbus.peers.events.auth.id.PlainNodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.PlainNodeIdentityType;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentityType;

public class JsonUtils {
    public static Gson gs;
    
    static {
	GsonBuilder gsonBuilder = new GsonBuilder();
	
	gsonBuilder.registerTypeAdapter(NodeIdentityType.class, new JsonDeserializer<NodeIdentityType>() {

	    @Override
	    public NodeIdentityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		JsonElement typeElem = jo.get("type");
		if (typeElem == null) {
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}

		String type = typeElem.getAsString();

		switch (type) {
		case PlainNodeIdentityType.TYPE:
		    return context.deserialize(json, PlainNodeIdentityType.class);
		case SpkiNodeIdentityType.TYPE:
		    return context.deserialize(json, SpkiNodeIdentityType.class);
		default:
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}
	    }
	});
	
	gsonBuilder.registerTypeAdapter(NodeIdentity.class, new JsonDeserializer<NodeIdentity>() {

	    @Override
	    public NodeIdentity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		JsonElement typeElem = jo.get("type");
		if (typeElem == null) {
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}
		
		typeElem = typeElem.getAsJsonObject().get("type");
		
		if (typeElem == null) {
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}

		String type = typeElem.getAsString();

		switch (type) {
		case PlainNodeIdentityType.TYPE:
		    return context.deserialize(json, PlainNodeIdentity.class);
		case SpkiNodeIdentityType.TYPE:
		    return context.deserialize(json, SpkiNodeIdentity.class);
		default:
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}
	    }
	});

//	gsonBuilder.registerTypeAdapter(NodeIdentityType.class, new JsonSerializer<NodeIdentityType>() {
//
//	    @Override
//	    public JsonElement serialize(DataSource src, Type typeOfSrc, JsonSerializationContext context) {
//		String type = src.getType();
//
//		switch (type) {
//		case DataSource.TYPES.MAP:
//		    return context.serialize(src, MapDataSource.class);
//		case DataSource.TYPES.SERVER:
//		    return context.serialize(src, ServerDataSource.class);
//		default:
//		    throw new JsonParseException("Unknown type for DataSource object " + type);
//		}
//	    }
//	});
	
	gs = gsonBuilder.create();
	
    }
    

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
   
    public static <T> T copyObject(T obj) {
	Class<T> clazz = (Class<T>)obj.getClass();
	return fromJson(toJson(obj), clazz);
    }
}
