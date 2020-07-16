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




    class Credentials{
        @SerializedName("id")
        String id;

        public String getId() {
            return id;
        }
    }
}
