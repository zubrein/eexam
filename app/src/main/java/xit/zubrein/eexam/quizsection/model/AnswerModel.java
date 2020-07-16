package xit.zubrein.eexam.quizsection.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerModel {

    @SerializedName("status_code")
    String status_code;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("score")
    String score;
    @SerializedName("subject_id")
    String subject_id;
    @SerializedName("answers")
    List<AnswerSetModel> list;

    public AnswerModel(String user_id, String score, String subject_id, List<AnswerSetModel> list) {
        this.user_id = user_id;
        this.score = score;
        this.subject_id = subject_id;
        this.list = list;
    }

    public String getStatus_code() {
        return status_code;
    }
}
