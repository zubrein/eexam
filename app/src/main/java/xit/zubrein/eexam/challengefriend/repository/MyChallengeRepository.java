package xit.zubrein.eexam.challengefriend.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;

public class MyChallengeRepository {
    
    Application application;
    MutableLiveData<List<MyChallengeModel.My_challenge>> list;
    MutableLiveData<List<AcceptedChallengeModel.My_challenge>> list2;
    private static final String TAG = "MyChallengeRepository";

    public MyChallengeRepository(Application application) {
        this.application = application;
    }
    
    
    public MutableLiveData<List<MyChallengeModel.My_challenge>> get_my_challenge(String token,String user_id){
        Log.d(TAG, "get_my_challenge: called");
        if(list == null){
            list = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<MyChallengeModel> call = service.get_my_challenge(token, user_id);
        call.enqueue(new Callback<MyChallengeModel>() {
            @Override
            public void onResponse(Call<MyChallengeModel> call, Response<MyChallengeModel> response) {
                if (response.isSuccessful()) {
                    MyChallengeModel model = response.body();
                    Log.d(TAG, "onResponse: "+model.toString());
                    if(model.getStatus_code().equals("200")){
                        list.setValue(model.getMyChallengeList());
                    }else{
                        list.setValue(null);
                    }
                }else{
                    Log.d(TAG, "onResponse: Server error");
                }
            }

            @Override
            public void onFailure(Call<MyChallengeModel> call, Throwable t) {

            }
        });
        
        return list;
    }

    public MutableLiveData<List<AcceptedChallengeModel.My_challenge>> get_accepted_challenge(String token, String user_id){
        Log.d(TAG, "get_accepted_challenge: called");
        if(list2 == null){
            list2 = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<AcceptedChallengeModel> call = service.get_accepted_challenge(token, user_id);
        call.enqueue(new Callback<AcceptedChallengeModel>() {
            @Override
            public void onResponse(Call<AcceptedChallengeModel> call, Response<AcceptedChallengeModel> response) {
                if (response.isSuccessful()) {
                    AcceptedChallengeModel model = response.body();
                    Log.d(TAG, "onResponse: "+model.toString());
                    if(model.getStatus_code().equals("200")){
                        list2.setValue(model.getAcceptedChallengeList());
                    }else{
                        list2.setValue(null);
                    }
                }else{
                    Log.d(TAG, "onResponse: Server error");
                }
            }

            @Override
            public void onFailure(Call<AcceptedChallengeModel> call, Throwable t) {

            }
        });

        return list2;
    }
}
