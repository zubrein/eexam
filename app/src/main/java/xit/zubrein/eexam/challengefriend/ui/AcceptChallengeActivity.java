package xit.zubrein.eexam.challengefriend.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import xit.zubrein.eexam.quizsection.QuizActivity;
import xit.zubrein.eexam.R;

public class AcceptChallengeActivity extends AppCompatActivity {

    EditText et_code;
    Button btn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_challenge);

        et_code = findViewById(R.id.et_code);
        btn_enter = findViewById(R.id.btn_enter);

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et_code.getText().toString().trim();
                if (code.equals("") || code.length() != 6){
                    Toast.makeText(AcceptChallengeActivity.this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(AcceptChallengeActivity.this, QuizActivity.class);
                    intent.putExtra("code",code);
                    intent.putExtra("quiz_type","challenge");
                    startActivity(intent);
                }
            }
        });

    }
}
