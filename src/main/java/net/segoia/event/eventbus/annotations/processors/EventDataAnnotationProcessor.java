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
package net.segoia.event.eventbus.annotations.processors;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.annotations.EventDataType;
import net.segoia.event.eventbus.annotations.utils.AnnotationInfo;
import net.segoia.event.eventbus.annotations.utils.ClassInfo;
import net.segoia.event.eventbus.annotations.utils.CodeGenUtils;
import net.segoia.event.eventbus.annotations.utils.FieldInfo;
import net.segoia.event.eventbus.annotations.utils.MethodInfo;
import net.segoia.event.eventbus.annotations.utils.ParameterInfo;
import net.segoia.event.eventbus.annotations.utils.TypeRef;

@SupportedAnnotationTypes("net.segoia.event.eventbus.annotations.EventDataType")
public class EventDataAnnotationProcessor extends AbstractProcessor {

    private void error(String msg, Element e) {
	processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
	processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + annotations.size());

	if (annotations.size() == 0) {
	    return false;
	}

	Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(EventDataType.class);

	for (Element element : elements) {

	    if (element.getKind() != ElementKind.CLASS) {
		error("The annotation @EventDataType can only be applied on classes: ", element);
		continue;
	    }

	    EventDataType usedBy = element.getAnnotation(EventDataType.class);

	    processUsedBy(usedBy, element, roundEnv);

	}

	/* generate a version class */
	genVersion();

	return false;
    }

    private void genVersion() {
	ClassInfo classInfo = new ClassInfo("net.segoia.eventbus.generated.events", "GeneratedEventsVersion",
		Modifier.PUBLIC | Modifier.FINAL, CodeGenUtils.CLASS);

	FieldInfo generatedAtField = new FieldInfo(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL,
		new TypeRef(Long.TYPE), "generatedAt", System.currentTimeMillis());
	classInfo.addField(generatedAtField);

	writeFile(classInfo.getFullName(), classInfo.build());
    }

    private void writeFile(String fileName, String content) {
	Writer writer;
	try {
	    JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(fileName);
	    writer = sourceFile.openWriter();
	    writer.write(content);
	    writer.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    protected void processUsedBy(EventDataType usedBy, Element element, RoundEnvironment roundEnv) {

	EventType[] eventTypes = usedBy.value();

	if (eventTypes == null || eventTypes.length == 0) {
	    return;
	}

	processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + element);

	for (EventType eType : eventTypes) {
	    generateEvent(eType, usedBy, element, roundEnv);
	}

    }

    protected void generateEvent(EventType eventType, EventDataType usedBy, Element element, RoundEnvironment roundEnv) {
	String elementPackage = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();

	processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Eelement package " + elementPackage);

	String packageName = eventType.packageName();
	if (packageName == null || packageName.isEmpty()) {
	    /* use the package name of the annotated element if not specified */
	    packageName = elementPackage;
	}

	String className = eventType.className();
	if (className == null || className.isEmpty()) {
	    /* derive a class name from the event type */
	    String et = eventType.value();
	    String[] etArray = et.split(":");
	    for (int i = 0; i < etArray.length; i++) {
		etArray[i] = CodeGenUtils.capitalize(etArray[i]);
	    }

	    className = String.join("", etArray) + "Event";
	}

	/* create a new event class */
	ClassInfo eventClassInfo = new ClassInfo(packageName, className, Modifier.PUBLIC, CodeGenUtils.CLASS);

	/* we create a custom event with the specified type */

	TypeRef eventDataType = new TypeRef(elementPackage, element.getSimpleName().toString());

	TypeRef parentType = new TypeRef(CustomEvent.class, eventDataType);

	/* set the event class to extend this custom event type */
	eventClassInfo.setParent(parentType);
	
	/* add EventType annotation */
	
	eventClassInfo.addAnnotation(new AnnotationInfo(new TypeRef(EventType.class), eventType.value()));

	/* add ET constant */
	eventClassInfo.addField(new FieldInfo(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL,
		new TypeRef(String.class), "ET", eventType.value()));

	/* add constructor with data */
	eventClassInfo.addConstructor(new MethodInfo(Modifier.PUBLIC, new ParameterInfo(eventDataType, "data"))
		.addOpBlock("super(ET,data)"));
	
	/* add empty constructor */
	eventClassInfo.addConstructor(new MethodInfo(Modifier.PUBLIC)
		.addOpBlock("super(ET)"));
	
	/* add getter  */
	eventClassInfo.addMethod(new MethodInfo(Modifier.PUBLIC, eventDataType, "getData").addOpBlock("return super.getData()"));
	
	/* and setter */
	eventClassInfo.addMethod(new MethodInfo(Modifier.PUBLIC, "setData", new ParameterInfo(eventDataType, "data")).addOpBlock("super.setData(data)"));

	writeFile(eventClassInfo.getFullName(), eventClassInfo.build());

    }

}
