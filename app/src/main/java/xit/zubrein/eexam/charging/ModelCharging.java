package xit.zubrein.eexam.charging;

import com.google.gson.annotations.SerializedName;

public class ModelCharging {

    @SerializedName("subscription_status")
    String subscription_status;
    @SerializedName("status_code")
    String status_code;

    public String getStatus_code() {
        return status_code;
    }

    public String getSubscription_status() {
        return subscription_status;
    }
}
