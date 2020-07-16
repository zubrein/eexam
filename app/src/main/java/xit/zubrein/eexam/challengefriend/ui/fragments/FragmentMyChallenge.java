package xit.zubrein.eexam.challengefriend.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.challengefriend.adapter.MyChallengeAdapter;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.ui.MyChallengeActivity;
import xit.zubrein.eexam.challengefriend.viewmodels.MyChallengeViewModel;

public class FragmentMyChallenge extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyChallengeAdapter mAdapter;
    MyChallengeViewModel viewmodel;
    private static final String TAG = "FragmentMyChallenge";

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_challenge, container, false);
        viewmodel = new ViewModelProvider(getActivity()).get(MyChallengeViewModel.class);

        viewmodel.get_my_challenge_list().observe(getActivity(), new Observer<List<MyChallengeModel.My_challenge>>() {
            @Override
            public void onChanged(List<MyChallengeModel.My_challenge> my_challengeList) {
                Log.d(TAG, "onChanged: "+my_challengeList.size());
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new MyChallengeAdapter(my_challengeList);
                recyclerView.setAdapter(mAdapter);
            }
        });

        final SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewmodel.get_my_challenge_list().observe(getActivity(), new Observer<List<MyChallengeModel.My_challenge>>() {
                    @Override
                    public void onChanged(List<MyChallengeModel.My_challenge> my_challengeList) {
                        Log.d(TAG, "onChanged: "+my_challengeList.size());
                        recyclerView = view.findViewById(R.id.recyclerView);
                        recyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new MyChallengeAdapter(my_challengeList);
                        recyclerView.setAdapter(mAdapter);
                    }
                });

                if (null != swipeLayout) {
                    swipeLayout.setRefreshing(false);
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewmodel.get_my_challenge_list().observe(getActivity(), new Observer<List<MyChallengeModel.My_challenge>>() {
            @Override
            public void onChanged(List<MyChallengeModel.My_challenge> my_challengeList) {
                Log.d(TAG, "onChanged: "+my_challengeList.size());
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new MyChallengeAdapter(my_challengeList);
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
}
