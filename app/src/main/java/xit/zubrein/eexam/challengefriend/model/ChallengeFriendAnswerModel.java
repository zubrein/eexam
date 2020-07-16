package xit.zubrein.eexam.challengefriend.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import xit.zubrein.eexam.quizsection.model.AnswerSetModel;

public class ChallengeFriendAnswerModel {

    @SerializedName("status_code")
    String status_code;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("code")
    String code;
    @SerializedName("score")
    String score;
    @SerializedName("answers")
    List<AnswerSetModel> list;

    public ChallengeFriendAnswerModel(String user_id, String code, String score, List<AnswerSetModel> list) {
        this.user_id = user_id;
        this.code = code;
        this.score = score;
        this.list = list;
    }

    public String getStatus_code() {
        return status_code;
    }
}
