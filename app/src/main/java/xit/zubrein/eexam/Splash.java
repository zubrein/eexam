package xit.zubrein.eexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.http.Header;
import xit.zubrein.eexam.signinsignup.ui.MSISDNActivity;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean loginstatus = false;
    private static final String TAG = "Splash";
    String url;
    final String app_version = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        FoldingCube foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loginstatus = sharedPreferences.getBoolean("login", false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference appRef = database.getReference("appdata");

        appRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String version = dataSnapshot.child("version").child(app_version).getValue().toString();
                url = dataSnapshot.child("url").getValue().toString();
                String otb_message = dataSnapshot.child("otb_message").getValue().toString();
                String subscribe_message = dataSnapshot.child("subscribe_message").getValue().toString();
                String base_url = dataSnapshot.child("base_url").getValue().toString();
                editor.putString("app_link",url);
                editor.putString("otb_message",otb_message);
                editor.putString("base_url",base_url);
                editor.putString("otb_message",otb_message);
                editor.putString("subscribe_message",subscribe_message);
                editor.putLong("quiz_time",Long.parseLong(dataSnapshot.child("quiz_time").getValue().toString()));
                editor.apply();
                if (version.equals("0")) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loginstatus) {
                                startActivity(new Intent(Splash.this, HomeActivity.class));
                                finish();

                            } else {
                                startActivity(new Intent(Splash.this, MSISDNActivity.class));
                                finish();
                            }
                        }
                    }, 2000);

                } else if (version.equals("1")) {
                    ViewDialog viewDialog = new ViewDialog();
                    viewDialog.showDialog("1");
                } else if (version.equals("2")) {
                    ViewDialog viewDialog = new ViewDialog();
                    viewDialog.showDialog("2");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public class ViewDialog {
        public void showDialog(String type) {
            final Dialog dialog = new Dialog(Splash.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.update_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView header = dialog.findViewById(R.id.dialog_message_header);
            TextView message = dialog.findViewById(R.id.dialog_message);
            TextView update = dialog.findViewById(R.id.update);
            TextView later = dialog.findViewById(R.id.later);

            if (type.equals("2")) {
                header.setText("Update Alert !!");
                message.setText("New version of E-Exam is live now. Please update your app to see new features.");
            } else if (type.equals("1")) {
                header.setText("Maintenance Alert !!");
                message.setText("This app is under maintenance. Please return after a few moment");
                update.setVisibility(View.GONE);
                later.setText("OK");
            }
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    finish();
                }
            });
            later.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            dialog.show();
        }
    }
}
