package xit.zubrein.eexam.signinsignup.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import xit.zubrein.eexam.signinsignup.model.ModelRegister;
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;
import xit.zubrein.eexam.signinsignup.model.ModelUser;
import xit.zubrein.eexam.signinsignup.repository.SigninSignupRepository;

public class ViewModelSigninSignup extends AndroidViewModel {

    SigninSignupRepository signinSignupRepository;
    private MutableLiveData<String> response;
    private MutableLiveData<ModelSigninSignup> modelSigninSignupMutableLiveData;
    private MutableLiveData<ModelUser> modelUserMutableLiveData;
    private MutableLiveData<ModelRegister> modelRegisterMutableLiveData;
    SharedPreferences sharedPreferences;
    String user_id,token;

    public ViewModelSigninSignup(@NonNull Application application) {
        super(application);
        signinSignupRepository = new SigninSignupRepository(application);
        sharedPreferences = application.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");
        token = sharedPreferences.getString("token","");
    }

    public LiveData<ModelUser> get_user_data() {

        modelUserMutableLiveData = signinSignupRepository.getUserData(token,user_id);

        return modelUserMutableLiveData;
    }

    public LiveData<ModelSigninSignup> confirmOTPResponse(String msisdn, String otp) {

        modelSigninSignupMutableLiveData = signinSignupRepository.confirmOTP(msisdn, otp);

        return modelSigninSignupMutableLiveData;
    }

    public LiveData<ModelRegister> registerLiveData(String msisdn, String name, String email, String city, String current_study) {

        modelRegisterMutableLiveData = signinSignupRepository.register(msisdn, name, email,city,current_study);

        return modelRegisterMutableLiveData;
    }

}
