package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        try (var outputStream = new FileOutputStream(fileName)) {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
