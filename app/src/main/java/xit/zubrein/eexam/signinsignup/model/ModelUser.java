package xit.zubrein.eexam.signinsignup.model;

import com.google.gson.annotations.SerializedName;

public class ModelUser {
    @SerializedName("id")
    String id;
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

    public String getId() {
        return id;
    }

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
}
