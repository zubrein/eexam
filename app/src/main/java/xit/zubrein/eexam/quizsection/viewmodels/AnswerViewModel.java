package xit.zubrein.eexam.quizsection.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import xit.zubrein.eexam.quizsection.model.AnswerModel;
import xit.zubrein.eexam.quizsection.repository.AnswerRepository;
import xit.zubrein.eexam.challengefriend.model.ChallengeFriendAnswerModel;

public class AnswerViewModel extends AndroidViewModel {

    private MutableLiveData<String> response;
    AnswerRepository answerRepository;

    public AnswerViewModel(@NonNull Application application) {
        super(application);
        answerRepository = new AnswerRepository();
    }

    public LiveData<String> submit_answer(String token, AnswerModel answerModel) {

        response = answerRepository.answer_submit(token,answerModel);

        return response;
    }

    public LiveData<String> answer_submit_challenge_friend(String token, ChallengeFriendAnswerModel answerModel) {

        response = answerRepository.answer_submit_challenge_friend(token,answerModel);

        return response;
    }



}
