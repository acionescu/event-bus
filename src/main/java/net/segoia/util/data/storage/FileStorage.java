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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Default file storage
 * 
 * @author adi
 *
 */
public class FileStorage extends AbstractStorage {
    /**
     * Root directory
     */
    private File root;

    /**
     * Characters not allowed in a path ( these characters can't appear before the last path separator )
     */
    private static final char[] forbiddenPathCharacters = new char[] { '.' };
    
    private static final char[] forbiddenFileNameChars;
    
    static {
	if(File.separatorChar != PATH_SEPARATOR) {
	    forbiddenFileNameChars= new char[] {File.separatorChar, PATH_SEPARATOR};
	}
	else {
	    forbiddenFileNameChars= new char[] {PATH_SEPARATOR};
	}
    }

    public FileStorage(File root) {
	super();
	this.root = root;

	initRoot();
    }

    public FileStorage(FileStorage parent, File root) {
	super(parent);
	this.root = root;
	initRoot();
    }

    public FileStorage(FileStorage parent, String root) throws StorageException {
	this(parent, parent.getFileForKey(root));
    }

    public FileStorage(String path) {
	this(new File(path));
    }

    public FileStorage() {
	super();
    }

    protected void initRoot() {
	if (!root.exists()) {
	    /* create this if doesn't exist */
	    root.mkdirs();
	} else if (!root.isDirectory()) {
	    throw new IllegalArgumentException("Expecting a direcatory, but it wasn't - " + root);
	}
    }

    protected String replaceAll(String input, char target, char with) {
	if (input == null) {
	    return null;
	}
	String ouput = input;
	while (ouput.indexOf(target) >= 0) {
	    ouput = ouput.replace(target, with);
	}
	return ouput;
    }

    protected boolean containsPathChars(String key, char pathSep, char[] searchChars) {
	/* first, get the last position of a path separator */
	int lastPathSepIndex = key.lastIndexOf(pathSep);

	if (lastPathSepIndex <= 0) {
	    /* not the case */
	    return false;
	}

	for (char c : searchChars) {
	    int ci = key.indexOf(c);
	    if (ci > 0 && ci < lastPathSepIndex) {
		return true;
	    }
	}
	return false;
    }

    protected boolean containsChars(String key, char[] searchChars) {
	for (char c : searchChars) {
	    if (key.indexOf(c) >= 0) {
		return true;
	    }
	}
	return false;
    }

    protected File getFileForKey(String key) throws StorageException {
	if (containsPathChars(key, Storage.PATH_SEPARATOR, forbiddenPathCharacters)) {
	    throw new StorageException("Key contains forbiddend characters");
	}

	if (Storage.PATH_SEPARATOR != (File.separatorChar)) {
	    /*
	     * if the system file separator doesn't match our path separator, then replace it with the default logic
	     * separator
	     */
	    key = replaceAll(key, File.separatorChar, Storage.LOGIC_SEPARATOR);

	    /* now replace the path separator with the actual file system separator */
	    key = replaceAll(key, Storage.PATH_SEPARATOR, File.separatorChar);
	}

	return new File(root, key);
    }
    
    private boolean isValidPath(String path) {
	return containsPathChars(path, Storage.PATH_SEPARATOR, forbiddenPathCharacters);
    }

    private boolean isValidFileName(String name) {
	return !containsChars(name, forbiddenFileNameChars);
    }
    
    @Override
    public OutputStream create(String key) throws StorageEntityExistsException, StorageException {
	File candidate = getFileForKey(key);
	boolean created;
	try {
	    File parent = candidate.getParentFile();
	    if (parent != null && !parent.exists()) {
		/* create path for this file if doesn't exist */
		parent.mkdirs();
	    }
	    created = candidate.createNewFile();
	} catch (IOException e) {
	    throw new StorageException("Failed creating file " + candidate, e);
	}
	if (!created) {
	    throw new StorageEntityExistsException("Can't create file " + candidate + ". Already exists.");
	}

	try {
	    return new FileOutputStream(candidate);
	} catch (FileNotFoundException e) {
	    throw new StorageException("Failed opening file " + candidate, e);
	}
    }

    @Override
    public OutputStream openForUpdate(String key) throws StorageException {
	File candidate = getFileForKey(key);
	try {
	    return new FileOutputStream(candidate);
	} catch (FileNotFoundException e) {
	    throw new StorageException("Failed opening file " + candidate, e);
	}
    }

    @Override
    public boolean delete(String key) {
	File candidate;
	try {
	    candidate = getFileForKey(key);
	    return recursiveDelete(candidate);
	} catch (StorageException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return false;
    }

    protected boolean recursiveDelete(File f) {
	if (f.isDirectory()) {
	    for (File cf : f.listFiles()) {
		boolean deleted = recursiveDelete(cf);
		if (!deleted) {
		    return false;
		}
	    }
	}
	return f.delete();
    }

    @Override
    public boolean exists(String key) {
	try {
	    return getFileForKey(key).exists();
	} catch (StorageException e) {
	    return false;
	}
    }

    @Override
    public InputStream openForRead(String key) throws StorageException {

	try {
	    File candidate = getFileForKey(key);
	    return new FileInputStream(candidate);
	} catch (FileNotFoundException | StorageException e) {
	    throw new StorageException(root + " Can't open file for read " + key, e);
	}
    }

    public File getRoot() {
	return root;
    }

    public void setRoot(File root) {
	this.root = root;
    }

    @Override
    public String getId() {
	return root.getAbsolutePath();
    }

    @Override
    public String[] list() {
	return root.list();
    }

    @Override
    public Storage createStorage(String key) throws StorageException {
	return new FileStorage(this, key);
    }

    @Override
    public Storage createStorageHierarchy(String... keys) throws StorageException {
	Storage parent = this;
	for (String key : keys) {
	    parent = parent.createStorage(key);
	}
	return parent;
    }

    @Override
    public boolean isStorage(String key) {
	File candidate;
	try {
	    candidate = getFileForKey(key);
	    return candidate.isDirectory();
	} catch (StorageException e) {
	    return false;
	}
    }

    @Override
    public StorageEntityInfo[] listEntities() {
	File[] files = root.listFiles();

	StorageEntityInfo[] result = new StorageEntityInfo[files.length];
	int index = 0;
	for (File f : files) {
	    result[index] = getStorageInfo(f);
	    index++;
	}
	return result;
    }

    protected StorageEntityInfo getStorageInfo(File f) {
	return new StorageEntityInfo(f.getName(), f.isDirectory(), f.lastModified(), f.length());
    }

    @Override
    public String[] extractHierarchy(String key) {
	/* simply split by the path separator */
	return key.split(Storage.PATH_SEPARATOR_STRING);
    }

    @Override
    public OutputStream createLeaf(String key) throws StorageEntityExistsException, StorageException {
	File candidate = getFileForKey(key);
	boolean created;
	try {
	    created = candidate.createNewFile();
	} catch (IOException e) {
	    throw new StorageException("Failed creating file " + candidate, e);
	}
	if (!created) {
	    throw new StorageEntityExistsException("Can't create file " + candidate + ". Already exists.");
	}

	try {
	    return new FileOutputStream(candidate);
	} catch (FileNotFoundException e) {
	    throw new StorageException("Failed opening file " + candidate, e);
	}
    }

    @Override
    public Storage getStorage(String path) throws StorageException {
	File fileForPath = getFileForKey(path);
	if (!fileForPath.exists()) {
	    throw new StorageException(root.getAbsolutePath() + ": No storage for path " + path);
	}
	if (!fileForPath.isDirectory()) {
	    throw new StorageException(root.getAbsolutePath() + ": Storage at path " + path
		    + " is not a directory, but requested as one.");
	}
	return new FileStorage(fileForPath);
    }

    @Override
    public StorageEntity getStorageEntity(String key) throws StorageException {
	File fileForPath = getFileForKey(key);
	if (!fileForPath.exists()) {
	    throw new StorageException(root.getAbsolutePath() + ": No storage for path " + key);
	}

	return new StorageEntity(this, key, getStorageInfo(fileForPath));
    }

    @Override
    public String getRelativePath(String key) throws StorageException {
	File file = getFileForKey(key);
	try {
	    String filePath = file.getCanonicalPath();
	    String rootPath = root.getCanonicalPath();
	    
	    /* test that the key is actually under the root file - because we're paranoid */
	    if (!filePath.startsWith(rootPath)) {
		throw new StorageException("Key " + key + " is not relative to " + rootPath);
	    }
	    /* return relative path */
	    String relPath = filePath.substring(rootPath.length());
	    if(relPath.isEmpty()) {
		return PATH_SEPARATOR_STRING;
	    }
	    return relPath;

	} catch (Exception e) {
	    throw new StorageException(this.root + " Failed getting path for key " + key, e);
	}

    }

    @Override
    public boolean move(String sourceKey, String destStorageKey) throws StorageException {
	File sourceFile = getFileForKey(sourceKey);
	File destFile = getFileForKey(destStorageKey);
	if (!destFile.isDirectory()) {
	    throw new StorageException("Destination path is not a directory");
	}

	if (!destFile.exists()) {
	    throw new StorageException("Destination path doesn't exist");
	}

	Path sourcePath = sourceFile.toPath();
	Path destPath = new File(destFile,sourceFile.getName()).toPath();

	try {
	    if (sourceFile.isFile()) {
		/* move a regular file */

		Files.move(sourcePath, destPath);

	    } else if (sourceFile.isDirectory()) {
		moveDirectory(sourcePath, destPath);

	    }
	    return true;
	} catch (IOException e) {
	    throw new StorageException("Move failed", e);
	}
    }

    protected boolean moveDirectory(Path source, Path dest) throws IOException {
//	if(dest.equals(source.getParent())) {
	/* if this is simply a rename, then use simple move */
	Files.move(source, dest);
	return true;
//	}
//	/* otherwise, we have to move files first */

    }

    @Override
    public StorageEntityInfo[] listEntities(StorageFilter filter) {
	if(filter == null) {
	    return listEntities();
	}
	
	FileFilter fileFilter = (f)->{
	    StorageEntityInfo storageInfo = getStorageInfo(f);
	    return filter.test(storageInfo);
	};
	
	File[] files = root.listFiles(fileFilter);
	
	StorageEntityInfo[] result = new StorageEntityInfo[files.length];
	int index = 0;
	for (File f : files) {
	    result[index] = getStorageInfo(f);
	    index++;
	}
	return result;
    }

    @Override
    public boolean rename(String key, String newName) throws StorageException {
	File fileForKey = getFileForKey(key);
	
	try {
	    if(fileForKey.getCanonicalPath().equals(root.getCanonicalPath())) {
	        throw new StorageException("Can't move root directory");
	    }
	} catch (IOException e) {
	    throw new StorageException("Rename failed",e);
	}
	
	if(!isValidFileName(newName)) {
	    throw new StorageException("Invalid filename "+newName);
	}
	
	File destFile = new File(fileForKey.getParent(), newName);
	
	if(destFile.exists()) {
	    throw new StorageException("Destination file already exists");
	}
	
	try {
	    Files.move(fileForKey.toPath(), destFile.toPath());
	} catch (IOException e) {
	    throw new StorageException("Rename failed",e);
	}
	
	return true;
    }
}
