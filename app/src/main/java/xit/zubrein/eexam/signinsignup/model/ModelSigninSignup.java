package xit.zubrein.eexam.signinsignup.model;

import android.net.wifi.hotspot2.pps.Credential;

import com.google.gson.annotations.SerializedName;

public class ModelSigninSignup {

    @SerializedName("status_code")
    String status_code;
    @SerializedName("registered")
    String registered;
    @SerializedName("token")
    String token;
    @SerializedName("user_details")
    Credentials credentials = new Credentials();


    public String getStatus_code() {
        return status_code;
    }

    public String getRegistered() {
        return registered;
    }
    public String getToken() {
        return token;
    }
    public String getUser_id() {
        return credentials.getId();
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public class Credentials{
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

        public String getId() {
            return id;
        }
    }
}
