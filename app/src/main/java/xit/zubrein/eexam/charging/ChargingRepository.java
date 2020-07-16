package xit.zubrein.eexam.charging;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;


public class ChargingRepository {

    String data;
    Activity activity;

    public ChargingRepository(Activity activity) {
        this.activity = activity;
    }

    public String get_sub_status(String token, String user_id) {
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelCharging> call = service.check_subscription(token, user_id);
        call.enqueue(new Callback<ModelCharging>() {
            @Override
            public void onResponse(Call<ModelCharging> call, Response<ModelCharging> response) {

                if (response.isSuccessful()) {
                    ModelCharging model = response.body();
                    data = model.getSubscription_status();

                }

            }

            @Override
            public void onFailure(Call<ModelCharging> call, Throwable t) {
            }
        });

        return data;
    }

    public String do_charge(String token, String user_id) {
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelCharging> call = service.charge(token, user_id);
        call.enqueue(new Callback<ModelCharging>() {
            @Override
            public void onResponse(Call<ModelCharging> call, Response<ModelCharging> response) {

                if (response.isSuccessful()) {
                    ModelCharging model = response.body();
                    data = model.getStatus_code();

                }

            }

            @Override
            public void onFailure(Call<ModelCharging> call, Throwable t) {
            }
        });

        return data;
    }

}
