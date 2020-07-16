package xit.zubrein.eexam.statistics.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import xit.zubrein.eexam.statistics.models.ModelStat;
import xit.zubrein.eexam.statistics.repository.StatisticsRepository;

public class StatViemodel extends AndroidViewModel {

    StatisticsRepository repository;
    MutableLiveData<List<ModelStat>> listMutableLiveData;
    SharedPreferences sharedPreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
    String user_id = sharedPreferences.getString("user_id","");
    String token = sharedPreferences.getString("token","");

    public StatViemodel(@NonNull Application application) {
        super(application);
        repository = new StatisticsRepository();
    }

    public LiveData<List<ModelStat>> getStatDate(String subject){
        listMutableLiveData = repository.getStatDate(token,user_id,subject);
        return listMutableLiveData;
    }




}
