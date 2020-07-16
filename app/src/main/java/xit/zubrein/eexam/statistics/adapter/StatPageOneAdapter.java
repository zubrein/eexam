package xit.zubrein.eexam.statistics.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.statistics.models.ModelStat;
import xit.zubrein.eexam.statistics.ui.StatDetailsPageOne;
import xit.zubrein.eexam.statistics.ui.StatDetailsPageTwo;


public class StatPageOneAdapter extends RecyclerView.Adapter<StatPageOneAdapter.SatePageOneViewHolder> {
    private List<ModelStat> list;
    Context c;
    ModelStat currentitem;

    public static class SatePageOneViewHolder extends RecyclerView.ViewHolder{
        public TextView position,date,total_question,total_right_answer,details;


        public SatePageOneViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            date = itemView.findViewById(R.id.date);
            total_question = itemView.findViewById(R.id.total_question);
            total_right_answer = itemView.findViewById(R.id.total_right_answer);
            details = itemView.findViewById(R.id.details);

        }
    }

    public StatPageOneAdapter(List<ModelStat> examplelist){
        this.list = examplelist;
    }

    @NonNull
    @Override
    public SatePageOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_pageone_item,parent,false);
        SatePageOneViewHolder evh = new SatePageOneViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SatePageOneViewHolder holder, final int position) {
        currentitem = list.get(position);
//        holder.position.setText(currentitem.getSubject_name());
        holder.position.setText(String.valueOf(position+1));
        holder.date.setText(" DATE :  "+currentitem.getDate());
        holder.total_question.setText(" Total Question : "+currentitem.getTotal_question());

        float percent = (Float.parseFloat(currentitem.getTotal_right_answer()) / Float.parseFloat(currentitem.getTotal_question())) * 100;
        String text = String.format("%.2f", percent);
        holder.total_right_answer.setText(" percentage of Correct Answer  "+text+"%");
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, StatDetailsPageTwo.class);
                intent.putExtra("list",(Serializable) list.get(position).getList());
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }



}
