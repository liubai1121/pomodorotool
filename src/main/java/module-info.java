module com.tool.pomodoro.technique.tool {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports com.tool.pomodoro.technique.tool to javafx.graphics;
    exports com.tool.pomodoro.technique.tool.controller.controller.today.vo to com.fasterxml.jackson.databind;
    exports com.tool.pomodoro.technique.tool.controller.controller.todo.vo to com.fasterxml.jackson.databind;

    opens com.tool.pomodoro.technique.tool to javafx.fxml;
    opens com.tool.pomodoro.technique.tool.controller.controller.todo.vo to javafx.base;

    opens com.tool.pomodoro.technique.tool.strategy.storage.todo.po;
    opens com.tool.pomodoro.technique.tool.strategy.storage.today.po;
    opens com.tool.pomodoro.technique.tool.strategy.storage.label.po;
    opens com.tool.pomodoro.technique.tool.controller.controller.label to javafx.fxml;
    opens com.tool.pomodoro.technique.tool.controller.controller.today to javafx.fxml;
    opens com.tool.pomodoro.technique.tool.controller.controller.label.vo to javafx.base;
    opens com.tool.pomodoro.technique.tool.controller.controller.today.vo to javafx.base;
    opens com.tool.pomodoro.technique.tool.controller.controller.todo to javafx.fxml;
    opens com.tool.pomodoro.technique.tool.controller.controller.tool to javafx.fxml;
    opens com.tool.pomodoro.technique.tool.controller.controller.report to javafx.fxml;


}