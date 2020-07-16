package xit.zubrein.eexam.challengefriend.model;

import com.google.gson.annotations.SerializedName;

public class RecieveCodeModel {

    @SerializedName("status_code")
    String status_code;
    @SerializedName("code")
    String code;

    public String getStatus_code() {
        return status_code;
    }

    public String getCode() {
        return code;
    }
}
