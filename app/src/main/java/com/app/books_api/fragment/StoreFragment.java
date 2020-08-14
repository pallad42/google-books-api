package com.app.books_api.fragment;

import androidx.fragment.app.Fragment;

public abstract class StoreFragment extends Fragment {
    protected abstract void writeData();
    protected abstract void readData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        writeData();
    }
}
