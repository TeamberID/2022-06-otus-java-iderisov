package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createInstance(rs);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), Collections.emptyList(), rs -> {
            try {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    T instance = createInstance(rs);
                    result.add(instance);
                }
                return result;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow();
    }

    @Override
    public long insert(Connection connection, T instance) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getInsertParameters(instance));
    }

    @Override
    public void update(Connection connection, T instance) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getUpdateParameters(instance));
    }

    private T createInstance(ResultSet rs) throws Exception {
        T instance = entityClassMetaData.getConstructor().newInstance();

        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            var field = instance.getClass().getDeclaredField(rs.getMetaData().getColumnName(i));
            field.setAccessible(true);
            field.set(instance, rs.getObject(i));
        }

        return instance;
    }

    private List<Object> getInsertParameters(T instance) {
        return entityClassMetaData.getFieldsWithoutId().stream().peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return field.get(instance);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    private List<Object> getUpdateParameters(T instance) {
        return entityClassMetaData.getAllFields().stream().peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return field.get(instance);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

}
