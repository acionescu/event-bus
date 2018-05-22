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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import net.segoia.event.eventbus.peers.vo.auth.id.IdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.PlainNodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.PlainNodeIdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.SharedIdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiFullIdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiFullNodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiNodeIdentityType;
import net.segoia.event.eventbus.peers.vo.comm.CommOperationDef;
import net.segoia.event.eventbus.peers.vo.comm.EncryptSymmetricOperationDef;
import net.segoia.event.eventbus.peers.vo.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.vo.comm.SignCommOperationDef;

public class JsonUtils {
    public static Gson gs;
    public static GsonBuilder gsonBuilder;

    static {
	gsonBuilder = new GsonBuilder();
	
	gsonBuilder.disableHtmlEscaping();

	gsonBuilder.registerTypeAdapter(IdentityType.class, new JsonDeserializer<IdentityType>() {

	    @Override
	    public IdentityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
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
		case SharedIdentityType.TYPE:
		    return context.deserialize(json, SharedIdentityType.class);
		case SpkiFullIdentityType.TYPE:
		    return context.deserialize(json, SpkiFullIdentityType.class);
		default:
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}
	    }
	});
	
	gsonBuilder.registerTypeAdapter(IdentityType.class, new JsonSerializer<IdentityType>() {

	    @Override
	    public JsonElement serialize(IdentityType src, Type typeOfSrc, JsonSerializationContext context) {
		switch(src.getType()) {
		case PlainNodeIdentityType.TYPE : return context.serialize(src, PlainNodeIdentityType.class);
		case SpkiNodeIdentityType.TYPE : return context.serialize(src, SpkiNodeIdentityType.class);
		case SharedIdentityType.TYPE: return context.serialize(src, SharedIdentityType.class);
		case SpkiFullIdentityType.TYPE: return context.serialize(SpkiFullIdentityType.class);
		default:
		    throw new JsonParseException("Type not supported for serialization "+src.getClass()+" "+src.getType());
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
		case SpkiFullIdentityType.TYPE:
		    return context.deserialize(json, SpkiFullNodeIdentity.class);
		default:
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}
	    }
	});
	
	gsonBuilder.registerTypeAdapter(NodeIdentity.class, new JsonSerializer<NodeIdentity>() {

	    @Override
	    public JsonElement serialize(NodeIdentity src, Type typeOfSrc, JsonSerializationContext context) {
		switch(src.getType().getType()) {
		case PlainNodeIdentityType.TYPE : return context.serialize(src, PlainNodeIdentity.class);
		case SpkiNodeIdentityType.TYPE : return context.serialize(src, SpkiNodeIdentity.class);
		case SpkiFullIdentityType.TYPE: return context.serialize(src, SpkiFullNodeIdentity.class);
		default:
		    throw new JsonParseException("Type not supported for serialization "+src.getClass()+" "+src.getType());
		}
	    }
	});

	gsonBuilder.registerTypeAdapter(CommOperationDef.class, new JsonDeserializer<CommOperationDef>() {

	    @Override
	    public CommOperationDef deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		JsonElement typeElem = jo.get("type");
		if (typeElem == null) {
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}

		String type = typeElem.getAsString();

		switch (type) {
		case SignCommOperationDef.TYPE:
		    return context.deserialize(json, SignCommOperationDef.class);
		case EncryptWithPublicCommOperationDef.TYPE:
		    return context.deserialize(json, EncryptWithPublicCommOperationDef.class);
		case EncryptSymmetricOperationDef.TYPE :
		    return context.deserialize(json, EncryptSymmetricOperationDef.class);
		default:
		    throw new JsonParseException("Unknown type for object " + json.toString());
		}
	    }
	});

	gsonBuilder.registerTypeAdapter(CommOperationDef.class, new JsonSerializer<CommOperationDef>() {

	    @Override
	    public JsonElement serialize(CommOperationDef src, Type typeOfSrc, JsonSerializationContext context) {
		switch(src.getType()) {
		case SignCommOperationDef.TYPE : return context.serialize(src, SignCommOperationDef.class);
		case EncryptWithPublicCommOperationDef.TYPE : return context.serialize(src, EncryptWithPublicCommOperationDef.class);
		case EncryptSymmetricOperationDef.TYPE : return context.serialize(src, EncryptSymmetricOperationDef.class);
		default:
		    throw new JsonParseException("Type not supported for serialization "+src.getClass()+" "+src.getType());
		}
	    }
	});

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
	    System.err.println("Failed processing json: "+json);
	    throw e;
	}
    }

    public static String toJson(Object obj) {
	return gs.toJson(obj);
    }

    public static Gson gs() {
	return gs;
    }

    public static String getString(JsonObject jo, String prop) {
	JsonElement jsonElement = jo.get(prop);
	if (jsonElement == null) {
	    return null;
	}
	return jsonElement.getAsString();
    }

    public static <T> T copyObject(T obj) {
	Class<T> clazz = (Class<T>) obj.getClass();
	return fromJson(toJson(obj), clazz);
    }
}
