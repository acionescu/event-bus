package net.segoia.event.eventbus.annotations.utils;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class CodeGenUtils {
    public static final String PACKAGE = "package";
    public static final String IMPORT = "import";
    public static final String EXTENDS = "extends";
    public static final String IMPLEMENTS = "implements";
    public static final String VOID = "void";

    public static final String CLASS = "class";
    public static final String INTERFACE = "interface";
    public static final String ENUM = "enum";
    public static final String ANNOTATION = "@interface";
    public static final String ANNOTATION_REF = "@";

    public static final String PACKAGE_SEP = ".";
    public static final String LINE_SEP = System.getProperty("line.separator");
    public static final String WORD_SEP = " ";
    public static final String PARAMS_SEP = ",";
    public static final String OP_END = ";";
    public static final String INDENT = "\t";
    public static final char INDENT_CHAR='\t';

    public static final String VALUE_ATTRIBUTION = "=";
    public static final String ATTR_ACCESSOR = ".";
    public static final String START_CLASS = "{";
    public static final String END_CLASS = "}";
    public static final String START_METHOD = "{";
    public static final String END_METHOD = "}";
    public static final String START_METHOD_PARAMS = "(";
    public static final String END_METHOD_PARAMS = ")";
    public static final String START_GENERIC_TYPE = "<";
    public static final String END_GENERIC_TYPE = ">";

    public static final String formatValue(Object value) {
	if (value == null) {
	    return "null";
	}
	Class<? extends Object> vClass = value.getClass();

	if (vClass == Long.class) {
	    return value.toString() + "L";
	}
	else if(vClass == String.class) {
	    return "\""+value+"\"";
	}

	return value.toString();
    }

    public static final String genField(FieldInfo fi) {
	StringBuffer sb = new StringBuffer();

	String fieldName = fi.getName();
	TypeRef fieldType = fi.getType();
	String fieldModifiers = Modifier.toString(fi.getModifiers());
	sb.append(fieldModifiers).append(WORD_SEP).append(formatTypeRef(fieldType)).append(WORD_SEP).append(fieldName);

	Object fieldValue = fi.getValue();
	if (fieldValue != null) {
	    /* add default value for this field */
	    sb.append(VALUE_ATTRIBUTION).append(formatValue(fieldValue));
	}
	sb.append(OP_END);

	return sb.toString();
    }

    public static final String genParam(ParameterInfo pi) {
	StringBuffer sb = new StringBuffer();
	sb.append(formatTypeRef(pi.getTypeRef())).append(WORD_SEP).append(pi.getName());

	return sb.toString();
    }

    public static final String genParamAsKeyValue(ParameterInfo pi) {
	StringBuffer sb = new StringBuffer();
	sb.append(pi.getName()).append(VALUE_ATTRIBUTION).append(formatValue(pi.getValue()));

	return sb.toString();
    }

    /**
     * Capitalizes the given string
     * 
     * @param input
     * @return
     */
    public static final String capitalize(String input) {
	return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    
    public static final StringBuffer newLine(StringBuffer sb, String offset) {
	sb.append(LINE_SEP).append(offset);
	return sb;
    }
    
    public static final String genOffset(int count) {
	char[] buffer = new char[count];
	Arrays.fill(buffer, INDENT_CHAR);
	return String.join("", new String(buffer));
    }

    public static final String genMethod(MethodInfo mi, OpBlockContext opBlockContext) {
	StringBuffer sb = new StringBuffer();
	
	/* get indent */
	String methodOffset = genOffset(opBlockContext.getCodeContext().size());

	String methodModifiers = Modifier.toString(mi.getModifiers());
	TypeRef returnType = mi.getReturnType();
	String methodName = mi.getName();

	boolean isConstructor = false;
	if (methodName == null) {
	    /* this is a constructor method. Use class name as name */
	    methodName = opBlockContext.getOwnerClass().getClassName();
	    isConstructor = true;
	}

	sb.append(methodOffset);
	sb.append(methodModifiers);
	if (!isConstructor) {
	    if (returnType != null) {
		sb.append(WORD_SEP).append(formatTypeRef(returnType));
	    } else {
		sb.append(WORD_SEP).append(VOID);
	    }
	}

	sb.append(WORD_SEP).append(methodName);

	sb.append(START_METHOD_PARAMS);

	/* add parameters */
	int paramIndex = 0;
	for (ParameterInfo pi : mi.getParameters()) {
	    if (paramIndex++ > 0) {
		sb.append(PARAMS_SEP).append(WORD_SEP);
	    }
	    sb.append(formatTypeRef(pi.getTypeRef())).append(WORD_SEP).append(pi.getName());
	}

	sb.append(END_METHOD_PARAMS);

	/* add method body */
	sb.append(START_METHOD);

	/* add code blocks */
	int opBlockIndex = 0;
	String codeBlocksOffset=methodOffset+INDENT;
	
	for (OpBlockInfo opBlock : mi.getOpBlocks()) {
	    opBlockContext.setBlockIndex(opBlockIndex);
	    String codeBlock = opBlock.getCode(opBlockContext);
	    sb.append(LINE_SEP).append(codeBlocksOffset);
	    sb.append(codeBlock).append(OP_END);

	    opBlockIndex++;
	}

	sb.append(LINE_SEP).append(methodOffset);
	sb.append(END_METHOD);

	return sb.toString();
    }

    public static final String formatTypeRef(TypeRef typeRef) {
	StringBuffer sb = new StringBuffer();

	sb.append(typeRef.getTypeName());

	List<TypeRef> generics = typeRef.getGenerics();
	if (generics != null && generics.size() > 0) {
	    sb.append(START_GENERIC_TYPE);
	    boolean first = true;
	    for (TypeRef g : generics) {
		if (!first) {
		    sb.append(PARAMS_SEP);
		}
		first = false;

		sb.append(formatTypeRef(g));

	    }
	    sb.append(END_GENERIC_TYPE);
	}

	return sb.toString();
    }

    public static final String genAnnotationRef(AnnotationInfo ai) {
	StringBuffer sb = new StringBuffer();
	sb.append(ANNOTATION_REF).append(ai.getType().getTypeName());

	Object value = ai.getValue();
	List<ParameterInfo> params = ai.getParameters();
	if (value != null || (params != null && params.size() > 0)) {
	    sb.append(START_METHOD_PARAMS);
	    if (value != null) {
		sb.append(formatValue(value));
	    } else {
		boolean first = true;
		for (ParameterInfo pi : params) {
		    if (!first) {
			sb.append(PARAMS_SEP);
		    }
		    first = false;
		    sb.append(genParamAsKeyValue(pi));
		}
	    }

	    sb.append(END_METHOD_PARAMS);
	}

	return sb.toString();
    }

    public static final String genClass(ClassInfo ci) {
	String packageName = ci.getPackageName();
	String className = ci.getClassName();

	if (packageName == null || className == null) {
	    throw new RuntimeException("Package name and class name need to be specified");
	}

	StringBuffer sb = new StringBuffer();

	/* add package info */
	sb.append(PACKAGE).append(WORD_SEP).append(packageName).append(OP_END);
	sb.append(LINE_SEP);

	/* add imports */
//	Set<Class<?>> classDependencies = ci.getClassDependencies();
//	
//	for(Class<?> cd : classDependencies) {
//	    sb.append(IMPORT).append(WORD_SEP).append(cd.getName()).append(OP_END);
//	    sb.append(LINE_SEP);
//	}

	List<TypeRef> dependencies = ci.getDependencies();
	for (TypeRef d : dependencies) {
	    sb.append(IMPORT).append(WORD_SEP).append(d.getPackageName()).append(PACKAGE_SEP).append(d.getTypeName())
		    .append(OP_END);
	    sb.append(LINE_SEP);
	}

	/* add annotations */
	List<AnnotationInfo> annotations = ci.getAnnotations();
	if (annotations != null && annotations.size() > 0) {
	    for (AnnotationInfo ai : annotations) {
		sb.append(LINE_SEP);
		sb.append(genAnnotationRef(ai));
	    }
	}

	/* add class definition */
	String classModifiersString = Modifier.toString(ci.getModifiers());
	sb.append(LINE_SEP);
	sb.append(classModifiersString).append(WORD_SEP).append(ci.getTypeName()).append(WORD_SEP).append(className);

	/* add inheritance */
	TypeRef parent = ci.getParent();
	if (parent != null) {
	    sb.append(WORD_SEP).append(EXTENDS).append(WORD_SEP).append(formatTypeRef(parent));
	}

	/* add interfaces */

	sb.append(START_CLASS);

	/* add fields */
	for (FieldInfo fi : ci.getFields().values()) {
	    sb.append(LINE_SEP).append(INDENT);
	    sb.append(genField(fi));
	}
	
	sb.append(LINE_SEP);

	/* add constructors */
	for (MethodInfo cmi : ci.getConstructors().values()) {
	    /* generate a block context */
	    OpBlockContext opBlockContext = new OpBlockContext(ci);

	    sb.append(LINE_SEP);
	    sb.append(genMethod(cmi, opBlockContext));
	}

	/* add methods */

	for (MethodInfo mi : ci.getMethods().values()) {
	    /* generate a block context */
	    OpBlockContext opBlockContext = new OpBlockContext(ci);

	    sb.append(LINE_SEP);
	    sb.append(genMethod(mi, opBlockContext));
	}

	sb.append(LINE_SEP);
	sb.append(END_CLASS);

	return sb.toString();
    }
}
