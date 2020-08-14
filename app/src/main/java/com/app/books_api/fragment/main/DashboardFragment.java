package com.app.books_api.fragment.main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.books_api.R;
import com.app.books_api.activity.MainActivity;
import com.app.books_api.adapter.SmallBookListAdapter;
import com.app.books_api.database.entity.BookEntity;
import com.app.books_api.database.viewmodel.BookViewModel;
import com.app.books_api.fragment.book.BookDetailsFragment;
import com.app.books_api.utils.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private SmallBookListAdapter adapter;
    private BookViewModel bookViewModel;
    private List<BookEntity> books = new ArrayList<>();

    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) requireActivity();

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.getAllOrderByUpdatedAt().observe(this, bookEntities -> {
            books.clear();
            books.addAll(bookEntities);
            adapter.setBooks(books);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity.showBar();
        mainActivity.enableBarItem(mainActivity.DASHBOARD_ITEM);

        NavController navController = Navigation.findNavController(view);
        RecyclerView recyclerView = view.findViewById(R.id.home_last_visited_layout);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new SmallBookListAdapter(book -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BookDetailsFragment.BOOK_ARG, book);
            mainActivity.hideBar();
            navController.navigate(R.id.bookDetailsFragment, bundle, AnimationUtils.fromTopToBottom());
        });
        recyclerView.setAdapter(adapter);
        adapter.setBooks(books);

        ImageView deleteAllImageView = view.findViewById(R.id.delete_all_image);
        deleteAllImageView.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

            builder.setTitle(getResources().getString(R.string.confirm));
            builder.setMessage(getResources().getString(R.string.clear_history_confirm));

            builder.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
                bookViewModel.deleteAll();
                Toast.makeText(requireContext(), getResources().getString(R.string.delete_all), Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            });

            builder.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });

            builder.setIcon(R.drawable.ic_baseline_delete_24);

            builder.create().show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.disableBarItem(mainActivity.DASHBOARD_ITEM);
    }
}