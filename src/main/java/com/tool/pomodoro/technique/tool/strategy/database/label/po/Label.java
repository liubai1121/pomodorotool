package com.tool.pomodoro.technique.tool.strategy.database.label.po;

import java.util.Objects;

public class Label implements Cloneable {
    private String labelId;
    private String labelName;

    public Label() {
    }

    public Label(String labelId, String labelName) {
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return Objects.equals(labelId, label.labelId) && Objects.equals(labelName, label.labelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelId, labelName);
    }

    @Override
    public String toString() {
        return "Label{" +
                "labelId='" + labelId + '\'' +
                ", labelName='" + labelName + '\'' +
                '}';
    }

    @Override
    public Label clone() {
        try {
            return (Label) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }


}
