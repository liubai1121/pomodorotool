module com.tool.pomodoro.technique.tool {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports com.tool.pomodoro.technique.tool to javafx.graphics;

    opens com.tool.pomodoro.technique.tool to javafx.fxml;
    opens com.tool.pomodoro.technique.tool.controller to javafx.fxml;
    opens com.tool.pomodoro.technique.tool.controller.vo to javafx.base;

    opens com.tool.pomodoro.technique.tool.strategy.database.todo.po;
    opens com.tool.pomodoro.technique.tool.strategy.database.today.po;
    opens com.tool.pomodoro.technique.tool.strategy.database.label.po;
    opens com.tool.pomodoro.technique.tool.controller.label to javafx.fxml;
}