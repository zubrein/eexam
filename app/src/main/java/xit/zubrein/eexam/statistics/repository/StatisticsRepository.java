package xit.zubrein.eexam.statistics.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.statistics.models.ModelStat;

public class StatisticsRepository {

    MutableLiveData<List<ModelStat>> list;
    private static final String TAG = "StatisticsRepository";

    public MutableLiveData<List<ModelStat>> getStatDate(String token, String user_id, String subject_id){
        Log.d(TAG, "getStatDate: called");
        if(list == null){
            list = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<List<ModelStat>> call = service.get_all_stat(token, user_id, subject_id);
        call.enqueue(new Callback<List<ModelStat>>() {
            @Override
            public void onResponse(Call<List<ModelStat>> call, Response<List<ModelStat>> response) {
                if (response.isSuccessful()) {
                    List<ModelStat> model = response.body();
                    list.setValue(model);
                    Log.d(TAG, "onResponse: "+model.size());
                }

            }

            @Override
            public void onFailure(Call<List<ModelStat>> call, Throwable t) {

            }
        });

        return list;
    }
    
}
