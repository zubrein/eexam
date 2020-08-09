package xit.zubrein.eexam.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.signinsignup.ui.EmailLoginActivity;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText old_pass,new_pass,con_pass;
    Button submit;
    String email;
    SharedPreferences sharedPreferences;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);

        email = sharedPreferences.getString("email","");
        auth = FirebaseAuth.getInstance();

        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        con_pass = findViewById(R.id.con_pass);
        submit = findViewById(R.id.submit);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String oldPass = old_pass.getText().toString();
                final String newPass = new_pass.getText().toString();
                final String conPass = con_pass.getText().toString();

                if(oldPass.equals("") || newPass.equals("") || conPass.equals("")){
                    Toast.makeText(ChangePasswordActivity.this, "Field must not empty", Toast.LENGTH_SHORT).show();
                }else{
                    if(newPass.equals(conPass)){
                        final LoadingBar loadingBar = new LoadingBar();
                        loadingBar.showDialog(ChangePasswordActivity.this);
                        auth.signInWithEmailAndPassword(email, oldPass)
                                .addOnCompleteListener(ChangePasswordActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            loadingBar.cancelDialog();
                                            FirebaseUser user = auth.getCurrentUser();
                                            user.updatePassword(conPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        CustomToast.showCustomToast(ChangePasswordActivity.this,"Password changed successfully");
                                                    }else{
                                                        CustomToast.showCustomToast(ChangePasswordActivity.this,"Password change unsuccessful");
                                                    }

                                                }
                                            });
                                        } else {
                                            loadingBar.cancelDialog();
                                            CustomToast.showCustomToast(ChangePasswordActivity.this,"Old password not matched");
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(ChangePasswordActivity.this, "New Password and Confirm password not matched", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }
}
