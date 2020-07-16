package xit.zubrein.eexam.signinsignup.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelRegister;
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;


public class SigninSignupRepository {

    Application application;
    MutableLiveData<String> data ;
    MutableLiveData<ModelSigninSignup> modelSigninSignup;
    MutableLiveData<ModelRegister> modelRegister;
    private static final String TAG = "SigninSignupRepository";

    public SigninSignupRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<ModelSigninSignup> sendOTP(String msisdn) {
        if(modelSigninSignup == null){
            modelSigninSignup = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelSigninSignup> call = service.send_otp(msisdn);
        call.enqueue(new Callback<ModelSigninSignup>() {
            @Override
            public void onResponse(Call<ModelSigninSignup> call, Response<ModelSigninSignup> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: called");
                    ModelSigninSignup model = response.body();
                    modelSigninSignup.setValue(model);
                }

            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
                data.setValue("No internet connect");
            }
        });

        return modelSigninSignup;
    }

    public MutableLiveData<ModelSigninSignup> confirmOTP(String msisdn,String otp) {
        if(modelSigninSignup == null){
            modelSigninSignup = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelSigninSignup> call = service.confirm_otp(msisdn,otp);
        call.enqueue(new Callback<ModelSigninSignup>() {
            @Override
            public void onResponse(Call<ModelSigninSignup> call, Response<ModelSigninSignup> response) {
                if (response.isSuccessful()) {
                    ModelSigninSignup model = response.body();
                    modelSigninSignup.setValue(model);
//                    if (model.getStatus_code().equals("200")) {
//                        data.setValue("success");
//                    }else{
//                        data.setValue("Please check your OTP");
//                    }
                }
            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
            }
        });

        return modelSigninSignup;
    }

    public MutableLiveData<ModelRegister> register(String msisdn,String name,String email,String city,String current_study) {
        if(modelRegister == null){
            modelRegister = new MutableLiveData<>();
        }
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelRegister> call = service.register(msisdn,name,email,city,current_study);
        call.enqueue(new Callback<ModelRegister>() {
            @Override
            public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                if (response.isSuccessful()) {
                    ModelRegister model = response.body();
                    modelRegister.setValue(model);
                }
            }

            @Override
            public void onFailure(Call<ModelRegister> call, Throwable t) {
            }
        });

        return modelRegister;
    }
}
