package xit.zubrein.eexam.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.victor.loading.newton.NewtonCradleLoading;

import java.util.List;

import xit.zubrein.eexam.R;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mAdapter;
    CategoryViewmodel viewmodel;
    private static final String TAG = "CategoryActivity";
    LinearLayout loading;
    NewtonCradleLoading newtonCradleLoading;
    TextView top_text;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        top_text = findViewById(R.id.top_text);
        newtonCradleLoading = findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();

        viewmodel = new ViewModelProvider(CategoryActivity.this).get(CategoryViewmodel.class);

        viewmodel.getSubjects().observe(CategoryActivity.this, new Observer<List<CategoryModel.subjects>>() {
            @Override
            public void onChanged(List<CategoryModel.subjects> subjects) {
                loading.setVisibility(View.GONE);
                top_text.setVisibility(View.VISIBLE);
                Log.d(TAG, "onChanged: "+subjects.size());
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(CategoryActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new CategoryAdapter(subjects,CategoryActivity.this,CategoryActivity.this);
                recyclerView.setAdapter(mAdapter);
            }
        });



    }
}
