package xit.zubrein.eexam.statistics.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryActivity;
import xit.zubrein.eexam.challengefriend.adapter.MyChallengeParticipantAdapter;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.ui.MyChallengeParticipantActivity;
import xit.zubrein.eexam.statistics.adapter.StatPageTwoAdapter;
import xit.zubrein.eexam.statistics.models.ModelStat;

public class StatDetailsPageTwo extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<ModelStat.Question_list> list;
    StatPageTwoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_details_page_two);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list = (List<ModelStat.Question_list>) getIntent().getSerializableExtra("list");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(StatDetailsPageTwo.this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StatPageTwoAdapter(list);
        recyclerView.setAdapter(mAdapter);


    }
}
