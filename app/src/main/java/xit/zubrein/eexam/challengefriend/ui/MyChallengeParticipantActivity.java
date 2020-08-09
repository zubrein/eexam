package xit.zubrein.eexam.challengefriend.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryAdapter;
import xit.zubrein.eexam.challengefriend.adapter.AcceptedChallengeAdapter;
import xit.zubrein.eexam.challengefriend.adapter.AcceptedChallengeParticipantAdapter;
import xit.zubrein.eexam.challengefriend.adapter.MyChallengeAdapter;
import xit.zubrein.eexam.challengefriend.adapter.MyChallengeParticipantAdapter;
import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.model.RecieveCodeModel;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.quizsection.QuizActivity;
import xit.zubrein.eexam.quizsection.model.QuestionsModel;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.CustomToastExit;
import xit.zubrein.eexam.utils.LoadingBar;

public class MyChallengeParticipantActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyChallengeParticipantAdapter mAdapter;
    AcceptedChallengeParticipantAdapter mAdapter2;
    List<MyChallengeModel.My_challenge.Challenge_details> list;
    List<AcceptedChallengeModel.My_challenge.Challenge_details> list2;
    LinearLayout share_code,copy_code;
    String code,token;
    RelativeLayout body,bodyButton;
    Button invite_friend;
    String type;
    TextView delete;
    SharedPreferences sharedPreferences;
    String app_link ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challenge_participant);

        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        app_link = sharedPreferences.getString("app_link","");

        share_code = findViewById(R.id.share_code);
        copy_code = findViewById(R.id.copy_code);
        body = findViewById(R.id.body);
        invite_friend = findViewById(R.id.invite_friend);
        bodyButton = findViewById(R.id.bodyButton);
        delete = findViewById(R.id.delete);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        code = getIntent().getStringExtra("code");
        type = getIntent().getStringExtra("type");
        if(type.equals("my_challenge")){
            list = (List<MyChallengeModel.My_challenge.Challenge_details>) getIntent().getSerializableExtra("list");
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(MyChallengeParticipantActivity.this);
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MyChallengeParticipantAdapter(list);
            recyclerView.setAdapter(mAdapter);
            if(list.size() == 0){
                delete.setVisibility(View.VISIBLE);
                body.setVisibility(View.GONE);
                bodyButton.setVisibility(View.VISIBLE);
            }
        }else if(type.equals("accepted_challenge")) {
            list2 = (List<AcceptedChallengeModel.My_challenge.Challenge_details>) getIntent().getSerializableExtra("list2");
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(MyChallengeParticipantActivity.this);
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter2 = new AcceptedChallengeParticipantAdapter(list2);
            recyclerView.setAdapter(mAdapter2);
        }




        share_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "E-EXAM");
                String sAux = "\nYou are invited on my e-exam challenge\n"+"\nCode: "+code;
                sAux = sAux+ "\nLet's see how much you score !! \n";
                sAux = sAux+ "\n\nDownload E-EXAM app on Google Playstore\n";
                sAux = sAux+ app_link+"\n";
                intent.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "E-EXAM");
                String sAux = "\nYou are invited on my e-exam challenge\n"+"\nCode: "+code;
                sAux = sAux+ "\nLet's see how much you score !! \n";
                sAux = sAux+ "\n\nDownload E-EXAM app on Google Playstore\n";
                sAux = sAux+ app_link+"\n";
                intent.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });



        copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager)   getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", code);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MyChallengeParticipantActivity.this, "Code copied", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialog viewDialog = new ViewDialog();
                viewDialog.showDialog(MyChallengeParticipantActivity.this,"Do you want to delete this challenge ?");
            }
        });

    }


    public void delete_challenge(){
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(MyChallengeParticipantActivity.this);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<RecieveCodeModel> call = service.delete_challenge(token, code);
        call.enqueue(new Callback<RecieveCodeModel>() {
            @Override
            public void onResponse(Call<RecieveCodeModel> call, Response<RecieveCodeModel> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    RecieveCodeModel model = response.body();
                    if(model.getStatus_code().equals("200")){
                        CustomToastExit.showCustomToast(MyChallengeParticipantActivity.this,"Your challenge hase been deleted successfully. Please create a new one and challenge your friends.");
                    }else{
                        Toast.makeText(MyChallengeParticipantActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(MyChallengeParticipantActivity.this, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecieveCodeModel> call, Throwable t) {

            }
        });
    }

    public class ViewDialog {
        public void showDialog(Activity activity, String messages) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView header = dialog.findViewById(R.id.dialog_message_header);
            header.setText("");
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
                    delete_challenge();

                }
            });

            dialog.show();
        }
    }
}
