package xit.zubrein.eexam.charging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.HomeActivity;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryAdapter;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.quizsection.QuizActivity;
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;
import xit.zubrein.eexam.signinsignup.ui.FormActivity;
import xit.zubrein.eexam.signinsignup.ui.OTPActivity;
import xit.zubrein.eexam.signinsignup.viewmodel.ViewModelSigninSignup;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;

public class PaymentOTPActivity extends AppCompatActivity {

    private PinView pinView;
    private CardView btnSent;
    String msisdn, otp;
    TextView textU, txtMSISDN;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String subject, quiz_type;
    LoadingBar loadingBar;
    String user_id, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_o_t_p);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");

        msisdn = getIntent().getStringExtra("msisdn");
        subject = getIntent().getStringExtra("subject_id");
        quiz_type = getIntent().getStringExtra("quiz_type");

        pinView = findViewById(R.id.pinView);
        btnSent = findViewById(R.id.btnSent);
        textU = findViewById(R.id.textU);
        txtMSISDN = findViewById(R.id.txtMSISDN);
        txtMSISDN.setText("+88" + msisdn + "");

        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otp = pinView.getText().toString();
                if (otp.length() == 4) {

                    check_otp();

                } else {
                    Toast.makeText(PaymentOTPActivity.this, "Please enter a valid otp", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    void check_otp() {
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(PaymentOTPActivity.this);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelSigninSignup> call = service.manual_otp_check(token, msisdn, otp);
        call.enqueue(new Callback<ModelSigninSignup>() {
            @Override
            public void onResponse(Call<ModelSigninSignup> call, Response<ModelSigninSignup> response) {
                if (response.isSuccessful()) {
                    ModelSigninSignup model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        loadingBar.cancelDialog();
                        do_charge(subject);
                    } else {
                        loadingBar.cancelDialog();
                        textU.setVisibility(View.VISIBLE);
                        textU.setText("X incorrect OTP");
                        textU.setTextColor(Color.RED);
                        pinView.setLineColor(Color.RED);
                    }
                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(PaymentOTPActivity.this, "server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(PaymentOTPActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void do_charge(final String subject) {

        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(PaymentOTPActivity.this);

        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelCharging> call = service.one_time_payment(token, msisdn, user_id);
        call.enqueue(new Callback<ModelCharging>() {
            @Override
            public void onResponse(Call<ModelCharging> call, Response<ModelCharging> response) {
                if (response.isSuccessful()) {
                    ModelCharging model = response.body();
                    String data = model.getStatus_code();

                    if (data.equals("200")) {
                        loadingBar.cancelDialog();
                        ViewDialog viewDialog = new ViewDialog();
                        viewDialog.showDialog(PaymentOTPActivity.this, "Payment successful.Continue ?", "success", subject, 200);

                    } else {
                        loadingBar.cancelDialog();
                        CustomToast.showCustomToast(PaymentOTPActivity.this, "You don't have sufficient balance");
                    }

                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(PaymentOTPActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ModelCharging> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(PaymentOTPActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class ViewDialog {
        public void showDialog(Activity activity, String messages, final String type, final String subject, final int code) {
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
                    if (type.equals("subscribe")) {
                        do_charge(subject);
                    } else if (type.equals("success")) {
                        Intent intent = new Intent(PaymentOTPActivity.this, QuizActivity.class);
                        intent.putExtra("subject_id", subject);
                        intent.putExtra("quiz_type", "regular");
                        startActivity(intent);
                    }

                }
            });

            dialog.show();
        }
    }
}
