package xit.zubrein.eexam.challengefriend.viewmodels;

import android.app.Application;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.repository.MyChallengeRepository;

public class MyChallengeViewModel extends AndroidViewModel {

    MyChallengeRepository myChallengeRepository;
    MutableLiveData<List<MyChallengeModel.My_challenge>> listMutableLiveData;
    MutableLiveData<List<AcceptedChallengeModel.My_challenge>> listMutableLiveData2;
    SharedPreferences sharedPreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
    String user_id = sharedPreferences.getString("user_id","");
    String token = sharedPreferences.getString("token","");

    public MyChallengeViewModel(@NonNull Application application) {
        super(application);
        myChallengeRepository = new MyChallengeRepository(application);
    }

    public LiveData<List<MyChallengeModel.My_challenge>> get_my_challenge_list(){
        listMutableLiveData = myChallengeRepository.get_my_challenge(token,user_id);
        return listMutableLiveData;
    }
    public LiveData<List<AcceptedChallengeModel.My_challenge>> get_accepted_challenge(){
        listMutableLiveData2 = myChallengeRepository.get_accepted_challenge(token,user_id);
        return listMutableLiveData2;
    }


}
