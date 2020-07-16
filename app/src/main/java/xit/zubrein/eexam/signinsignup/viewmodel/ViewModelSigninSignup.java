package xit.zubrein.eexam.signinsignup.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import xit.zubrein.eexam.signinsignup.model.ModelRegister;
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;
import xit.zubrein.eexam.signinsignup.repository.SigninSignupRepository;

public class ViewModelSigninSignup extends AndroidViewModel {

    SigninSignupRepository signinSignupRepository;
    private MutableLiveData<String> response;
    private MutableLiveData<ModelSigninSignup> modelSigninSignupMutableLiveData;
    private MutableLiveData<ModelRegister> modelRegisterMutableLiveData;


    public ViewModelSigninSignup(@NonNull Application application) {
        super(application);
        signinSignupRepository = new SigninSignupRepository(application);
    }

    public LiveData<ModelSigninSignup> sendOTPResponse(String msisdn) {

        modelSigninSignupMutableLiveData = signinSignupRepository.sendOTP(msisdn);

        return modelSigninSignupMutableLiveData;
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
