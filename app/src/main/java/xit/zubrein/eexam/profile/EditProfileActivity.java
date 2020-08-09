package xit.zubrein.eexam.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import java.text.NumberFormat;
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
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;
import xit.zubrein.eexam.signinsignup.ui.FormActivity;
import xit.zubrein.eexam.signinsignup.ui.MSISDNActivity;
import xit.zubrein.eexam.signinsignup.ui.OTPActivity;
import xit.zubrein.eexam.utils.LoadingBar;

public class EditProfileActivity extends AppCompatActivity {


    String label, user_id, token;
    TextView txt_label,textU;
    EditText input;
    Button submit,submit_otp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout layout_input, layout_study,layout_main,layout_otp;
    List<String> categories= new ArrayList<String>();
    Spinner spinner;
    String currentStudy = "",otp;
    private PinView pinView;
    String input_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");

        submit = findViewById(R.id.submit);
        submit = findViewById(R.id.submit);
        txt_label = findViewById(R.id.txt_label);
        input = findViewById(R.id.input);
        layout_study = findViewById(R.id.layout_study);
        layout_input = findViewById(R.id.layout_input);
        spinner = findViewById(R.id.spinner);
        layout_main = findViewById(R.id.layout_main);
        layout_otp = findViewById(R.id.layout_otp);
        pinView = findViewById(R.id.pinView);
        submit_otp = findViewById(R.id.submit_otp);
        textU = findViewById(R.id.textU);


        label = getIntent().getStringExtra("label");
        if (label.equals("name")) {
            txt_label.setText("Enter your name");
        } else if (label.equals("email")) {
            txt_label.setText("Enter your email");
        } else if (label.equals("city")) {
            txt_label.setText("Enter your city");
        } else if (label.equals("current_study")) {
            layout_input.setVisibility(View.GONE);
            layout_study.setVisibility(View.VISIBLE);
            txt_label.setText("Select your current study");
        } else if (label.equals("msisdn")) {
            txt_label.setText("Enter your ROBI/AIRTEL number");
            input.setHint("018******");
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }

        initCurrentStudy();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentStudy = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentStudy = "School";
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_data = input.getText().toString();
                if (label.equals("name") || label.equals("city")) {
                    if (input_data.equals("")) {
                        Toast.makeText(EditProfileActivity.this, "Field must not empty", Toast.LENGTH_SHORT).show();
                    } else {
                        send_data(input_data);
                    }
                } else if (label.equals("current_study")) {
                    send_data(currentStudy);
                }else if (label.equals("msisdn")) {
                    if(input_data.length() != 11){
                        Toast.makeText(EditProfileActivity.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                    }else{
                        send_otp(input_data);
                    }
                }


            }
        });

        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = pinView.getText().toString();
                if(otp.length() !=4){
                    Toast.makeText(EditProfileActivity.this, "Please enter a valid otp", Toast.LENGTH_SHORT).show();
                }else{
                    check_otp(input_data);
                }
            }
        });

    }

    void send_data(final String input) {
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(EditProfileActivity.this);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelRegister> call = null;
        if (label.equals("name")) {
            call = service.edit_name(token, input, user_id);
        } else if (label.equals("city")) {
            call = service.edit_city(token, input, user_id);
        } else if (label.equals("current_study")) {
            call = service.edit_current_study(token, input, user_id);
        } else if (label.equals("email")) {
            call = service.edit_email(token, input, user_id);
        } else if (label.equals("msisdn")) {
            call = service.edit_msisdn(token, input, user_id);
        }

        call.enqueue(new Callback<ModelRegister>() {
            @Override
            public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    final ModelRegister model = response.body();
                    if (model.getStatus_code().equals("200")) {
                        editor.putString(label, input);
                        editor.apply();
                        Toast.makeText(EditProfileActivity.this, "successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (model.getStatus_code().equals("512")) {
                        loadingBar.cancelDialog();
                        Toast.makeText(EditProfileActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(EditProfileActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelRegister> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(EditProfileActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initCurrentStudy() {
        categories.add("School");
        categories.add("College");
        categories.add("University");
        categories.add("Job");
    }

    public void send_otp(String msisdn){
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(EditProfileActivity.this);
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
                        layout_main.setVisibility(View.GONE);
                        layout_otp.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(EditProfileActivity.this, "OTP send failed.please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(EditProfileActivity.this, "Network fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void check_otp(String msisdn){
        final LoadingBar loadingBar = new LoadingBar();
        loadingBar.showDialog(EditProfileActivity.this);
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
                            send_data(input_data);
                        }else{
                            loadingBar.cancelDialog();
                            Toast.makeText(EditProfileActivity.this, "Mobile number already registered", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditProfileActivity.this, "check otp server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelSigninSignup> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(EditProfileActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
