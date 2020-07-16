package xit.zubrein.eexam.challengefriend.adapter;

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
import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;
import xit.zubrein.eexam.challengefriend.ui.MyChallengeParticipantActivity;


public class AcceptedChallengeAdapter extends RecyclerView.Adapter<AcceptedChallengeAdapter.AcceptedChallengeViewHolder> {
    private List<AcceptedChallengeModel.My_challenge> list;
    Context c;
    AcceptedChallengeModel.My_challenge currentitem;
    String code;

    public static class AcceptedChallengeViewHolder extends RecyclerView.ViewHolder{
        public TextView position,code,view_all,total_participant,date,created_by;


        public AcceptedChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            code = itemView.findViewById(R.id.code);
            view_all = itemView.findViewById(R.id.view_all);
            total_participant = itemView.findViewById(R.id.total_participant);
            date = itemView.findViewById(R.id.date);
            created_by = itemView.findViewById(R.id.created_by);

        }
    }

    public AcceptedChallengeAdapter(List<AcceptedChallengeModel.My_challenge> examplelist){
        this.list = examplelist;
    }

    @NonNull
    @Override
    public AcceptedChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.acceptedchallenge_item,parent,false);
        AcceptedChallengeViewHolder evh = new AcceptedChallengeViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AcceptedChallengeViewHolder holder, final int position) {
        currentitem = list.get(position);
//        holder.position.setText(currentitem.getSubject_name());
        holder.position.setText(String.valueOf(position+1));
        holder.code.setText("CODE : "+currentitem.getCode());
        holder.total_participant.setText("  "+currentitem.getTotal_accepted_challenge());
        holder.date.setText("  "+currentitem.getDate()+"");
        holder.created_by.setText("  Created by : "+currentitem.getChallenge_created_by()+"");
        holder.view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, MyChallengeParticipantActivity.class);
                intent.putExtra("list2",(Serializable) list.get(position).getChallengeDetailsList());
                intent.putExtra("code",list.get(position).getCode());
                intent.putExtra("type","accepted_challenge");
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }



}
