package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT %s FROM %s", getColumnNames(), entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(getSelectAllSql() + " WHERE %s = ?",
                entityClassMetaData.getIdField().getName().toLowerCase());
    }

    @Override
    public String getInsertSql() {
        return String.format("INSERT INTO %s VALUES (%s)",
                getInsertParamsNames(),
                getInsertValues()
        );
    }

    @Override
    public String getUpdateSql() {
        return String.format("UPDATE %s SET %s WHERE %d = ?",
                entityClassMetaData.getName(),
                getUpdateParameters(),
                entityClassMetaData.getIdField().getName().toLowerCase()
        );
    }

    private String getInsertParamsNames() {
        return entityClassMetaData.getName()
                + "("
                + entityClassMetaData.getFieldsWithoutId()
                .stream().map(field -> field.getName().toLowerCase()).collect(Collectors.joining(","))
                + ")";
    }

    private String getInsertValues() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> "?").collect(Collectors.joining(","));
    }

    private String getUpdateParameters() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName().toLowerCase() + "= ?")
                .collect(Collectors.joining(","));
    }

    private String getColumnNames() {
        return entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(","));
    }

}
