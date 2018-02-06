package com.sohu.thrift.generator.builder;

import com.sohu.thrift.generator.ClassEntity;
import com.sohu.thrift.generator.EnumEntity;
import com.sohu.thrift.generator.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanux on 2018/2/6.
 */
public class test {
    public static void main(String[] args) {
        ClassBuilder classBuilder = new ClassBuilder();
        List<ClassEntity> structs = new ArrayList<>();
        List<EnumEntity> enums = new ArrayList<>();
        classBuilder.buildThriftStruct(User.class,structs,enums);
        System.out.println(structs);
    }
}
