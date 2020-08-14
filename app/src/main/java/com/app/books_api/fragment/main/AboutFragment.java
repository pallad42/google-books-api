package com.app.books_api.fragment.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.books_api.R;
import com.app.books_api.activity.MainActivity;

public class AboutFragment extends Fragment {
    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity.showBar();
        mainActivity.enableBarItem(mainActivity.ABOUT_ITEM);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.disableBarItem(mainActivity.ABOUT_ITEM);
    }
}