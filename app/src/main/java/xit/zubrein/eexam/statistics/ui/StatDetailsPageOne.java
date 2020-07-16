package xit.zubrein.eexam.statistics.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryActivity;
import xit.zubrein.eexam.statistics.adapter.StatPageOneAdapter;
import xit.zubrein.eexam.statistics.models.ModelStat;
import xit.zubrein.eexam.statistics.viewmodels.StatViemodel;

public class StatDetailsPageOne extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    StatPageOneAdapter mAdapter;
    StatViemodel viewmodel;
    private static final String TAG = "StateDetailsPageOne";
    String subject;
    RelativeLayout bodyButton;
    Button participate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details_page_one);

        ImageView back = findViewById(R.id.back);
        bodyButton = findViewById(R.id.bodyButton);
        participate = findViewById(R.id.participate);
        recyclerView = findViewById(R.id.recyclerView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        subject = getIntent().getStringExtra("subject");

        viewmodel = new ViewModelProvider(StatDetailsPageOne.this).get(StatViemodel.class);

        viewmodel.getStatDate(subject).observe(StatDetailsPageOne.this, new Observer<List<ModelStat>>() {
            @Override
            public void onChanged(List<ModelStat> list) {
                if(list.size()== 0 ){
                    recyclerView.setVisibility(View.GONE);
                    bodyButton.setVisibility(View.VISIBLE);
                }else{

                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(StatDetailsPageOne.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new StatPageOneAdapter(list);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });

        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatDetailsPageOne.this, CategoryActivity.class));
            }
        });

    }
}
