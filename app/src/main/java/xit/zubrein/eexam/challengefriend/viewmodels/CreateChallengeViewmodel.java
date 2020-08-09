package xit.zubrein.eexam.challengefriend.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import xit.zubrein.eexam.category.CategoryModel;
import xit.zubrein.eexam.category.CategoryRepository;
import xit.zubrein.eexam.challengefriend.model.CreateChallengeModel;
import xit.zubrein.eexam.challengefriend.model.RecieveCodeModel;
import xit.zubrein.eexam.challengefriend.repository.CreateChallengeRepository;

public class CreateChallengeViewmodel extends AndroidViewModel {

    CategoryRepository categoryRepository;
    CreateChallengeRepository createChallengeRepository;

    public MutableLiveData<List<CategoryModel.subjects>> subjectsMutableLiveData;
    public MutableLiveData<String> data;
    SharedPreferences sharedpreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
    String token = sharedpreferences.getString("token", "");

    public CreateChallengeViewmodel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        createChallengeRepository = new CreateChallengeRepository(application);
    }

    public LiveData<List<CategoryModel.subjects>> getSubjects() {

        subjectsMutableLiveData = categoryRepository.getSubjects(token);

        return subjectsMutableLiveData;
    }

    public LiveData<List<CategoryModel.subjects>> get_sebjects_create_challenge() {

        subjectsMutableLiveData = categoryRepository.get_sebjects_create_challenge(token);

        return subjectsMutableLiveData;
    }



    public LiveData<String> get_code(CreateChallengeModel model) {
        data = new MutableLiveData<>();
        data = createChallengeRepository.get_code(token, model);
        return data;
    }
}
