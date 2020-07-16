package xit.zubrein.eexam.quizsection.model;

import java.io.Serializable;

public class AnswerSetModel implements Serializable {
    String question_id;
    String value;

    public AnswerSetModel(String question_id, String value) {
        this.question_id = question_id;
        this.value = value;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
