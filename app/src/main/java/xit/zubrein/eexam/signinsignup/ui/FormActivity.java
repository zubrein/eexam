package xit.zubrein.eexam.signinsignup.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.HomeActivity;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.signinsignup.model.ModelRegister;
import xit.zubrein.eexam.signinsignup.viewmodel.ViewModelSigninSignup;
import xit.zubrein.eexam.utils.LoadingBar;

public class FormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    EditText etName, etDistrict, etEmail, etPassword;
    CardView btnRegister;
    List<String> categories;
    String currentStudy = "", msisdn;
    ViewModelSigninSignup viewModel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private static final String TAG = "FormActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        spinner = findViewById(R.id.spinner);
        etName = findViewById(R.id.etName);
        etDistrict = findViewById(R.id.etDistrict);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spinner.setOnItemSelectedListener(this);

        msisdn = getIntent().getStringExtra("msisdn");

        viewModel = new ViewModelProvider(this).get(ViewModelSigninSignup.class);

        initCurrentStudy();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etName.getText().toString();
                final String city = etDistrict.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                if (name.equals("") && city.equals("")) {
                    Toast.makeText(FormActivity.this, "Please fill up the form", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(FormActivity.this, "Insert email", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(FormActivity.this, "Insert password", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(FormActivity.this, "Password should be at lease 6 digit", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(FormActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    register(msisdn, name, email, city, currentStudy);
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

    public class CustomToastHere {
        public Dialog dialog;

        public void showCustomToast(Activity activity, String messages, final String token, final String user_id) {
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
                    editor.putBoolean("login", true);
                    editor.putString("msisdn", msisdn);
                    editor.putString("token", "Bearer " + token);
                    editor.putString("user_id", user_id);
                    editor.apply();
                    finish();
                    startActivity(new Intent(FormActivity.this, HomeActivity.class));
                }
            });

            dialog.show();
        }
    }

    void register(String msisdn, String name, String email, String city, String current_study) {
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(FormActivity.this);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelRegister> call = service.register(msisdn, name, email, city, current_study);
        call.enqueue(new Callback<ModelRegister>() {
            @Override
            public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    final ModelRegister model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                .addOnCompleteListener(FormActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        loadingBar.cancelDialog();
                                                        CustomToastHere customToastHere = new CustomToastHere();
                                                        customToastHere.showCustomToast(FormActivity.this, "A verification email has been sent to email. Email should be verified to login with email and reset your password. ", model.getToken(), model.getUser_id());
                                                    } else {
                                                        loadingBar.cancelDialog();
                                                        Toast.makeText(FormActivity.this, task.getException().getMessage() + "", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(FormActivity.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else if (model.getStatus_code().equals("512")) {
                        loadingBar.cancelDialog();
                        Toast.makeText(FormActivity.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(FormActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelRegister> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(FormActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
