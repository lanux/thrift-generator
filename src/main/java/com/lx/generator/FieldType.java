package com.lx.generator;

import com.lx.generator.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldType implements Cloneable {
	
	public static final int BASIC_TYPE = 1;
	public static final int COLLECTION_TYPE = 1 << 1;
	public static final int STRUCT_TYPE = 1 << 2;
	public static final int VOID_TYPE = 1 << 3;
	public static final int ENUM_TYPE = 1 << 4;

	public static final FieldType BASIC = new FieldType("basic", BASIC_TYPE, "boolean", "Boolean");
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
		boolean basicType = CommonUtils.isBasicType(clazz);
		if (basicType){
			return BASIC.setValue(clazz.getSimpleName()).setJavaClass(clazz);
		}
		if(List.class.isAssignableFrom(clazz)) {
			return LIST.setValue(clazz.getSimpleName()).setJavaClass(clazz);
		}
		if(Set.class.isAssignableFrom(clazz)) {
			return SET.setValue(clazz.getSimpleName()).setJavaClass(clazz);
		}
		if(Map.class.isAssignableFrom(clazz)) {
			return MAP.setValue(clazz.getSimpleName()).setJavaClass(clazz);
		}
		if(clazz.isEnum()) {
			return ENUM.setValue(clazz.getSimpleName()).setJavaClass(clazz);
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
	public FieldType setValue(String value) {
		this.value = value;
		return this;
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
	public FieldType setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
		return this;
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
	public FieldType setWarpperClassName(String warpperClassName) {
		this.warpperClassName = warpperClassName;
		return this;
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
	public FieldType setJavaClass(Class<?> javaClass) {
		this.javaClass = javaClass;
		return this;
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
	public FieldType setType(int type) {
		this.type = type;
		return this;
	}
	
	
}
