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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInfo extends CodeElement<ClassInfo> {
    private String packageName;

    private List<TypeRef> dependencies = new ArrayList<>();

    private int modifiers;

    /* class, interface, enum or annotation */
    private String typeName;
    private String className;
    private TypeRef parent;
    private Set<TypeRef> interfaces;

    private Map<String, FieldInfo> fields = new LinkedHashMap<>();
    private Map<String, MethodInfo> constructors=new LinkedHashMap<>();
    
    private Map<String, MethodInfo> methods = new LinkedHashMap<>();

    public ClassInfo() {
	super();
	// TODO Auto-generated constructor stub
    }

    public ClassInfo(String packageName, String className, int modifiers, String typeName) {
	super();
	this.packageName = packageName;
	this.className = className;
	this.modifiers = modifiers;
	this.typeName = typeName;
    }

    public String build() {
	return CodeGenUtils.genClass(this);
    }

    public TypeRef asTypeRef() {
	return new TypeRef(packageName, className);
    }

    public ClassInfo addField(FieldInfo f) {
	fields.put(f.getName(), f);
	return this;
    }
    
    public ClassInfo addConstructor(MethodInfo c) {
	constructors.put(c.getSignature(), c);
	return this;
    }

    public ClassInfo addMethod(MethodInfo m) {
	methods.put(m.getSignature(), m);
	return this;
    }

    public ClassInfo addImport(TypeRef typeRef) {
	List<TypeRef> trGen = typeRef.getGenerics();
	/* if the type has generics, add those too as imports */
	if (trGen != null) {
	    for (TypeRef trg : trGen) {
		addImport(trg);
	    }
	}

	if (packageName.equals(typeRef.getPackageName())) {
	    /* return if they share the same package */
	    return this;
	}

	dependencies.add(typeRef);
	return this;
    }

    
    
    @Override
    public ClassInfo addAnnotation(AnnotationInfo ai) {
	addImport(ai.getType());
	return super.addAnnotation(ai);
    }

    public String getFullName() {
	return packageName + CodeGenUtils.PACKAGE_SEP + className;
    }

    public String getPackageName() {
	return packageName;
    }

    public void setPackageName(String packageName) {
	this.packageName = packageName;
    }

    public int getModifiers() {
	return modifiers;
    }

    public void setModifiers(int modifiers) {
	this.modifiers = modifiers;
    }

    public String getClassName() {
	return className;
    }

    public void setClassName(String className) {
	this.className = className;
    }

    public TypeRef getParent() {
	return parent;
    }

    public ClassInfo setParent(TypeRef parent) {
	this.parent = parent;
	/* add import as well */
	return addImport(parent);
    }

    public Set<TypeRef> getInterfaces() {
	return interfaces;
    }

    public void setInterfaces(Set<TypeRef> interfaces) {
	this.interfaces = interfaces;
    }

    public Map<String, FieldInfo> getFields() {
	return fields;
    }

    public void setFields(Map<String, FieldInfo> fields) {
	this.fields = fields;
    }

    public Map<String, MethodInfo> getMethods() {
	return methods;
    }

    public void setMethods(Map<String, MethodInfo> methods) {
	this.methods = methods;
    }

    public String getTypeName() {
	return typeName;
    }

    public void setTypeName(String typeName) {
	this.typeName = typeName;
    }

    public List<TypeRef> getDependencies() {
	return dependencies;
    }

    public void setDependencies(List<TypeRef> dependencies) {
	this.dependencies = dependencies;
    }

    public Map<String, MethodInfo> getConstructors() {
        return constructors;
    }

    public void setConstructors(Map<String, MethodInfo> constructors) {
        this.constructors = constructors;
    }

}
