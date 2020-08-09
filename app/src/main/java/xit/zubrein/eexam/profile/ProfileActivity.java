package xit.zubrein.eexam.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import xit.zubrein.eexam.R;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout layout_msisdn,layout_email,layout_password,layout_city,layout_study;
    TextView change_name,change_msisdn,txt_msisdn,change_email,txt_email,change_password,txt_password,change_study,txt_study,txt_name,change_city,txt_city;
    SharedPreferences sharedPreferences;
    String name,email,msisdn,city,study;
    String user_id,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        email = sharedPreferences.getString("email","");
        msisdn = sharedPreferences.getString("msisdn","");
        city = sharedPreferences.getString("city","");
        study = sharedPreferences.getString("current_study","");
        user_id = sharedPreferences.getString("user_id","");
        token = sharedPreferences.getString("token","");

        txt_email.setText(email);
        if(msisdn.equals("")){
            change_msisdn.setText("ADD");
        }
        txt_msisdn.setText(msisdn);
        txt_name.setText(name);
        txt_city.setText(city);
        txt_study.setText(study);

        change_msisdn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("label","msisdn");
                startActivity(intent);
                
            }
        });
        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("label","name");
                startActivity(intent);
            }
        });
        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("label","email");
                startActivity(intent);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        change_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("label","city");
                startActivity(intent);
            }
        });
        change_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("label","current_study");
                startActivity(intent);
            }
        });


    }

    void init(){
        layout_msisdn = findViewById(R.id.layout_msisdn);
        layout_email = findViewById(R.id.layout_email);
        layout_password = findViewById(R.id.layout_password);
        layout_city = findViewById(R.id.layout_city);
        layout_study = findViewById(R.id.layout_study);
        change_msisdn = findViewById(R.id.change_msisdn);
        txt_msisdn = findViewById(R.id.txt_msisdn);
        change_email = findViewById(R.id.change_email);
        txt_email = findViewById(R.id.txt_email);
        change_password = findViewById(R.id.change_password);
        txt_password = findViewById(R.id.txt_password);
        txt_name = findViewById(R.id.txt_name);
        change_name = findViewById(R.id.change_name);
        change_study = findViewById(R.id.change_study);
        txt_study = findViewById(R.id.txt_study);
        change_city = findViewById(R.id.change_city);
        txt_city = findViewById(R.id.txt_city);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        email = sharedPreferences.getString("email","");
        msisdn = sharedPreferences.getString("msisdn","");
        city = sharedPreferences.getString("city","");
        study = sharedPreferences.getString("current_study","");
        user_id = sharedPreferences.getString("user_id","");
        token = sharedPreferences.getString("token","");

        txt_email.setText(email);
        if(msisdn.equals("")){
            change_msisdn.setText("ADD");
        }
        txt_msisdn.setText(msisdn);
        txt_name.setText(name);
        txt_city.setText(city);
        txt_study.setText(study);
    }
}
