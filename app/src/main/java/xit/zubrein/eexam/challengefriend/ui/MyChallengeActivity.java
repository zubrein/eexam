package xit.zubrein.eexam.challengefriend.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.challengefriend.adapter.MyChallengeAdapter;
import xit.zubrein.eexam.challengefriend.adapter.SectionsPagerAdapter;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.ui.fragments.FragmentAcceptedChallenge;
import xit.zubrein.eexam.challengefriend.ui.fragments.FragmentMyChallenge;
import xit.zubrein.eexam.challengefriend.viewmodels.MyChallengeViewModel;

public class MyChallengeActivity extends AppCompatActivity {


    private static final String TAG = "MyChallengeActivity";
    LinearLayout create_challenge,join_challenge;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challenge);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        create_challenge = findViewById(R.id.create_challenge);
        join_challenge = findViewById(R.id.join_challenge);
        create_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyChallengeActivity.this,CreateChallengeActivity.class));
            }
        });
        join_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyChallengeActivity.this,AcceptChallengeActivity.class));
            }
        });

        setupViewPager();
        
    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMyChallenge()); //index 0
        adapter.addFragment(new FragmentAcceptedChallenge());
        //index 2


        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("MY CHALLENGE");
        tabLayout.getTabAt(1).setText("ACCEPTED CHALLENGE");


    }

}
