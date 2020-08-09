package xit.zubrein.eexam.signinsignup.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

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

public class OTPActivity extends AppCompatActivity {

    private PinView pinView;
    private CardView btnSent;

    ViewModelSigninSignup viewModel;
    String msisdn, otp;
    TextView textU,txtMSISDN;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewModel = new ViewModelProvider(this).get(ViewModelSigninSignup.class);

        msisdn = getIntent().getStringExtra("msisdn");

        pinView = findViewById(R.id.pinView);
        btnSent = findViewById(R.id.btnSent);
        textU = findViewById(R.id.textU);
        txtMSISDN = findViewById(R.id.txtMSISDN);
        txtMSISDN.setText("+88"+msisdn+"");

        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otp = pinView.getText().toString();
                if (otp.length() == 4) {

                    check_otp();

                } else {
                    Toast.makeText(OTPActivity.this, "Please enter a valid otp", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OTPActivity.this,MSISDNActivity.class));
        finish();
    }

    void check_otp(){
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(OTPActivity.this);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelSigninSignup> call = service.confirm_otp(msisdn,otp);
        call.enqueue(new Callback<ModelSigninSignup>() {
            @Override
            public void onResponse(Call<ModelSigninSignup> call, Response<ModelSigninSignup> response) {
                if (response.isSuccessful()) {
                    ModelSigninSignup model = response.body();
                    if(model.getStatus_code().equals("200")){
                        if(model.getRegistered().equals("no")){
                            loadingBar.cancelDialog();
                            finish();
                            Intent intent = new Intent(OTPActivity.this,FormActivity.class);
                            intent.putExtra("msisdn",msisdn);
                            startActivity(intent);
                        }else{
                            loadingBar.cancelDialog();
                            editor.putBoolean("login",true);
                            editor.putString("token","Bearer "+model.getToken());
                            editor.putString("user_id",model.getUser_id());
                            editor.apply();
                            startActivity(new Intent(OTPActivity.this, HomeActivity.class));
                            finish();
                        }
                    }else{
                        loadingBar.cancelDialog();
                        textU.setVisibility(View.VISIBLE);
                        textU.setText("X incorrect OTP");
                        textU.setTextColor(Color.RED);
                        pinView.setLineColor(Color.RED);
                    }
                }else{
                    loadingBar.cancelDialog();
                }
            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(OTPActivity.this, "server error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
