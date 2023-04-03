package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoVo;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class TodoDetailController implements Initializable {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
    }

    @FXML
    private TextArea todoDetailTextAreal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToolController.registerController(this);
    }

    public void display(TodoVo todoVo) {
        Optional.ofNullable(todoVo)
                .ifPresent(vo -> todoDetailTextAreal.setText(format(vo)));
    }

    private String format(TodoVo todoVo) {
        StringBuilder build = new StringBuilder();
        try {
            JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(todoVo));
            appendIfPresent("id", jsonNode, build);
            appendIfPresent("content", jsonNode, build);
            appendIfPresent("createTime", jsonNode, build);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return build.toString();
    }

    private void appendIfPresent(String fieldName, JsonNode jsonNode, StringBuilder builder) {
        Optional.ofNullable(jsonNode.get(fieldName))
                .map(JsonNode::asText)
                .ifPresent(val -> builder.append(fieldName).append("=").append(val).append("\r\n"));
    }
}
