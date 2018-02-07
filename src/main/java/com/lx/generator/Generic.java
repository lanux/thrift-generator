package com.lx.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 泛型描述类,是一个树形的结构,每个Generic都有可能有多个types
 */
public class Generic extends FieldType {

    /**
     * 可能是FieldType,也可能还是泛型
     */
    private List<? super FieldType> types = new ArrayList<FieldType>();

    /**
     * @return the types
     */
    @SuppressWarnings("unchecked")
    public List<FieldType> getTypes() {
        return (List<FieldType>) types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(List<FieldType> types) {
        this.types = types;
    }

    public void addGeneric(FieldType generic) {
        types.add(generic);
    }

    @Override
    public String toString() {
        if (types == null || types.isEmpty()) {
            return this.getValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.getValue());
        sb.append("<");
        for (int i = 0; i < types.size(); i++) {
            Object type = types.get(i);
            FieldType fieldType = (FieldType) type;

            if (type instanceof Generic) {
                sb.append(((Generic) type).toString());
            } else {
                sb.append(fieldType.getValue());
            }

            if (i != types.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(">");
        return sb.toString();
    }

    public static Generic fromType(Type type) {
        Generic generic = new Generic();
        if (!(type instanceof ParameterizedType)) {
            FieldType fieldType = FieldType.fromJavaType(type);
            generic.setJavaClass(fieldType.getJavaClass());
            generic.setJavaTypeName(fieldType.getJavaTypeName());
            generic.setValue(fieldType.getValue());
            generic.setWarpperClassName(fieldType.getWarpperClassName());
            generic.setType(fieldType.getType());
            return generic;
        }
        FieldType fieldType = FieldType.fromJavaType(type);
        generic.setValue(fieldType.getValue());
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        for (Type typeArgument : types) {
            if (typeArgument instanceof ParameterizedType) {
                generic.addGeneric(fromType(typeArgument));
                continue;
            }
            FieldType typeArgumentFieldType = FieldType.fromJavaType((Class<?>) typeArgument);
            if (typeArgumentFieldType.isStruct() || typeArgumentFieldType.isEnum()) {
                typeArgumentFieldType = typeArgumentFieldType.clone();
                typeArgumentFieldType.setJavaClass((Class<?>) typeArgument);
                typeArgumentFieldType.setValue(((Class<?>) typeArgument).getSimpleName());
            }
            generic.addGeneric(typeArgumentFieldType);
        }
        return generic;
    }


}
