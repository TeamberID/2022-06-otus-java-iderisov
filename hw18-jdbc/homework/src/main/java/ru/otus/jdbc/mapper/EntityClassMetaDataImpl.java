package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData {

    private final String name;
    private final Constructor constructor;
    private final Field idField;
    private final List<Field> fields;
    private final List<Field> simpleFields;

    public EntityClassMetaDataImpl(Class<T> clazz) {

        this.name = clazz.getSimpleName().toLowerCase();

        try {
            this.constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Constructor not found");
        }

        this.idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow();

        this.simpleFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());

        this.fields = new ArrayList<>(simpleFields);
        this.fields.add(this.idField);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return simpleFields;
    }

}
