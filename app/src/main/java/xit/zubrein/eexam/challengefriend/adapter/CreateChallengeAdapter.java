package xit.zubrein.eexam.challengefriend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryModel;


public class CreateChallengeAdapter extends RecyclerView.Adapter<CreateChallengeAdapter.CreateChallengeViewHolder> {
    private List<CategoryModel.subjects> subjectList;
    Context c;
    ArrayList<Integer> question_count = new ArrayList<>();

    public static class CreateChallengeViewHolder extends RecyclerView.ViewHolder{
        public TextView position,txt_subjects,count;
        public Button decrease,increase;


        public CreateChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            txt_subjects = itemView.findViewById(R.id.txt_subjects);
            count = itemView.findViewById(R.id.count);
            decrease = itemView.findViewById(R.id.decrease);
            increase = itemView.findViewById(R.id.increase);
        }
    }

    public CreateChallengeAdapter(List<CategoryModel.subjects> examplelist){
        this.subjectList = examplelist;
        for (int i = 0; i<100 ; i++){
            question_count.add(0);
        }
    }

    @NonNull
    @Override
    public CreateChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        c = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.createchallengecategory_item,parent,false);
        CreateChallengeViewHolder evh = new CreateChallengeViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CreateChallengeViewHolder holder, final int position) {
        final CategoryModel.subjects currentitem = subjectList.get(position);
        holder.txt_subjects.setText(currentitem.getSubject_name());
        holder.position.setText(String.valueOf(position+1));
        holder.count.setText(question_count.get(position)+"");
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question_count.get(position) > 0){
                    question_count.set(position,question_count.get(position)-1);
                    holder.count.setText(question_count.get(position)+"");
                }
            }
        });

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question_count.set(position,question_count.get(position)+1);
                holder.count.setText(question_count.get(position)+"");
            }
        });


    }

    @Override
    public int getItemCount() {
        return subjectList.size();

    }

    public ArrayList<Integer> return_list(){
        return question_count;
    }

}
