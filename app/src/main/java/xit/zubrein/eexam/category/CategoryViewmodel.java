package xit.zubrein.eexam.category;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


public class CategoryViewmodel extends AndroidViewModel {

    CategoryRepository categoryRepository;

    public MutableLiveData<List<CategoryModel.subjects>> subjectsMutableLiveData;
    SharedPreferences sharedpreferences =getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
    String token = sharedpreferences.getString("token","");

    public CategoryViewmodel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
    }

    public LiveData<List<CategoryModel.subjects>> getSubjects(){

        subjectsMutableLiveData = categoryRepository.getSubjects(token);

        return subjectsMutableLiveData;
    }

}
