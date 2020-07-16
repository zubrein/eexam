package xit.zubrein.eexam.statistics.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelStat implements Serializable {

    @SerializedName("date")
    String date;
    @SerializedName("total_right_answer")
    String total_right_answer;
    @SerializedName("total_question")
    String total_question;
    @SerializedName("question")
    List<Question_list> list ;


    public String getDate() {
        return date;
    }

    public String getTotal_right_answer() {
        return total_right_answer;
    }

    public String getTotal_question() {
        return total_question;
    }

    public List<Question_list> getList() {
        return list;
    }

    public class Question_list implements Serializable{
        @SerializedName("question")
        String question;
        @SerializedName("answer")
        String answer;
        @SerializedName("status")
        String status;

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public String getStatus() {
            return status;
        }
    }


}
