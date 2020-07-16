package xit.zubrein.eexam.category;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {
    @SerializedName("status_code")
    String status_code;
    @SerializedName("subjects")
    List<subjects> subject_list;


    public class subjects{
        @SerializedName("id")
        String id;
        @SerializedName("subject_name")
        String subject_name;

        public String getId() {
            return id;
        }

        public String getSubject_name() {
            return subject_name;
        }
    }

    public String getStatus_code() {
        return status_code;
    }
}
