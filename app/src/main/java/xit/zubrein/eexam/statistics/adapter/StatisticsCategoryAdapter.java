package xit.zubrein.eexam.statistics.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryModel;
import xit.zubrein.eexam.statistics.ui.StatDetailsPageOne;
import xit.zubrein.eexam.utils.LoadingBar;


public class StatisticsCategoryAdapter extends RecyclerView.Adapter<StatisticsCategoryAdapter.CategoryViewHolder> {
    private List<CategoryModel.subjects> subjectList;
    Context c;
    String user_id = "", token = "";
    SharedPreferences sharedPreferences;
    LoadingBar loadingBar;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSubject;
        public CardView parent;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject = itemView.findViewById(R.id.txtSubject);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public StatisticsCategoryAdapter(List<CategoryModel.subjects> examplelist, Context c) {
        this.subjectList = examplelist;
        this.c = c;
        sharedPreferences = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");
        loadingBar = new LoadingBar();


    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_category_item, parent, false);
        CategoryViewHolder evh = new CategoryViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final CategoryModel.subjects currentitem = subjectList.get(position);
        holder.txtSubject.setText(currentitem.getSubject_name());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, StatDetailsPageOne.class);
                intent.putExtra("subject",currentitem.getId());
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectList.size();

    }

}
