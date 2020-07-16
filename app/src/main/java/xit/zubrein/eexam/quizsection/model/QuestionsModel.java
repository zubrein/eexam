package xit.zubrein.eexam.quizsection.model;

/**
 * Created by zubrein on 7/15/19.
 */


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionsModel {

    @SerializedName("duration")
    public int duration;
    @SerializedName("status_code")
    public int status_code;
    @SerializedName("error")
    public String error;
    @SerializedName("availability")
    public boolean availability;
    @SerializedName("question")
    public List<QuestionList> questionList;

    public String getError() {
        return error;
    }

    public int getStatus_code() {
        return status_code;
    }

    public boolean isAvailability() {
        return availability;
    }

    public class QuestionList{
        @SerializedName("id")
        private String question_id;
        @SerializedName("question")
        private String question;
        @SerializedName("option1")
        private String optA;
        @SerializedName("option2")
        private String optB;
        @SerializedName("option3")
        private String optC;
        @SerializedName("option4")
        private String optD;
        @SerializedName("correct_answer")
        private String answer;


        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getOptA() {
            return optA;
        }

        public void setOptA(String optA) {
            this.optA = optA;
        }

        public String getOptB() {
            return optB;
        }

        public void setOptB(String optB) {
            this.optB = optB;
        }

        public String getOptC() {
            return optC;
        }

        public void setOptC(String optC) {
            this.optC = optC;
        }

        public String getOptD() {
            return optD;
        }

        public void setOptD(String optD) {
            this.optD = optD;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }


    }


}