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
package net.segoia.event.eventbus.annotations.utils;

import java.util.Arrays;
import java.util.List;

public class TypeRef {
    private String packageName;
    private String typeName;
    /**
     * Generics for this type
     */
    private List<TypeRef> generics;

    public TypeRef(String packageName, String typeName, TypeRef... gen) {
	super();
	this.packageName = packageName;
	this.typeName = typeName;
	this.generics = Arrays.asList(gen);
    }

    public TypeRef(Class clazz, TypeRef... gen) {
	this.packageName = clazz.getPackage().getName();
	this.typeName = clazz.getSimpleName();
	this.generics = Arrays.asList(gen);
    }

    public TypeRef(Class clazz) {
	if (!clazz.isPrimitive()) {
	    this.packageName = clazz.getPackage().getName();
	}
	this.typeName = clazz.getSimpleName();
    }

    public TypeRef() {
	super();
	// TODO Auto-generated constructor stub
    }

    public TypeRef(String packageName, String typeName) {
	super();
	this.packageName = packageName;
	this.typeName = typeName;
    }

    public String getFullName() {
	return packageName + CodeGenUtils.PACKAGE_SEP + typeName;
    }

    public String getPackageName() {
	return packageName;
    }

    public void setPackageName(String packageName) {
	this.packageName = packageName;
    }

    public String getTypeName() {
	return typeName;
    }

    public void setTypeName(String typeName) {
	this.typeName = typeName;
    }

    public List<TypeRef> getGenerics() {
	return generics;
    }

    public void setGenerics(List<TypeRef> generics) {
	this.generics = generics;
    }

}
