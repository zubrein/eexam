package xit.zubrein.eexam.category;

import com.google.gson.annotations.SerializedName;

public class ModelAvailability {

    @SerializedName("availability")
    String availability;
    @SerializedName("sub_status")
    String sub_status_paid;
    @SerializedName("msisdn")
    String msisdn;

    public String getAvailability() {
        return availability;
    }

    public String getSub_status_paid() {
        return sub_status_paid;
    }

    public String getMsisdn() {
        return msisdn;
    }
}
