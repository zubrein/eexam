package xit.zubrein.eexam.challengefriend.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.challengefriend.adapter.AcceptedChallengeAdapter;
import xit.zubrein.eexam.challengefriend.adapter.MyChallengeAdapter;
import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.ui.AcceptChallengeActivity;
import xit.zubrein.eexam.challengefriend.ui.MyChallengeActivity;
import xit.zubrein.eexam.challengefriend.viewmodels.MyChallengeViewModel;

public class FragmentAcceptedChallenge extends Fragment {

    SharedPreferences sharedPreferences;
    String user_id, token;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AcceptedChallengeAdapter mAdapter;
    MyChallengeViewModel viewmodel;

    private static final String TAG = "FragmentAcceptChallenge";

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_accepted_challenge, container, false);

        sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");

        viewmodel = new ViewModelProvider(getActivity()).get(MyChallengeViewModel.class);

        final LinearLayout body = view.findViewById(R.id.body);
        Button btn_enter = view.findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AcceptChallengeActivity.class));
            }
        });


        viewmodel.get_accepted_challenge().observe(getActivity(), new Observer<List<AcceptedChallengeModel.My_challenge>>() {
            @Override
            public void onChanged(List<AcceptedChallengeModel.My_challenge> my_challengeList) {
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                if(my_challengeList != null){
                    mAdapter = new AcceptedChallengeAdapter(my_challengeList);
                    recyclerView.setAdapter(mAdapter);
                    body.setVisibility(View.GONE);
                }else{
                    body.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

}
