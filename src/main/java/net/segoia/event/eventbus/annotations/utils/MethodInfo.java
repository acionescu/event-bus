package net.segoia.event.eventbus.annotations.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodInfo extends CodeElement<MethodInfo> {
    private int modifiers;
    private TypeRef returnType;
    private String name;

    /**
     * The parameters for this method
     */
    private List<ParameterInfo> parameters = new ArrayList<>();

    /**
     * The operation blocks in the execution order
     */
    private List<OpBlockInfo> opBlocks = new ArrayList<>();

    public MethodInfo(int modifiers, TypeRef returnType, String name, ParameterInfo... params) {
	super();
	this.modifiers = modifiers;
	this.returnType = returnType;
	this.name = name;
	this.parameters = Arrays.asList(params);
    }

    public MethodInfo(int modifiers, TypeRef returnType, String name) {
	super();
	this.modifiers = modifiers;
	this.returnType = returnType;
	this.name = name;
    }

    public MethodInfo(int modifiers, TypeRef returnType, String name, List<ParameterInfo> parameters) {
	super();
	this.modifiers = modifiers;
	this.returnType = returnType;
	this.name = name;
	this.parameters = parameters;
    }

    public MethodInfo(int modifiers, String name, List<ParameterInfo> parameters) {
	super();
	this.modifiers = modifiers;
	this.name = name;
	this.parameters = parameters;
    }
    
    public MethodInfo(int modifiers, String name, ParameterInfo... params) {
	super();
	this.modifiers = modifiers;
	this.name = name;
	this.parameters = Arrays.asList(params);
    }

    /**
     * Use this to build a constructor
     * 
     * @param modifiers
     * @param params
     */
    public MethodInfo(int modifiers, ParameterInfo... params) {
	super();
	this.modifiers = modifiers;
	this.parameters = Arrays.asList(params);
    }

    public MethodInfo addOpBlock(OpBlockInfo opBlock) {
	opBlocks.add(opBlock);
	return this;
    }

    public String getSignature() {
	return (name != null) ? name
		: "" + CodeGenUtils.START_METHOD_PARAMS + parameters + CodeGenUtils.END_METHOD_PARAMS;
    }

    /**
     * Adds a generic code block
     * 
     * @param codeBlock
     * @return
     */
    public MethodInfo addOpBlock(String codeBlock) {
	return addOpBlock(new GenericCodeBlock(codeBlock));
    }

    public List<OpBlockInfo> getOpBlocks() {
	return opBlocks;
    }

    public void setOpBlocks(List<OpBlockInfo> opBlocks) {
	this.opBlocks = opBlocks;
    }

    public int getModifiers() {
	return modifiers;
    }

    public void setModifiers(int modifiers) {
	this.modifiers = modifiers;
    }

    public TypeRef getReturnType() {
	return returnType;
    }

    public void setReturnType(TypeRef returnType) {
	this.returnType = returnType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<ParameterInfo> getParameters() {
	return parameters;
    }

    public void setParameters(List<ParameterInfo> parameters) {
	this.parameters = parameters;
    }

}
