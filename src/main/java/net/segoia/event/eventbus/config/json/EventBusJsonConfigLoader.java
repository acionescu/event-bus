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
package net.segoia.event.eventbus.config.json;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.segoia.event.conditions.AndCondition;
import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.ConditionWrapper;
import net.segoia.event.conditions.FalseCondition;
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.conditions.NotCondition;
import net.segoia.event.conditions.OrCondition;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.conditions.TrueCondition;

public class EventBusJsonConfigLoader {
    private static Map<String, JsonDeserializer<?>> jsonDeserializers = new HashMap<String, JsonDeserializer<?>>();

    private static final Gson gson;

    static {
	/* defaul deserializer for strict conditions */
	jsonDeserializers.put(null, new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		String id = jo.get("id").getAsString();

		String et = jo.get("et").getAsString();

		Condition cond;

		String[] etArray = et.split(":");
		int alen = etArray.length;

		String scope = null;
		String category = null;
		String name = null;

		if (alen > 0) {
		    scope = (etArray[0].isEmpty()) ? null : etArray[0];
		}
		if (alen > 1) {
		    category = (etArray[1].isEmpty()) ? null : etArray[1];
		}
		if (alen > 2) {
		    name = (etArray[2].isEmpty()) ? null : etArray[2];
		}

		if (scope == null || category == null || name == null) {
		    LooseEventMatchCondition obj = new LooseEventMatchCondition(id);
		    obj.setScope(scope);
		    obj.setCategory(category);
		    obj.setName(name);
		    cond = obj;
		} else {

		    StrictEventMatchCondition obj = new StrictEventMatchCondition(id);
		    obj.setEt(et);
		    obj.setParams((Map) context.deserialize(jo.get("params"), LinkedHashMap.class));
		    cond = obj;
		}

		return cond;
	    }
	});

	/* adding or deserializer */
	jsonDeserializers.put("or", new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		OrCondition orCondition = new OrCondition(jo.get("id").getAsString(),
			(Condition[]) context.deserialize(jo.get("conditions"), Condition[].class));

		return orCondition;

	    }
	});

	/* adding and deserializer */
	jsonDeserializers.put("and", new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		AndCondition condition = new AndCondition(jo.get("id").getAsString(),
			(Condition[]) context.deserialize(jo.get("conditions"), Condition[].class));

		return condition;

	    }
	});

	/* adding not deserializer */
	jsonDeserializers.put("not", new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		NotCondition notCondition = new NotCondition(jo.get("id").getAsString(),
			(Condition[]) context.deserialize(jo.get("conditions"), Condition[].class));

		return notCondition;

	    }
	});

	/* condRef */

	jsonDeserializers.put("condRef", new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {

		JsonObject jo = json.getAsJsonObject();

		String refId = jo.get("id").getAsString();

		return new ConditionWrapper(refId + "-ref", refId);

	    }
	});
	
	/* true */
	jsonDeserializers.put("true", new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		return new TrueCondition();
	    }
	});
	
	/* false */
	jsonDeserializers.put("false", new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		return new FalseCondition();
	    }
	});

	GsonBuilder gb = new GsonBuilder();

	gb.registerTypeAdapter(Condition.class, new JsonDeserializer<Condition>() {

	    @Override
	    public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		    throws JsonParseException {
		JsonObject jo = json.getAsJsonObject();
		JsonElement conditionType = jo.get("ctype");
		String ctype = null;
		if (conditionType != null) {
		    ctype = conditionType.getAsString().trim();
		    if ("".equals(ctype)) {
			ctype = null;
		    }
		}
		JsonDeserializer<?> deserializerForType = jsonDeserializers.get(ctype);
		if (deserializerForType == null) {
		    throw new JsonParseException("No deserializer defined for condition type " + ctype);
		}
		return (Condition) deserializerForType.deserialize(json, typeOfT, context);
	    }
	});

	gb.registerTypeAdapterFactory(new TypeAdapterFactory() {

	    @Override
	    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
		final TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, type);

		TypeAdapter<T> typeAdapter = new TypeAdapter<T>() {

		    @Override
		    public void write(JsonWriter out, T value) throws IOException {
			delegateAdapter.write(out, value);
		    }

		    @Override
		    public T read(JsonReader in) throws IOException {
			JsonElement value = Streams.parse(in);

			if (value.isJsonNull()) {
			    return null;
			}

			if (!value.isJsonObject()) {
			    return delegateAdapter.fromJsonTree(value);
			}
			JsonObject jo = value.getAsJsonObject();

			JsonElement cnameElem = jo.remove("className");

			if (cnameElem != null) {
			    String cname = cnameElem.getAsString();

			    try {
				return (T) gson.fromJson(value, Class.forName(cname));
			    } catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			    }
			} else {
			    return delegateAdapter.fromJsonTree(value);
			}
		    }
		};

		return typeAdapter;

	    }
	});

	gson = gb.create();
    }

    public static EventBusJsonConfig loadEBusConfig(Reader reader) {

	EventBusJsonConfig ebusConfig = gson.fromJson(reader, EventBusJsonConfig.class);

	return ebusConfig;
    }

    public static <T> T load(Reader reader, Class<T> clazz) {
	return gson.fromJson(reader, clazz);
    }
}
