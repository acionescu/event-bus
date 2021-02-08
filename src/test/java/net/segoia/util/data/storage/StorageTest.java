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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.junit.Test;

public class StorageTest {

    @Test
    public void testDataStore() {
	
	/* create a file storage */
	FileStorage storage = new FileStorage("src/test/resources/storage_test/storage1/");
	
	/* create a data store on this storage*/
	DefaultDataStore dataStore = new DefaultDataStore(storage);
	
	/* define a text to store under a key */
	
	String testText1 = "This is some text\n with a new line.";
	
	/* create a buffer from the text */
	
	ByteArrayInputStream testText1Is = new ByteArrayInputStream(testText1.getBytes());
	
	String testText1Id = "testText1";
	
	/* test that the file doesn't exist */
	
	assertFalse(dataStore.exists(testText1Id));
	
	
	try {
	    
	    dataStore.create(testText1Id, testText1Is);
	} catch (StorageEntityExistsException | StorageException e) {
	    fail("Failed saving some testText1 - "+e.getMessage());
	}
	
	/* test that the file exists */
	assertTrue(dataStore.exists(testText1Id));
	
	/* now try to read it back */
	try {
	    byte[] recoveredTestText1Data = dataStore.get(testText1Id);
	    String recoveredTestText1 = new String(recoveredTestText1Data);
	    
	    /* test that it has the same contents that we saved */
	    
	    /* bytes */
	    assertTrue(Arrays.equals(testText1.getBytes(), recoveredTestText1Data));
	    
	    /* actual text */
	    assertEquals(testText1, recoveredTestText1);
	    
	} catch (DataTooLargeException | StorageMissingException | StorageException e) {
	    fail("Failed retrieving testText1 - "+e.getMessage());
	}
	
	/* now delte it */
	boolean deleted = dataStore.delete(testText1Id);
	
	assertTrue(deleted);
	
	/* test that the file was actually removed after deletion */
	
	assertFalse(dataStore.exists(testText1Id));
	
    }
    
    @Test
    public void testStorageWithPath() {
	/* create a file storage */
	FileStorage storage = new FileStorage("src/test/resources/storage_test/storage1/");
	
	/* create a data store on this storage*/
	DefaultDataStore dataStore = new DefaultDataStore(storage);
	
	String path = "a/key/with/hierarchy/this.is";
	try {
	    
	    String contents = "My file with hierarchy";
	    dataStore.create(path, new String(contents).getBytes());
	    
	    assertEquals(contents, new String(dataStore.get(path)));
	    
	    dataStore.delete(path);
	    
	    assertFalse(dataStore.exists(path));
	    
	} catch (StorageEntityExistsException | StorageException  | StorageMissingException | DataTooLargeException e ) {
	    e.printStackTrace();
	   fail("Failed creating file with path - "+e.getMessage());
	}
    }
    
    @Test
    public void testSanitize() {
	FileStorage storage = new FileStorage("src/test/resources/storage_test/storage1/");
	try {
	    Storage storage2 = storage.getStorage("/");
	} catch (StorageException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    @Test
    public void testGetRelativePath() {
	String path = "a/key/with/hierarchy/this.is";
	
	FileStorage storage = new FileStorage("src/test/resources/storage_test/storage1/");
	
	try {
	    System.out.println(storage.getRelativePath(path));
	} catch (StorageException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
}
