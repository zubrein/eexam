package xit.zubrein.eexam.signinsignup.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }else{
                    final LoadingBar loadingBar = new LoadingBar();
                    loadingBar.showDialog(ForgotPasswordActivity.this);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loadingBar.cancelDialog();
                                    if (task.isSuccessful()) {
                                        CustomToast.showCustomToast(ForgotPasswordActivity.this,"An email has been sent to "+email.getText().toString()+". Please check your email.");
                                    }else{
                                        CustomToast.showCustomToast(ForgotPasswordActivity.this,"This email is not registered. Please check your email");
                                    }
                                }
                            });
                }

            }
        });

    }
}
