package xit.zubrein.eexam.quizsection.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import xit.zubrein.eexam.quizsection.database.Questions;
import xit.zubrein.eexam.quizsection.database.QuestionsRepository;


/**
 * Created by zubrein on 7/15/19.
 */

public class QuestionsViewModel extends AndroidViewModel {

    private QuestionsRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Questions>> mAllQuestions;

    public QuestionsViewModel (Application application) {
        super(application);
        mRepository = new QuestionsRepository(application);
        mAllQuestions = mRepository.getmAllQuestions();
    }

    public LiveData<List<Questions>> getAllQuestions() { return mAllQuestions; }

    public void insert(Questions word) {
        mRepository.insert(word);
    }
    public void delete() { mRepository.del(); }
}