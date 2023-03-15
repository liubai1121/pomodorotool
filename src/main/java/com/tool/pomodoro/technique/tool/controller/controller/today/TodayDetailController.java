package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TodayDetailController implements Initializable {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @FXML
    private TextArea todayDetailTextArea;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToolController.registerController(this);
    }

    public void display(TodayVo todayVo) {
        Optional.ofNullable(todayVo)
                .ifPresent(vo -> {
                    try {
                        todayDetailTextArea.setText(objectMapper.writeValueAsString(vo));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void displayNone() {
        todayDetailTextArea.setText("");
    }
}
