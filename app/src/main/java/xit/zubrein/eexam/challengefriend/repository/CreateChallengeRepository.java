package xit.zubrein.eexam.challengefriend.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.challengefriend.model.CreateChallengeModel;
import xit.zubrein.eexam.challengefriend.model.RecieveCodeModel;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelRegister;

public class CreateChallengeRepository {
    Application application;
    MutableLiveData<String> data;
    MutableLiveData<RecieveCodeModel> modelReceiveCode;
    private static final String TAG = "CreateChallengeReposito";

    public CreateChallengeRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<String> get_code(String token, CreateChallengeModel model) {
        if (data == null) {
            data = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<RecieveCodeModel> call = service.get_code(token, model);
        call.enqueue(new Callback<RecieveCodeModel>() {
            @Override
            public void onResponse(Call<RecieveCodeModel> call, Response<RecieveCodeModel> response) {
                if (response.isSuccessful()) {
                    RecieveCodeModel model = response.body();
                    data.setValue(model.getCode());

                }
            }

            @Override
            public void onFailure(Call<RecieveCodeModel> call, Throwable t) {
            }
        });

        return data;
    }
}
