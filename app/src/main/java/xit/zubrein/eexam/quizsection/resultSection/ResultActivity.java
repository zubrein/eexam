package xit.zubrein.eexam.quizsection.resultSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import xit.zubrein.eexam.HomeActivity;
import xit.zubrein.eexam.quizsection.model.AnswerModel;
import xit.zubrein.eexam.quizsection.model.AnswerSetModel;
import xit.zubrein.eexam.quizsection.viewmodels.AnswerViewModel;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.challengefriend.model.ChallengeFriendAnswerModel;

public class ResultActivity extends AppCompatActivity {

    List<AnswerSetModel> list = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String user_id, score, token,quiz_type,code,subject_id;
    AnswerViewModel viewModel;
    TextView txt;
    FrameLayout buttonOk,mid_section;
    LinearLayout loading;
    NewtonCradleLoading newtonCradleLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        viewModel = new ViewModelProvider(this).get(AnswerViewModel.class);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");

        Bundle b = getIntent().getExtras();

        list = (List<AnswerSetModel>) b.getSerializable("answerlist");
        score = b.getString("score");
        quiz_type = b.getString("quiz_type");
        code = b.getString("code");
        subject_id = b.getString("subject_id");

        txt = findViewById(R.id.txt);
        buttonOk = findViewById(R.id.buttonOk);
        mid_section = findViewById(R.id.mid_section);
        loading = findViewById(R.id.loading);
        newtonCradleLoading = findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();

        if(quiz_type.equals("regular")){
            AnswerModel answerModel = new AnswerModel(user_id, score,subject_id, list);

            viewModel.submit_answer(token, answerModel).observe(ResultActivity.this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s.equals("success")) {
                        loading.setVisibility(View.GONE);
                        mid_section.setVisibility(View.VISIBLE);
                        txt.setText(score+"");
                    }
                }
            });
        }else{
            ChallengeFriendAnswerModel answerModel = new ChallengeFriendAnswerModel(user_id,code, score, list);

            viewModel.answer_submit_challenge_friend(token, answerModel).observe(ResultActivity.this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s.equals("success")) {
                        loading.setVisibility(View.GONE);
                        mid_section.setVisibility(View.VISIBLE);
                        txt.setText(score+"");
                    }
                }
            });
        }


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
