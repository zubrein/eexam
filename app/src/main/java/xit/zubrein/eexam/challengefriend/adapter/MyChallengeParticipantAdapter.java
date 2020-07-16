package xit.zubrein.eexam.challengefriend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;


public class MyChallengeParticipantAdapter extends RecyclerView.Adapter<MyChallengeParticipantAdapter.MyChallengeParticipantViewHolder> {
    private List<MyChallengeModel.My_challenge.Challenge_details> list;
    Context c;

    public static class MyChallengeParticipantViewHolder extends RecyclerView.ViewHolder{
        public TextView position,name,score;


        public MyChallengeParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);

        }
    }

    public MyChallengeParticipantAdapter(List<MyChallengeModel.My_challenge.Challenge_details> examplelist){
        this.list = examplelist;
    }

    @NonNull
    @Override
    public MyChallengeParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_challenge_participant_item,parent,false);
        MyChallengeParticipantViewHolder evh = new MyChallengeParticipantViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyChallengeParticipantViewHolder holder, final int position) {
        final MyChallengeModel.My_challenge.Challenge_details currentitem = list.get(position);
//        holder.position.setText(currentitem.getSubject_name());
        holder.position.setText(String.valueOf(position+1));
        holder.name.setText(currentitem.getName());
        holder.score.setText(currentitem.getScore());

    }

    @Override
    public int getItemCount() {
        return list.size();

    }


}
