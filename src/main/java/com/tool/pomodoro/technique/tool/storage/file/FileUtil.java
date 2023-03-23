package com.tool.pomodoro.technique.tool.storage.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private FileUtil() {

    }

    public static Optional<File> getLabelFile() {
        var filePath = FileConfig.getLabelFile();

        return getFileOrCreate(filePath);
    }

    public static Optional<File> getFileOrCreate(String filePath) {
        try {
            var file = new File(filePath);
            if (!file.exists()) {
                return file.createNewFile() ? Optional.of(file) : Optional.empty();
            }
            return Optional.of(file);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    public static void doSerialized(File file, List<?> data) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {

            for (Object todo : data) {
                String todoJson = objectMapper.writeValueAsString(todo);
                bw.write(todoJson);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> doDeserialized(File file, Class<T> clazz) {
        try {
            return Files.lines(Path.of(file.getAbsolutePath()))
                    .map(data -> convertToObject(data, clazz))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static <T> T convertToObject(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }
    }


    public static void doSerialized(File file, Object object) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            String todoJson = objectMapper.writeValueAsString(object);
            bw.write(todoJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Map<String, List<String>> doDeserializedLink(File file) {
        try {
            String dataMap = Files.readString(file.toPath());
            return objectMapper.readValue(dataMap, new TypeReference<HashMap<String, List<String>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public static Map<String, String> doDeserializedLinkForTodo(File file) {
        try {
            String dataMap = Files.readString(file.toPath());
            return objectMapper.readValue(dataMap, new TypeReference<HashMap<String, String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
