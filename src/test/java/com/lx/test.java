package com.lx;

import com.lx.entity.User;
import com.lx.generator.ClassEntity;
import com.lx.generator.EnumEntity;
import com.lx.generator.builder.ClassBuilder;

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
