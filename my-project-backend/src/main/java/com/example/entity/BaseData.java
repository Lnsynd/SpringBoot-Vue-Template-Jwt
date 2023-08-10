package com.example.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * Created by 刘千山 on 2023/8/10/010-15:42
 */
public interface BaseData {

    default <V> V asViewObject(Class<V> clazz, Consumer<V> consumer){
        V v = this.asViewObject(clazz);
        consumer.accept(v);
        return v;
    }

    default <V> V asViewObject(Class<V> clazz) {
        try {
            // 获取字段
            Field[] declaredFields = clazz.getDeclaredFields();
            // 获取构造器
            Constructor<V> constructor = clazz.getConstructor();
            V v = constructor.newInstance();
            for (Field declaredField : declaredFields) {
                convert(declaredField,v);
            }
            return v;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    private void convert(Field field, Object vo) {
        try {
            Field source = this.getClass().getDeclaredField(field.getName());
            field.setAccessible(true);
            source.setAccessible(true);
            field.set(vo,source.get(this));
        } catch ( NoSuchFieldException | IllegalAccessException ignored) {

        }
    }
}
