package xit.zubrein.eexam.signinsignup.model;

import com.google.gson.annotations.SerializedName;

public class ModelEmailLogin {
    @SerializedName("token")
    String token;
    @SerializedName("id")
    String user_id;
    @SerializedName("name")
    String name;
    @SerializedName("mobile")
    String msisdn;
    @SerializedName("email")
    String email;
    @SerializedName("city")
    String city;
    @SerializedName("current_study")
    String current_study;
    public String getName() {
        return name;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getCurrent_study() {
        return current_study;
    }

    @SerializedName("status_code")
    String status_code;

    public String getToken() {
        return token;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getStatus_code() {
        return status_code;
    }
}
