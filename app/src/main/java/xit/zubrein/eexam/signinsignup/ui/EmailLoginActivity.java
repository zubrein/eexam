package xit.zubrein.eexam.signinsignup.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.HomeActivity;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelEmailLogin;
import xit.zubrein.eexam.signinsignup.model.ModelEmailLogin;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;

public class EmailLoginActivity extends AppCompatActivity {

    CardView btnSignUP, btnSignIN;
    EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailLoginActivity";
    LoadingBar loadingBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new LoadingBar();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnSignIN = findViewById(R.id.btnSignIN);
        btnSignUP = findViewById(R.id.btnSignUP);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingBar.showDialog(EmailLoginActivity.this);

                final String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.equals("") && !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                    Toast.makeText(EmailLoginActivity.this, "Please insert a valid email address", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(EmailLoginActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loadingBar.cancelDialog();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user.isEmailVerified()) {
                                            get_id(email);
                                        } else {
                                            CustomToastHere customToastHere = new CustomToastHere();
                                            customToastHere.showCustomToast(EmailLoginActivity.this, "Your account is not verified yet . Please check your email.");
                                        }

                                    } else {
                                        loadingBar.cancelDialog();
                                        CustomToastHere customToastHere = new CustomToastHere();
                                        customToastHere.showCustomToast(EmailLoginActivity.this, task.getException().getMessage()+"");


                                    }
                                }
                            });
                }

            }
        });


        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLoginActivity.this, EmailSignupActivity.class));
            }
        });

    }

    void get_id(String email) {
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelEmailLogin> call = service.get_user_id_by_email(email);
        call.enqueue(new Callback<ModelEmailLogin>() {
            @Override
            public void onResponse(Call<ModelEmailLogin> call, Response<ModelEmailLogin> response) {
                if (response.isSuccessful()) {
                    ModelEmailLogin s = response.body();
                    if (s.getStatus_code().equals("200")) {
                        loadingBar.cancelDialog();
                        editor.putBoolean("login", true);
                        editor.putString("token", "Bearer " + s.getToken());
                        editor.putString("user_id", s.getUser_id());
                        editor.apply();
                        Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EmailLoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        loadingBar.cancelDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelEmailLogin> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(EmailLoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CustomToastHere {
        public Dialog dialog;

        public void showCustomToast(Activity activity, String messages) {
            dialog = new Dialog(activity);
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
                }
            });

            dialog.show();
        }
    }
}
