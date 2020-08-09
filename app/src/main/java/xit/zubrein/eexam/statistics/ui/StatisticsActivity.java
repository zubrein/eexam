package xit.zubrein.eexam.statistics.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.challengefriend.adapter.SectionsPagerAdapter;
import xit.zubrein.eexam.challengefriend.ui.fragments.FragmentAcceptedChallenge;
import xit.zubrein.eexam.challengefriend.ui.fragments.FragmentMyChallenge;
import xit.zubrein.eexam.statistics.ui.fragments.FragmentOverall;
import xit.zubrein.eexam.statistics.ui.fragments.FragmentRecent;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setupViewPager();

    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentRecent());//index 0
        adapter.addFragment(new FragmentOverall()); //index 1




        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("RECENT");
        tabLayout.getTabAt(1).setText("OVERALL");



    }
}
