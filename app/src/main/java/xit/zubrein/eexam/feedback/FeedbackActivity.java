package xit.zubrein.eexam.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelRegister;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;

public class FeedbackActivity extends AppCompatActivity {

    EditText etFeedback;
    TextView rate_us;
    Button send;
    SharedPreferences sharedPreferences;
    String user_id = "",token = "",app_link= "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        
        etFeedback = findViewById(R.id.etFeedback);
        rate_us = findViewById(R.id.rate_us);
        send = findViewById(R.id.send);

        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");
        token = sharedPreferences.getString("token","");
        app_link = sharedPreferences.getString("app_link","");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = etFeedback.getText().toString();
                if(feedback.equals("")){
                    Toast.makeText(FeedbackActivity.this, "Please write something", Toast.LENGTH_SHORT).show();
                }else{
                    send_data(feedback);
                }
            }
        });

        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app_link)));
            }
        });

        
    }
    
    
    void send_data(String data) {
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(FeedbackActivity.this);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelRegister> call = service.send_feedback(token,user_id,data);
        call.enqueue(new Callback<ModelRegister>() {
            @Override
            public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    final ModelRegister model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        CustomToast.showCustomToast(FeedbackActivity.this,"Thanks for your valuable feedback");
                    }

                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(FeedbackActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelRegister> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(FeedbackActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
}
