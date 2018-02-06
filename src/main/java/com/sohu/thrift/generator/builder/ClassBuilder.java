package com.sohu.thrift.generator.builder;

import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sohu.thrift.generator.*;
import org.apache.log4j.Logger;

import com.sohu.thrift.generator.ClassEntity;

public class ClassBuilder {
	
    private static Logger log = Logger.getLogger(ClassBuilder.class);
	
    FieldBuilder fieldBuilder = new FieldBuilder();
    
    private boolean isIncludeSuperField;

    ClassBuilder() {
        isIncludeSuperField = true;
    }
	
    public ClassEntity buildThriftStruct(Class<?> clazz,
                                         List<ClassEntity> structs,
                                         List<EnumEntity> enums) {

//        boolean exists = structs.stream().anyMatch(p -> p.getPeerClass().equals(clazz));
//        if (exists) return null;
        // check class type
        TypeVariable<?>[] typeVar = clazz.getTypeParameters();
        if (typeVar != null && typeVar.length != 0) {
            if (!List.class.isAssignableFrom(clazz) &&
                    !Set.class.isAssignableFrom(clazz) &&
                    !Map.class.isAssignableFrom(clazz)) {
                StringBuilder tip = new StringBuilder();
                tip.append("Current class:" + clazz.getName());
                throw new IllegalArgumentException("only list/set/map support generic!" 
                        + tip.toString());
            }
        }
        List<Field> fields = new ArrayList<Field>();
        
        if (isIncludeSuperField) {
            Class<?> superClass = clazz.getSuperclass();
            while (superClass != null && !superClass.getName().startsWith("java.lang")) {
                log.debug("include super class field, class:" + clazz.getSuperclass());
                Field[] sf = superClass.getDeclaredFields();
                for (Field s : sf) {
                    fields.add(s);
                }
                superClass = superClass.getSuperclass();
            }
        }
	
        Field[] myFields = clazz.getDeclaredFields();
        for (Field f : myFields) {
            fields.add(f);
        }

		ClassEntity struct = new ClassEntity();
		List<FieldEntity> thriftFields = new ArrayList<FieldEntity>();
		for (Field field : fields) {
			FieldEntity thriftField = fieldBuilder.buildThriftField(this, field, structs, enums);
			if(thriftField == null) {
				continue;
			}
			thriftFields.add(thriftField);
		}
		struct.setName(clazz.getSimpleName());
		struct.setFields(thriftFields);
		struct.setPeerClass(clazz);
        structs.add(struct);

		return struct;
	}
	
	/**
	 * @param structs
	 * @param generic
	 */
	public void buildStrutsByGeneric(List<ClassEntity> structs,
			Generic generic, List<EnumEntity> enums) {
		List<FieldType> fieldTypes = generic.getTypes();
        for (FieldType subFieldType : fieldTypes) {
            if (subFieldType.isStruct()) {
                buildThriftStruct(subFieldType.getJavaClass(), structs, enums);
            }
            if (subFieldType instanceof Generic) {
                this.buildStrutsByGeneric(structs, (Generic) subFieldType, enums);
            }
        }
	}
	
	public EnumEntity buildThriftEnum(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		EnumEntity enumEntity = new EnumEntity();
		enumEntity.setName(clazz.getSimpleName());
		
		List<EnumField> nameValues = new ArrayList<EnumField>();
		for (int i = 0;i < fields.length;i ++) {
			Field field = fields[i];
			if (field.getName().equals("ENUM$VALUES") ||
					field.getName().equals("$VALUES") ||
					field.getName().equals("__PARANAMER_DATA")) {
				continue;
			}
			EnumField nameValue = new EnumField(field.getName(), i);
			nameValues.add(nameValue);
		}
		enumEntity.setFields(nameValues);
		return enumEntity;
	}
    
    public void setIncludeSuperField(boolean isInclude) {
        this.isIncludeSuperField = isInclude;
    }
}
