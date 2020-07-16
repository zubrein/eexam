package xit.zubrein.eexam.challengefriend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;
import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;


public class AcceptedChallengeParticipantAdapter extends RecyclerView.Adapter<AcceptedChallengeParticipantAdapter.AcceptedChallengeParticipantViewHolder> {
    private List<AcceptedChallengeModel.My_challenge.Challenge_details> list;
    Context c;

    public static class AcceptedChallengeParticipantViewHolder extends RecyclerView.ViewHolder{
        public TextView position,name,score;


        public AcceptedChallengeParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);

        }
    }

    public AcceptedChallengeParticipantAdapter(List<AcceptedChallengeModel.My_challenge.Challenge_details> examplelist){
        this.list = examplelist;
    }

    @NonNull
    @Override
    public AcceptedChallengeParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_challenge_participant_item,parent,false);
        AcceptedChallengeParticipantViewHolder evh = new AcceptedChallengeParticipantViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AcceptedChallengeParticipantViewHolder holder, final int position) {
        final AcceptedChallengeModel.My_challenge.Challenge_details currentitem = list.get(position);
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
