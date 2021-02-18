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

public class FieldInfo extends CodeElement<FieldInfo> {
    private int modifiers;
    private TypeRef type;
    private String name;
    private Object value;

    public FieldInfo(int modifiers, TypeRef type, String name) {
	super();
	this.modifiers = modifiers;
	this.type = type;
	this.name = name;
    }

    public FieldInfo(int modifiers, TypeRef type, String name, Object value) {
	super();
	this.modifiers = modifiers;
	this.type = type;
	this.name = name;
	this.value = value;
    }

    public FieldInfo() {
	super();
	// TODO Auto-generated constructor stub
    }

    public int getModifiers() {
	return modifiers;
    }

    public void setModifiers(int modifiers) {
	this.modifiers = modifiers;
    }

    public TypeRef getType() {
	return type;
    }

    public void setType(TypeRef type) {
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
	this.value = value;
    }

}
