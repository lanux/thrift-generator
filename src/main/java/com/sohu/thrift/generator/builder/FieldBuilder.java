package com.sohu.thrift.generator.builder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.sohu.thrift.generator.EnumEntity;
import com.sohu.thrift.generator.Generic;
import com.sohu.thrift.generator.FieldEntity;
import com.sohu.thrift.generator.ClassEntity;

public class FieldBuilder {
	
	public FieldEntity buildThriftField(ClassBuilder structBuilder, Field field, List<ClassEntity> structs, List<EnumEntity> enums) {
		FieldEntity thriftField = new FieldEntity();
		thriftField.setName(field.getName());
		if(field.getName().equals("__PARANAMER_DATA")) {
			return null;
		}
		Type type = field.getGenericType();
		Generic generic = Generic.fromType(field.getGenericType());
		thriftField.setGenericType(generic);
		if(type instanceof ParameterizedType) {
			structBuilder.buildStrutsByGeneric(structs, generic, enums);
		}else {
			if(generic.isEnum() || generic.isStruct()) {
				generic.setJavaClass(field.getType());
				generic.setValue(field.getType().getSimpleName());
				generic.setJavaTypeName(field.getType().getSimpleName());
				if(generic.isStruct()) {
					structBuilder.buildThriftStruct(field.getType(), structs, enums);
				}else {
					enums.add(structBuilder.buildThriftEnum(field.getType()));
				}
			}
		}
		return thriftField;
	}
}
