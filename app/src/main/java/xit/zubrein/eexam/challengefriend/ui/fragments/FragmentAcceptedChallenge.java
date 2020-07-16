package xit.zubrein.eexam.challengefriend.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import xit.zubrein.eexam.challengefriend.viewmodels.MyChallengeViewModel;

public class FragmentAcceptedChallenge extends Fragment {

    SharedPreferences sharedPreferences;
    String user_id,token;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AcceptedChallengeAdapter mAdapter;
    MyChallengeViewModel viewmodel;
    private static final String TAG = "FragmentAcceptChallenge";

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_challenge, container, false);

        sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");

        viewmodel = new ViewModelProvider(getActivity()).get(MyChallengeViewModel.class);

        viewmodel.get_accepted_challenge().observe(getActivity(), new Observer<List<AcceptedChallengeModel.My_challenge>>() {
            @Override
            public void onChanged(List<AcceptedChallengeModel.My_challenge> my_challengeList) {
                Log.d(TAG, "onChanged: "+my_challengeList.size());
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new AcceptedChallengeAdapter(my_challengeList);
                recyclerView.setAdapter(mAdapter);
            }
        });



        return view;
    }

}
