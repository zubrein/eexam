package xit.zubrein.eexam.statistics.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.victor.loading.newton.NewtonCradleLoading;

import java.util.List;

import xit.zubrein.eexam.R;
import xit.zubrein.eexam.category.CategoryActivity;
import xit.zubrein.eexam.category.CategoryAdapter;
import xit.zubrein.eexam.category.CategoryModel;
import xit.zubrein.eexam.category.CategoryViewmodel;
import xit.zubrein.eexam.challengefriend.adapter.MyChallengeAdapter;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.viewmodels.MyChallengeViewModel;
import xit.zubrein.eexam.statistics.adapter.StatisticsCategoryAdapter;

public class FragmentRecent extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private StatisticsCategoryAdapter mAdapter;
    CategoryViewmodel viewmodel;
    private static final String TAG = "FragmentRecent";
    LinearLayout loading;
    NewtonCradleLoading newtonCradleLoading;

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recent, container, false);

        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        newtonCradleLoading = view.findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();


        viewmodel = new ViewModelProvider(getActivity()).get(CategoryViewmodel.class);

        viewmodel.getSubjects().observe(getActivity(), new Observer<List<CategoryModel.subjects>>() {
            @Override
            public void onChanged(List<CategoryModel.subjects> subjects) {
                loading.setVisibility(View.GONE);
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new StatisticsCategoryAdapter(subjects,getContext());
                recyclerView.setAdapter(mAdapter);
            }
        });

        return view;
    }

}
