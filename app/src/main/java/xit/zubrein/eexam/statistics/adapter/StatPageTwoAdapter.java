package xit.zubrein.eexam.statistics.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.statistics.models.ModelStat;


public class StatPageTwoAdapter extends RecyclerView.Adapter<StatPageTwoAdapter.SatePageTwoViewHolder> {
    private List<ModelStat.Question_list> list;
    Context c;

    public static class SatePageTwoViewHolder extends RecyclerView.ViewHolder{
        public TextView position,question,answer;


        public SatePageTwoViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);

        }
    }

    public StatPageTwoAdapter(List<ModelStat.Question_list> examplelist){
        this.list = examplelist;
    }

    @NonNull
    @Override
    public SatePageTwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_pagetwo_item,parent,false);
        SatePageTwoViewHolder evh = new SatePageTwoViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SatePageTwoViewHolder holder, final int position) {
        ModelStat.Question_list currentitem = list.get(position);
        holder.position.setText(String.valueOf(position+1));
        holder.question.setText(currentitem.getQuestion());
        String status = currentitem.getStatus();
        if(status.equals("1")){
            holder.answer.setTextColor(Color.GREEN);
        }else{
            holder.answer.setTextColor(Color.RED);
        }
        holder.answer.setText(currentitem.getAnswer());


    }

    @Override
    public int getItemCount() {
        return list.size();

    }



}
