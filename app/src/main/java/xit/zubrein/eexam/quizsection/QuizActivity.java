package xit.zubrein.eexam.quizsection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbr.gradienttextview.GradientTextView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.FoldingCube;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.quizsection.database.Questions;
import xit.zubrein.eexam.quizsection.model.AnswerSetModel;
import xit.zubrein.eexam.quizsection.model.QuestionsModel;
import xit.zubrein.eexam.quizsection.resultSection.ResultActivity;
import xit.zubrein.eexam.quizsection.viewmodels.QuestionsViewModel;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.utils.LoadingBar;

public class QuizActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private static long COUNTDOWN_IN_MILLIS = 15000;
    int questionCount = 0;
    List<Questions> quesList;
    int score = 0;
    int qid = 0;
    Questions currentQ;
    private TextView txtQuestion;
    private TextView serial;
    private GradientTextView textViewScore;
    private GradientTextView textViewQuestionCount;
    private TextView opt1;
    private TextView opt2;
    private TextView opt3;
    private TextView opt4;
    CardView opt1_blue, opt1_red, opt1_green, opt2_blue, opt2_red, opt2_green, opt3_blue, opt3_red, opt3_green, opt4_blue, opt4_red, opt4_green;
    private GradientTextView textViewCountDown;
    private QuestionsViewModel questionsViewModel;
    private RelativeLayout relativeLayout, heade,progressBarLayout;

    private boolean answered = false;
    SharedPreferences sharedPreferences;
    String token, user_id, quiz_type, code = "", subject;
//    LoadingBar loadingBar;
    SpinKitView spin_kit;
    List<AnswerSetModel> list = new ArrayList<AnswerSetModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

//        loadingBar = new LoadingBar();
//        loadingBar.showDialog(QuizActivity.this);

        subject = getIntent().getStringExtra("subject_id");
        quiz_type = getIntent().getStringExtra("quiz_type");

        if (quiz_type.equals("challenge")) {
            code = getIntent().getStringExtra("code");
        }

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        user_id = sharedPreferences.getString("user_id", "");

        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        opt1_red = findViewById(R.id.opt1_red);
        opt1_green = findViewById(R.id.opt1_green);
        opt1_blue = findViewById(R.id.opt1_blue);
        opt2_red = findViewById(R.id.opt2_red);
        opt2_green = findViewById(R.id.opt2_green);
        opt2_blue = findViewById(R.id.opt2_blue);
        opt3_red = findViewById(R.id.opt3_red);
        opt3_green = findViewById(R.id.opt3_green);
        opt3_blue = findViewById(R.id.opt3_blue);
        opt4_red = findViewById(R.id.opt4_red);
        opt4_green = findViewById(R.id.opt4_green);
        opt4_blue = findViewById(R.id.opt4_blue);
        heade = findViewById(R.id.heade);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        FoldingCube foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);


        relativeLayout = (RelativeLayout) findViewById(R.id.profileLoadingScreen);
        if (quiz_type.equals("regular")) {
            fetchQuestionsforRegularexam();
        } else {
            fetchQuestionsforchallenge();
        }


        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);

        questionsViewModel.getAllQuestions().observe(this, new Observer<List<Questions>>() {
            @Override
            public void onChanged(@Nullable final List<Questions> words) {
                // Update the cached copy of the words in the adapter.
                quesList = words;
                Collections.shuffle(quesList);
            }
        });

    }

    private void setQuestionView() {
        txtQuestion.setText(currentQ.getQuestion());
        opt1.setText(currentQ.getOptA());
        opt2.setText(currentQ.getOptB());
        opt3.setText(currentQ.getOptC());
        opt4.setText(currentQ.getOptD());
        qid++;
        serial.setText(qid+"/"+questionCount+ " |");
    }

    private void fetchQuestionsforRegularexam() {
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<QuestionsModel> call = service.getQuestions(token, user_id, subject);
        call.enqueue(new Callback<QuestionsModel>() {
            @Override
            public void onResponse(Call<QuestionsModel> call, Response<QuestionsModel> response) {
                if (response.isSuccessful()) {
                    QuestionsModel questionsModel = response.body();
                    List<QuestionsModel.QuestionList> list = questionsModel.questionList;

                    questionsViewModel.delete();
                    questionCount = list.size();
                    for (int i = 0; i < questionCount; i++) {
                        String question_id = list.get(i).getQuestion_id();
                        String question = list.get(i).getQuestion();
                        String answer = list.get(i).getAnswer();
                        String opta = list.get(i).getOptA();
                        String optb = list.get(i).getOptB();
                        String optc = list.get(i).getOptC();
                        String optd = list.get(i).getOptD();

                        Questions questions = new Questions(question_id, question, opta,
                                optb, optc, optd, answer);

                        questionsViewModel.insert(questions);
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            takeAction();

                        }
                    }, 3000);

                } else {
                    Toast.makeText(QuizActivity.this, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionsModel> call, Throwable t) {

            }
        });
    }

    private void fetchQuestionsforchallenge() {
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<QuestionsModel> call = service.getQuestionsChallengefriend(token, user_id, code);
        call.enqueue(new Callback<QuestionsModel>() {
            @Override
            public void onResponse(Call<QuestionsModel> call, Response<QuestionsModel> response) {
                if (response.isSuccessful()) {
                    QuestionsModel questionsModel = response.body();
                    if (questionsModel.getStatus_code() == 510) {
                        CustomToastQActivity toastQActivity = new CustomToastQActivity();
                        toastQActivity.showCustomToast("You already accepted this challenge before. Please try a new one.");
                    } else if(questionsModel.getStatus_code() == 503){
                        CustomToastQActivity toastQActivity = new CustomToastQActivity();
                        toastQActivity.showCustomToast("Your given code doesn't exist. Please try a new one.");
                    }else {
                        List<QuestionsModel.QuestionList> list = questionsModel.questionList;

                        questionsViewModel.delete();
                        questionCount = list.size();
                        for (int i = 0; i < questionCount; i++) {
                            String question_id = list.get(i).getQuestion_id();
                            String question = list.get(i).getQuestion();
                            String answer = list.get(i).getAnswer();
                            String opta = list.get(i).getOptA();
                            String optb = list.get(i).getOptB();
                            String optc = list.get(i).getOptC();
                            String optd = list.get(i).getOptD();

                            Questions questions = new Questions(question_id, question, opta,
                                    optb, optc, optd, answer);

                            questionsViewModel.insert(questions);
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                takeAction();

                            }
                        }, 3000);
                    }


                } else {
                    Toast.makeText(QuizActivity.this, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionsModel> call, Throwable t) {

            }
        });
    }

    public void takeAction() {
        progressBarLayout.setVisibility(View.GONE);
//        loadingBar.cancelDialog();
        heade.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        COUNTDOWN_IN_MILLIS = sharedPreferences.getLong("quiz_time", (long) 0.0) * 1000;
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();
        currentQ = quesList.get(qid);
        txtQuestion = (TextView) findViewById(R.id.textView1);
        serial = (TextView) findViewById(R.id.serial);

        opt1 = findViewById(R.id.opt1);
        opt2 = findViewById(R.id.opt2);
        opt3 = findViewById(R.id.opt3);
        opt4 = findViewById(R.id.opt4);
        setQuestionView();

        opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(1);
                button_disabled();
            }
        });
        opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(2);
                button_disabled();
            }
        });
        opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(3);
                button_disabled();
            }
        });
        opt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(4);
                button_disabled();
            }
        });

    }


    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                check_answer(0);
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    public void check_answer(int n) {

        answered = true;
        countDownTimer.cancel();

        if (n == Integer.parseInt(currentQ.getAnswer())) {
            score++;
            textViewScore.setText("Your score : " + score);

            AnswerSetModel answerSetModel = new AnswerSetModel(currentQ.getQuestion_id(), "1");
            list.add(answerSetModel);

        } else {
            AnswerSetModel answerSetModel = new AnswerSetModel(currentQ.getQuestion_id(), "0");
            list.add(answerSetModel);
        }

        if (Integer.parseInt(currentQ.getAnswer()) == 1) {
            opt1_green.setVisibility(View.VISIBLE);
            opt1_blue.setVisibility(View.INVISIBLE);
            opt1_red.setVisibility(View.INVISIBLE);

        } else if (Integer.parseInt(currentQ.getAnswer()) == 2) {
            opt2_green.setVisibility(View.VISIBLE);
            opt2_blue.setVisibility(View.INVISIBLE);
            opt2_red.setVisibility(View.INVISIBLE);
        } else if (Integer.parseInt(currentQ.getAnswer()) == 3) {
            opt3_green.setVisibility(View.VISIBLE);
            opt3_blue.setVisibility(View.INVISIBLE);
            opt3_red.setVisibility(View.INVISIBLE);
        } else if (Integer.parseInt(currentQ.getAnswer()) == 4) {
            opt4_green.setVisibility(View.VISIBLE);
            opt4_blue.setVisibility(View.INVISIBLE);
            opt4_red.setVisibility(View.INVISIBLE);
        }
        if (n == 1 && n != Integer.parseInt(currentQ.getAnswer())) {
            opt1_green.setVisibility(View.INVISIBLE);
            opt1_blue.setVisibility(View.INVISIBLE);
            opt1_red.setVisibility(View.VISIBLE);
        } else if (n == 2 && n != Integer.parseInt(currentQ.getAnswer())) {
            opt2_green.setVisibility(View.INVISIBLE);
            opt2_blue.setVisibility(View.INVISIBLE);
            opt2_red.setVisibility(View.VISIBLE);
        } else if (n == 3 && n != Integer.parseInt(currentQ.getAnswer())) {
            opt3_green.setVisibility(View.INVISIBLE);
            opt3_blue.setVisibility(View.INVISIBLE);
            opt3_red.setVisibility(View.VISIBLE);
        } else if (n == 4 && n != Integer.parseInt(currentQ.getAnswer())) {
            opt4_green.setVisibility(View.INVISIBLE);
            opt4_blue.setVisibility(View.INVISIBLE);
            opt4_red.setVisibility(View.VISIBLE);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                visibility();
                button_enabled();
                if (qid < questionCount) {
                    COUNTDOWN_IN_MILLIS = sharedPreferences.getLong("quiz_time", (long) 0.0) * 1000;
                    timeLeftInMillis = COUNTDOWN_IN_MILLIS;
                    startCountDown();
                    currentQ = quesList.get(qid);
                    setQuestionView();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    Bundle b = new Bundle();
                    b.putString("score", score + "");
                    b.putString("quiz_type", quiz_type);
                    b.putString("subject_id", subject);
                    b.putString("code", code);
                    b.putSerializable("answerlist", (Serializable) list); //Your score
                    intent.putExtras(b); //Put your score to your next Intent
                    startActivity(intent);
                    finish();

                }

            }
        }, 3500);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void visibility() {
        opt1_blue.setVisibility(View.VISIBLE);
        opt1_green.setVisibility(View.INVISIBLE);
        opt1_red.setVisibility(View.INVISIBLE);
        opt2_blue.setVisibility(View.VISIBLE);
        opt2_green.setVisibility(View.INVISIBLE);
        opt2_red.setVisibility(View.INVISIBLE);
        opt3_blue.setVisibility(View.VISIBLE);
        opt3_green.setVisibility(View.INVISIBLE);
        opt3_red.setVisibility(View.INVISIBLE);
        opt4_blue.setVisibility(View.VISIBLE);
        opt4_green.setVisibility(View.INVISIBLE);
        opt4_red.setVisibility(View.INVISIBLE);

    }

    public void button_disabled() {
        opt1.setEnabled(false);
        opt2.setEnabled(false);
        opt3.setEnabled(false);
        opt4.setEnabled(false);
    }

    public void button_enabled() {
        opt1.setEnabled(true);
        opt2.setEnabled(true);
        opt3.setEnabled(true);
        opt4.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        ViewDialogExit viewDialogExit = new ViewDialogExit();
        viewDialogExit.showDialog(QuizActivity.this, "", "You won't be able to join this contest further. Do you want to exit ?");

    }

    public class ViewDialogExit {
        public void showDialog(Activity activity, String headerr, String messages) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView header = dialog.findViewById(R.id.dialog_message_header);
            header.setText(headerr);
            TextView message = dialog.findViewById(R.id.dialog_message);
            message.setText(messages);
            FrameLayout mDialogNo = dialog.findViewById(R.id.frmNo);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            FrameLayout mDialogOk = dialog.findViewById(R.id.frmOk);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                }
            });

            dialog.show();
        }
    }

    public class CustomToastQActivity {
        public  Dialog dialog;
        public  void showCustomToast(String messages) {
            dialog = new Dialog(QuizActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_toast);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView message = dialog.findViewById(R.id.dialog_message);
            message.setText(messages);

            FrameLayout mDialogOk = dialog.findViewById(R.id.frmOk);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                }
            });

            dialog.show();
        }
    }


}
