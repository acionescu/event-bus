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

public class AnnotationInfo extends CodeElement<AnnotationInfo> {
    private TypeRef type;
    private Object value;
    private List<ParameterInfo> parameters;

    public AnnotationInfo(TypeRef type, Object value) {
	super();
	this.type = type;
	this.value = value;
    }

    public AnnotationInfo(TypeRef type, Object value, List<ParameterInfo> parameters) {
	super();
	this.type = type;
	this.value = value;
	this.parameters = parameters;
    }

    public AnnotationInfo(TypeRef type, Object value, ParameterInfo... params) {
	super();
	this.type = type;
	this.value = value;
	this.parameters = Arrays.asList(params);
    }

    public TypeRef getType() {
        return type;
    }

    public void setType(TypeRef type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterInfo> parameters) {
        this.parameters = parameters;
    }
    
    
}
