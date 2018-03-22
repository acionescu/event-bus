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
