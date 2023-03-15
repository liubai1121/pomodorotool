package com.tool.pomodoro.technique.tool.storage.file;

public class FileConfig {

    private static final String FOLDER_DIRECTORY = "D:\\Program Files\\Pomodoro Technique Tool\\";

    private static final String FILE_SUFFIX= ".json";

    private static final String TODO_FILE_NAME = "todo";

    private static final String LABEL_FILE_NAME = "label";

    public static String getTodoFile() {
        return FOLDER_DIRECTORY + TODO_FILE_NAME + FILE_SUFFIX;
    }

    public static String getFolderDirectory() {
        return FOLDER_DIRECTORY;
    }

    public static String getFileSuffix() {
        return FILE_SUFFIX;
    }

    public static String getLabelFile() {
        return FOLDER_DIRECTORY + LABEL_FILE_NAME + FILE_SUFFIX;
    }



}
