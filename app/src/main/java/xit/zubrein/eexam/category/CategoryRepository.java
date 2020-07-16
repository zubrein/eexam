package xit.zubrein.eexam.category;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;

public class CategoryRepository {

    MutableLiveData<List<CategoryModel.subjects>> data ;
    Application application;

    public CategoryRepository(Application application) {
        this.application = application;
    }

    private static final String TAG = "CategoryRepository";

    public MutableLiveData<List<CategoryModel.subjects>> getSubjects(final String token) {
        if(data == null){
            data = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<CategoryModel> call = service.get_sebjects(token);
        call.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {

                Log.d(TAG, "onResponse: "+token);
                if (response.isSuccessful()) {
                    CategoryModel model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        data.setValue(model.subject_list);
                    }else{
                        Log.d(TAG, "onFailure: failed");
                    }
                } else {
                    Log.d(TAG, "onFailure: Server error" + token);
                }

            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Log.d(TAG, "onFailure: No internet connection");
            }
        });

        return data;
    }
    
}
