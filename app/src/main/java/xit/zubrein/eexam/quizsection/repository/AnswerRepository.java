package xit.zubrein.eexam.quizsection.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.quizsection.model.AnswerModel;
import xit.zubrein.eexam.challengefriend.model.ChallengeFriendAnswerModel;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;

public class AnswerRepository {
    
    MutableLiveData<String> data;
    private static final String TAG = "AnswerRepository";
    
    public MutableLiveData<String> answer_submit(String token,AnswerModel answerModel) {
        if(data == null){
            data = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<AnswerModel> call = service.answer_submit(token,answerModel);
        call.enqueue(new Callback<AnswerModel>() {
            @Override
            public void onResponse(Call<AnswerModel> call, Response<AnswerModel> response) {
                if (response.isSuccessful()) {
                    AnswerModel model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        Log.d(TAG, "onResponse: success");
                        data.setValue("success");
                    }else{
                        data.setValue("failed");
                    }
                } else {
                    data.setValue("server error");
                }

            }

            @Override
            public void onFailure(Call<AnswerModel> call, Throwable t) {
                data.setValue("No internet connect");
            }
        });

        return data;
    }
    public MutableLiveData<String> answer_submit_challenge_friend(String token, ChallengeFriendAnswerModel answerModel) {
        if(data == null){
            data = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ChallengeFriendAnswerModel> call = service.answer_submit_challengeFriend(token,answerModel);
        call.enqueue(new Callback<ChallengeFriendAnswerModel>() {
            @Override
            public void onResponse(Call<ChallengeFriendAnswerModel> call, Response<ChallengeFriendAnswerModel> response) {
                if (response.isSuccessful()) {
                    ChallengeFriendAnswerModel model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        Log.d(TAG, "onResponse: success");
                        data.setValue("success");
                    }else{
                        data.setValue("failed");
                    }
                } else {
                    data.setValue("server error");
                }

            }

            @Override
            public void onFailure(Call<ChallengeFriendAnswerModel> call, Throwable t) {
                data.setValue("No internet connect");
            }
        });

        return data;
    }

    
}
