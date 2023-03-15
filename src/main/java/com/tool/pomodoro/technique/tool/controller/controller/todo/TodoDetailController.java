package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoVo;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TodoDetailController implements Initializable {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @FXML
    private TextArea todoDetailTextAreal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToolController.registerController(this);
    }

    public void display(TodoVo todoVo) {
        Optional.ofNullable(todoVo)
                .ifPresent(vo -> {
                    try {
                        String replace = objectMapper.writeValueAsString(vo)
                                .replace("{", "")
                                .replace("}", "")
                                .replace("\"", "")
                                .replace(":", "=");
                        String[] dataList = replace.split(",");
                        String data = String.join("\r\n", dataList);
                        todoDetailTextAreal.setText(data);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void displayNone() {
        todoDetailTextAreal.setText("");
    }
}
