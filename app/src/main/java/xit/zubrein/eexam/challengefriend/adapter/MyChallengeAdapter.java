package xit.zubrein.eexam.challengefriend.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryModel;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.ui.MyChallengeParticipantActivity;


public class MyChallengeAdapter extends RecyclerView.Adapter<MyChallengeAdapter.MyChallengeViewHolder> {
    private List<MyChallengeModel.My_challenge> list;
    Context c;
    MyChallengeModel.My_challenge currentitem;
    String code;

    public static class MyChallengeViewHolder extends RecyclerView.ViewHolder{
        public TextView position,code,view_all,total_participant,date;


        public MyChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            code = itemView.findViewById(R.id.code);
            view_all = itemView.findViewById(R.id.view_all);
            total_participant = itemView.findViewById(R.id.total_participant);
            date = itemView.findViewById(R.id.date);

        }
    }

    public MyChallengeAdapter(List<MyChallengeModel.My_challenge> examplelist){
        this.list = examplelist;
    }

    @NonNull
    @Override
    public MyChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mychallenge_item,parent,false);
        MyChallengeViewHolder evh = new MyChallengeViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyChallengeViewHolder holder, final int position) {
        currentitem = list.get(position);
//        holder.position.setText(currentitem.getSubject_name());
        holder.position.setText(String.valueOf(position+1));
        holder.code.setText("CODE : "+currentitem.getCode());
        holder.total_participant.setText("  "+currentitem.getTotal_accepted_challenge());
        holder.date.setText("  "+currentitem.getDate()+"");
        holder.view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, MyChallengeParticipantActivity.class);
                intent.putExtra("list",(Serializable) list.get(position).getChallengeDetailsList());
                intent.putExtra("code",list.get(position).getCode());
                intent.putExtra("type","my_challenge");
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }



}
