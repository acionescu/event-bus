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
package net.segoia.util.data.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.test.CustomTestEvent;
import net.segoia.event.eventbus.util.EBus;

public class DocumentStoreTest {

    @Test
    public void testDocumentStore() {
	/* create a simple key sequence */
	SequenceKeyGenerator<Event> keyGenerator = new SequenceKeyGenerator<>();

	/* create an event json translator */

	DocumentTranslator<Event> eventJsonTranslator = new EventJsonTranslator();

	/* create a data store */

	DefaultDataStore eventDataStore = new DefaultDataStore(
		new FileStorage("src/test/resources/storage_test/event_store/"));

	/* create an event document store */

	DocumentStore<Event> eventStore = new DefaultDocumentStore<>(eventDataStore, eventJsonTranslator, keyGenerator,
		Event.class);

	/* initialize the event node */
	EBus.getMainNode();

	/* create 10 events */

	Map<String,Event> created = new HashMap<>();
	
	for (int i = 0; i < 10; i++) {
	    /* create event i */
	    CustomTestEvent eventi = new CustomTestEvent("event" + i);

	    /* store it */

	    try {
		String eventKey = eventStore.create(eventi);
		created.put(eventKey, eventi);
		/* try to read event immediately */
		Event recoveredEvent = eventStore.get(eventKey);
		
		/* test that the two events match */
		assertEquals(eventi, recoveredEvent);
		
	    } catch (StorageEntityExistsException | StorageException e) {
		fail("Failed to store/read event " + i);
	    }

	}
	
	/* list all keys */
	
	String[] allKeys = eventStore.listKeys();
	
	/* test they match with what we've created - order is irrelevant */
	assertEquals(created.keySet(), new HashSet<>(Arrays.asList(allKeys)));
	
	/* delete all */
	
	int deletedCount = eventStore.delete(allKeys);
	
	/* test that they were deleted */
	assertTrue(deletedCount == 10);
	
	assertTrue(eventStore.list().size()==0);

    }

}
