package net.segoia.event.eventbus.annotations.utils;

public class ParameterInfo extends CodeElement<ParameterInfo> {
    private TypeRef typeRef;
    private String name;
    private Object value;
    
    public ParameterInfo() {
	super();
	// TODO Auto-generated constructor stub
    }

    public ParameterInfo(TypeRef typeRef, String name) {
	super();
	this.typeRef = typeRef;
	this.name = name;
    }
    
    

    public ParameterInfo(String name, Object value) {
	super();
	this.name = name;
	this.value = value;
    }

    public TypeRef getTypeRef() {
	return typeRef;
    }

    public void setTypeRef(TypeRef typeRef) {
	this.typeRef = typeRef;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return CodeGenUtils.genParam(this);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
