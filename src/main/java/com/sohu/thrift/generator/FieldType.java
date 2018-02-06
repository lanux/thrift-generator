/**
 * 
 */
package com.sohu.thrift.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldType implements Cloneable {
	
	public static final int BASIC_TYPE = 1;
	public static final int COLLECTION_TYPE = 1 << 1;
	public static final int STRUCT_TYPE = 1 << 2;
	public static final int VOID_TYPE = 1 << 3;
	public static final int ENUM_TYPE = 1 << 4;
	
	public static final FieldType BOOL = new FieldType("bool", BASIC_TYPE, "boolean", "Boolean");
	public static final FieldType BYTE = new FieldType("byte", BASIC_TYPE, "byte", "Byte");
	public static final FieldType I16 = new FieldType("i16", BASIC_TYPE, "short", "Short");
	public static final FieldType I32 = new FieldType("i32", BASIC_TYPE, "int", "Integer");
	public static final FieldType I64 = new FieldType("i64", BASIC_TYPE, "long", "Long");
	public static final FieldType DOUBLE = new FieldType("double", BASIC_TYPE, "double", "Double");
	public static final FieldType STRING = new FieldType("string", BASIC_TYPE, "String", "String");
	public static final FieldType LIST = new FieldType("list", COLLECTION_TYPE, "List");
	public static final FieldType SET = new FieldType("set", COLLECTION_TYPE, "Set");
	public static final FieldType MAP = new FieldType("map", COLLECTION_TYPE, "Map");
	public static final FieldType ENUM = new FieldType("enum", ENUM_TYPE, "enum", "Enum");
	public static final FieldType VOID = new FieldType("void", VOID_TYPE, "void", "Void");
	public static final FieldType STRUCT = new FieldType("struct", STRUCT_TYPE, "class", "Class");
	
	private String value;
	
	private int type;
	
	private String javaTypeName;
	
	private Class<?> javaClass;
	
	private String warpperClassName;
	
	/**
	 * 
	 */
	public FieldType() {
		super();
	}

	/**
	 * @param value
	 * @param type
	 * @param javaTypeName
	 */
	private FieldType(String value, int type, String javaTypeName) {
		this.value = value;
		this.type = type;
		this.javaTypeName = javaTypeName;
	}

	/**
	 * @param value
	 * @param type
	 * @param javaTypeName
	 * @param warpperClassName
	 */
	private FieldType(String value, int type, String javaTypeName,
					  String warpperClassName) {
		this.value = value;
		this.type = type;
		this.javaTypeName = javaTypeName;
		this.warpperClassName = warpperClassName;
	}
	
	@Override
	public FieldType clone() {
		try {
			return (FieldType) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("clone object error!", e);
		}
	}
	
	public static FieldType fromJavaType(Type type) {
		Class<?> clazz = null;
		if(type instanceof ParameterizedType) {
			clazz = (Class<?>) ((ParameterizedType) type).getRawType();
		}else {
			clazz = (Class<?>) type;
		}
		return fromJavaType(clazz);
	}
	
	
	public static FieldType fromJavaType(Class<?> clazz) {
		if(clazz == short.class || clazz == Short.class) {
			return I16;
		}
		if(clazz == int.class || clazz == Integer.class) {
			return I32;
		}
		if(clazz == long.class || clazz == Long.class) {
			return I64;
		}
		if(clazz == String.class) {
			return STRING;
		}
		if(clazz == boolean.class || clazz == Boolean.class) {
			return BOOL;
		}
		if (clazz == double.class || clazz == Double.class) {
			return DOUBLE;
		}
		if(clazz == Date.class) {
			return I64;
		}
		if(List.class.isAssignableFrom(clazz)) {
			return LIST;
		}
		if(Set.class.isAssignableFrom(clazz)) {
			return SET;
		}
		if(Map.class.isAssignableFrom(clazz)) {
			return MAP;
		}
		if(clazz.isEnum()) {
			return ENUM;
		}
		if(!clazz.getName().startsWith("java.lang")) {
			FieldType fieldType = STRUCT.clone();
			fieldType.setValue(clazz.getSimpleName());
			return fieldType;
		}
		throw new RuntimeException("Unkonw type :" + clazz);
	}

	public String getTypeName() {
		if(isBasicType() || isCollection()) {
			return value;
		}
		if(isEnum()) {
			return "enum";
		}
		if(isStruct()) {
			return "struct";
		}
		return "unkonw";
	}
	
	public boolean isBasicType() {
		return (this.type & BASIC_TYPE) == BASIC_TYPE;
	}
	
	/**
	 * @return the isCollection
	 */
	public boolean isCollection() {
		return (this.type & COLLECTION_TYPE) == COLLECTION_TYPE;
	}
	
	public boolean isStruct() {
		return (this.type & STRUCT_TYPE) == STRUCT_TYPE;
	}

	public boolean isEnum() {
		return (this.type & ENUM_TYPE) == ENUM_TYPE;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the javaTypeName
	 */
	public String getJavaTypeName() {
		if(javaTypeName != null && !javaTypeName.isEmpty()) {
			return javaTypeName;
		}
		if(isStruct()) {
			return this.getJavaClass().getSimpleName();
		}
		return javaTypeName;
	}

	/**
	 * @param javaTypeName the javaTypeName to set
	 */
	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
	}

	/**
	 * @return the warpperClassName
	 */
	public String getWarpperClassName() {
		return warpperClassName;
	}

	/**
	 * @param warpperClassName the warpperClassName to set
	 */
	public void setWarpperClassName(String warpperClassName) {
		this.warpperClassName = warpperClassName;
	}

	/**
	 * @return the javaClass
	 */
	public Class<?> getJavaClass() {
		return javaClass;
	}

	/**
	 * @param javaClass the javaClass to set
	 */
	public void setJavaClass(Class<?> javaClass) {
		this.javaClass = javaClass;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	
}
