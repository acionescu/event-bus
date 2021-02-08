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
