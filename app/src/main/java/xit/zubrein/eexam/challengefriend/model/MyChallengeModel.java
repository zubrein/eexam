package xit.zubrein.eexam.challengefriend.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyChallengeModel implements Serializable {
    @SerializedName("status_code")
    String status_code;
    @SerializedName("my_challenge")
    List<My_challenge> myChallengeList;


    public String getStatus_code() {
        return status_code;
    }

    public List<My_challenge> getMyChallengeList() {
        return myChallengeList;
    }


    public class My_challenge implements Serializable{
        @SerializedName("code")
        String code;
        @SerializedName("total_accepted_challenge")
        String total_accepted_challenge;
        @SerializedName("date")
        String date;
        @SerializedName("challenge_details")
        List<Challenge_details> challengeDetailsList;

        public String getCode() {
            return code;
        }

        public String getDate() {
            return date;
        }

        public String getTotal_accepted_challenge() {
            return total_accepted_challenge;
        }

        public List<Challenge_details> getChallengeDetailsList() {
            return challengeDetailsList;
        }

        public class Challenge_details implements Serializable{
            @SerializedName("name")
            String name;
            @SerializedName("score")
            String score;
            @SerializedName("user_id")
            String user_id;

            public String getName() {
                return name;
            }

            public String getScore() {
                return score;
            }

            public String getUser_id() {
                return user_id;
            }
        }


    }

}

