package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        var is = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName);
        try (var reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            var gson = new Gson();
            Type measurementListType = new TypeToken<ArrayList<Measurement>>() {
            }.getType();
            return gson.fromJson(reader, measurementListType);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }

}
