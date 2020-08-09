package xit.zubrein.eexam.signinsignup.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.HomeActivity;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;
import xit.zubrein.eexam.signinsignup.viewmodel.ViewModelSigninSignup;
import xit.zubrein.eexam.utils.LoadingBar;

public class MSISDNActivity extends AppCompatActivity {

    EditText etMSISDN;
    CardView btnSentOTP;
    String TAG = "MSISDNActivity";
    SharedPreferences sharedPreferences;
    boolean loginstatus = false;
    TextView signinwithemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_s_i_s_d_n);

        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);


        etMSISDN = findViewById(R.id.etMSISDN);
        btnSentOTP = findViewById(R.id.btnSentOTP);
        signinwithemail = findViewById(R.id.signinwithemail);

        btnSentOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msisdn = etMSISDN.getText().toString();
                if(msisdn.startsWith("018") || msisdn.startsWith("016") && msisdn.length() == 11){
                 send_otp(msisdn);
                }else{
                    Toast.makeText(MSISDNActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }


            }
        });

        signinwithemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MSISDNActivity.this,EmailLoginActivity.class));
            }
        });

    }

    public void send_otp(String msisdn){
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(MSISDNActivity.this);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelSigninSignup> call = service.send_otp(msisdn);
        call.enqueue(new Callback<ModelSigninSignup>() {
            @Override
            public void onResponse(Call<ModelSigninSignup> call, Response<ModelSigninSignup> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    ModelSigninSignup model = response.body();
                    if(model.getStatus_code().equals("200")){
                        Intent intent = new Intent(MSISDNActivity.this,OTPActivity.class);
                        intent.putExtra("msisdn",etMSISDN.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(MSISDNActivity.this, "Network fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
