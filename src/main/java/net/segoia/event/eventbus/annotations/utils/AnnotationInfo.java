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
