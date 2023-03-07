package com.tool.pomodoro.technique.tool.database.file;

public class FileConfig {

    private static final String FOLDER_DIRECTORY = "D:\\Program Files\\Pomodoro Technique Tool\\";

    private static final String FILE_SUFFIX= ".json";

    private static final String TODO_FILE_NAME = "todo";

    public static String getTodoFile() {
        return FOLDER_DIRECTORY + TODO_FILE_NAME + FILE_SUFFIX;
    }

    public static String getFolderDirectory() {
        return FOLDER_DIRECTORY;
    }

    public static String getFileSuffix() {
        return FILE_SUFFIX;
    }



}
