package xit.zubrein.eexam.charging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;
import xit.zubrein.eexam.signinsignup.ui.MSISDNActivity;
import xit.zubrein.eexam.signinsignup.ui.OTPActivity;
import xit.zubrein.eexam.utils.LoadingBar;

public class PaymentActivity extends AppCompatActivity {

    EditText etMSISDN;
    CardView btnSentOTP;
    String subject,quiz_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        subject = getIntent().getStringExtra("subject_id");
        quiz_type = getIntent().getStringExtra("quiz_type");

        etMSISDN = findViewById(R.id.etMSISDN);
        btnSentOTP = findViewById(R.id.btnSentOTP);

        btnSentOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msisdn = etMSISDN.getText().toString();
                if(msisdn.startsWith("018") || msisdn.startsWith("016") && msisdn.length() == 11){
                    send_otp(msisdn);
                }else{
                    Toast.makeText(PaymentActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void send_otp(final String msisdn){
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(PaymentActivity.this);
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
                        Intent intent = new Intent(PaymentActivity.this, PaymentOTPActivity.class);
                        intent.putExtra("msisdn",msisdn);
                        intent.putExtra("subject_id", subject);
                        intent.putExtra("quiz_type", "regular");
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(PaymentActivity.this, "Network fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
