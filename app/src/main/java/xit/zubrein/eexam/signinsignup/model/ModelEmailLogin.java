package xit.zubrein.eexam.signinsignup.model;

import com.google.gson.annotations.SerializedName;

public class ModelEmailLogin {
    @SerializedName("token")
    String token;
    @SerializedName("id")
    String user_id;
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
