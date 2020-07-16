package xit.zubrein.eexam.challengefriend.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryModel;
import xit.zubrein.eexam.challengefriend.adapter.CreateChallengeAdapter;
import xit.zubrein.eexam.challengefriend.model.CreateChallengeModel;
import xit.zubrein.eexam.challengefriend.model.CreateChallengeSubjectModel;
import xit.zubrein.eexam.challengefriend.model.RecieveCodeModel;
import xit.zubrein.eexam.challengefriend.viewmodels.CreateChallengeViewmodel;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;


public class CreateChallengeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CreateChallengeViewmodel viewmodel;
    CreateChallengeViewmodel viewmodel2;
    private static final String TAG = "CreateChallengeActivity";
    CreateChallengeAdapter mAdapter;
    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<String> subjectIDList = new ArrayList<>();
    Button btn_create;
    List<CreateChallengeSubjectModel> createChallengeSubjectModelList = new ArrayList<>();
    String token = "", user_id = "";
    SharedPreferences sharedPreferences;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        btn_create = findViewById(R.id.btn_create);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");

        viewmodel = new ViewModelProvider(CreateChallengeActivity.this).get(CreateChallengeViewmodel.class);


        viewmodel.getSubjects().observe(CreateChallengeActivity.this, new Observer<List<CategoryModel.subjects>>() {
            @Override
            public void onChanged(List<CategoryModel.subjects> subjects) {

                Log.d(TAG, "onChanged: " + subjects.size());
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(CreateChallengeActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new CreateChallengeAdapter(subjects);
                recyclerView.setAdapter(mAdapter);
                for (int i = 0; i < subjects.size(); i++) {
                    subjectIDList.add(subjects.get(i).getId());
                }

            }
        });


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_data(mAdapter.return_list());
            }
        });

    }

    CreateChallengeSubjectModel createChallengeSubjectModel;

    public void send_data(ArrayList<Integer> list) {
        createChallengeSubjectModelList = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            count = count + list.get(i);
        }
        if (count == 10) {
            for (int i = 0; i < subjectIDList.size(); i++) {
                createChallengeSubjectModel = new CreateChallengeSubjectModel(subjectIDList.get(i), list.get(i));
                createChallengeSubjectModelList.add(createChallengeSubjectModel);
            }
            send_data();
        } else {
            Toast.makeText(this, "Please select 10 questions", Toast.LENGTH_SHORT).show();
        }


    }

    public void send_data() {
        final CreateChallengeModel model = new CreateChallengeModel(user_id, createChallengeSubjectModelList);
//        viewmodel2 = new ViewModelProvider(CreateChallengeActivity.this).get(CreateChallengeViewmodel.class);
//        viewmodel.get_code(model).observe(CreateChallengeActivity.this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                viewmodel.get_code(model).removeObservers(CreateChallengeActivity.this);

//            }
//        });
        final LoadingBar loadingBar = new LoadingBar() ;
        loadingBar.showDialog(CreateChallengeActivity.this);

        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<RecieveCodeModel> call = service.get_code(token, model);
        call.enqueue(new Callback<RecieveCodeModel>() {
            @Override
            public void onResponse(Call<RecieveCodeModel> call, Response<RecieveCodeModel> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    RecieveCodeModel model = response.body();
                    ShareCodeMessage shareCodeMessage = new ShareCodeMessage();
                    shareCodeMessage.showCustomToast(model.getCode());

                }else{
                    loadingBar.cancelDialog();
                    CustomToast.showCustomToast(CreateChallengeActivity.this,"Server error");
                }
            }

            @Override
            public void onFailure(Call<RecieveCodeModel> call, Throwable t) {
                loadingBar.cancelDialog();
            }
        });
    }


    public class ShareCodeMessage {
        public Dialog dialog;
        public void showCustomToast(final String code) {
            dialog = new Dialog(CreateChallengeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.customtoastsharecode);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView message = dialog.findViewById(R.id.dialog_message);
            TextView send_code = dialog.findViewById(R.id.send_code);
            message.setText("Your code is : "+code);

            TextView mDialogCAncel = dialog.findViewById(R.id.cancel);
            mDialogCAncel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            send_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "FCM");
                    String sAux = "\nAccept my challenge\n"+"\nCode: "+code;
                    sAux = sAux+ "\n\nE-Exam\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            });
            dialog.show();
        }
    }

}
