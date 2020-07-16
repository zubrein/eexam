package xit.zubrein.eexam.signinsignup.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelRegister;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;

public class EmailSignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    EditText etName, etDistrict, etEmail, etPasssword;
    CardView btnSignUp;
    List<String> categories;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailSignupActivity";

    String currentStudy;
    LoadingBar loadingBar = new LoadingBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);

        mAuth = FirebaseAuth.getInstance();

        spinner = findViewById(R.id.spinner);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPasssword = findViewById(R.id.etPasssword);
        etDistrict = findViewById(R.id.etDistrict);
        btnSignUp = findViewById(R.id.btnSignUp);
        spinner.setOnItemSelectedListener(this);

        initCurrentStudy();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etName.getText().toString().equals("")) {
                    Toast.makeText(EmailSignupActivity.this, "Please insert your name", Toast.LENGTH_SHORT).show();
                } else if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(EmailSignupActivity.this, "Please insert your email", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                    Toast.makeText(EmailSignupActivity.this, "Insert a valid email", Toast.LENGTH_SHORT).show();
                } else if (etPasssword.getText().toString().equals("")) {
                    Toast.makeText(EmailSignupActivity.this, "Please insert your password", Toast.LENGTH_SHORT).show();
                } else if (etPasssword.getText().toString().length() < 6) {
                    Toast.makeText(EmailSignupActivity.this, "Password should be at least 6 digit", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.showDialog(EmailSignupActivity.this);
                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPasssword.getText().toString())
                            .addOnCompleteListener(EmailSignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    loadingBar.cancelDialog();
                                                    register(etEmail.getText().toString(), etName.getText().toString(),
                                                            etDistrict.getText().toString(), currentStudy);
                                                } else {
                                                    loadingBar.cancelDialog();
                                                    Toast.makeText(EmailSignupActivity.this, task.getException().getMessage() + "", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        loadingBar.cancelDialog();
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(EmailSignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });


    }

    public void initCurrentStudy() {
        categories = new ArrayList<String>();
        categories.add("School");
        categories.add("College");
        categories.add("University");
        categories.add("Job");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        currentStudy = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        currentStudy = "School";
    }

    void register(String email, String name, String city, String current_study) {
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelRegister> call = service.register_email(email, name, city, current_study);
        call.enqueue(new Callback<ModelRegister>() {
            @Override
            public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                if (response.isSuccessful()) {
                    ModelRegister model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        loadingBar.cancelDialog();
                        loadingBar.showDialog(EmailSignupActivity.this);
                        CustomToastHere customToastHere = new CustomToastHere();
                        customToastHere.showCustomToast(EmailSignupActivity.this, "An verification email has been sent to your email. Please check your email");
                    } else {
                        loadingBar.cancelDialog();
                        CustomToastHere customToastHere = new CustomToastHere();
                        customToastHere.showCustomToast(EmailSignupActivity.this, model.getStatus_code());

                    }
                }
            }

            @Override
            public void onFailure(Call<ModelRegister> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(EmailSignupActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
                    finish();
                }
            });

            dialog.show();
        }
    }
}
