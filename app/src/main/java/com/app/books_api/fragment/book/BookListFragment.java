package com.app.books_api.fragment.book;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.books_api.R;
import com.app.books_api.adapter.BookListAdapter;

import static com.app.books_api.api.google_books.GoogleBooksApi.*;

import com.app.books_api.api.google_books.GoogleBooksUtils;
import com.app.books_api.database.entity.BookEntity;
import com.app.books_api.database.repository.BookRepository;
import com.app.books_api.fragment.StoreFragment;
import com.app.books_api.listener.NetworkListener;
import com.app.books_api.utils.AnimationUtils;
import com.app.books_api.utils.InternalStorageUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BookListFragment extends StoreFragment implements NetworkListener {
    // Bundle data
    private static final String ADAPTER_LIST_STATE = "ADAPTER_LIST_STATE";
    private static final String RECYCLER_VIEW_ITEM_POSITION_STATE = "RECYCLER_VIEW_ITEM_POSITION_STATE";
    private static final String START_INDEX_STATE = "START_INDEX_STATE";

    // Arguments
    public static final String BOOK_TITLE_ARG = "BOOK_TITLE_ARG";
    public static final String BOOK_FILTER_ARG = "BOOK_FILTER_ARG";
    public static final String BOOK_ORDER_BY_ARG = "BOOK_ORDER_BY_ARG";
    public static final String BOOK_PRINT_TYPE_ARG = "BOOK_PRINT_TYPE_ARG";
    public static final String BOOK_REFRESH_ARG = "BOOK_REFRESH_ARG";

    // Thread manager
    private CompositeDisposable compositeDisposable;
    private BookRepository bookRepository;

    // View
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View messagePanel;
    private TextView errorMessage;

    private BookListAdapter adapter;
    private NavController navController;

    // Cached data
    private List<BookEntity> books = new ArrayList<>();

    // Fetch response
    public enum FetchResponse {
        OK, EMPTY, ERROR
    }

    // Recycler view scroll
    private boolean canScroll = true;

    // State variables
    private int startIndex = 0;
    private int maxResults = 10;

    // Arguments
    private String bookTitle;
    private QueryFilter filter;
    private QueryOrderBy orderBy;
    private QueryPrintType printType;
    private boolean refresh;

    private boolean created = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();
        bookRepository = new BookRepository(requireContext());

        Bundle args = getArguments();
        if (args != null) {
            bookTitle = args.getString(BOOK_TITLE_ARG);
            filter = (QueryFilter) args.getSerializable(BOOK_FILTER_ARG);
            orderBy = (QueryOrderBy) args.getSerializable(BOOK_ORDER_BY_ARG);
            printType = (QueryPrintType) args.getSerializable(BOOK_PRINT_TYPE_ARG);
            refresh = (boolean) args.getSerializable(BOOK_REFRESH_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.book_list_rv);
        messagePanel = view.findViewById(R.id.message_panel);
        errorMessage = view.findViewById(R.id.error_message);

        navController = Navigation.findNavController(view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BookListAdapter(book -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BookDetailsFragment.BOOK_ARG, book);
            navController.navigate(R.id.bookDetailsFragment, bundle, AnimationUtils.fromRightToLeft());
            bookRepository.insert(book);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (canScroll && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    startIndex += maxResults;
                    fetchBooks(false);
                }
            }
        });
        adapter.setBooks(books);

        if (!created) {
            if (!refresh) {
                readData();
            }
            if (books.isEmpty()) {
                fetchBooks(true);
            }
            created = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        bookRepository.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        writeData();
    }

    @Override
    public void onBroadcastStart() {
        if (!books.isEmpty()) {
            fetchBooks(false);
        } else {
            fetchBooks(true);
        }
    }

    @Override
    public void onBroadcastStop() {

    }

    private void fetchBooks(boolean loadingScreen) {
        canScroll = false;
        if (loadingScreen) {
            showLoadingScreen();
        }
        compositeDisposable.add(getApiService().getBooks(
                bookTitle,
                startIndex,
                maxResults,
                filter.getName(),
                orderBy.getName(),
                printType.getName()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<BookEntity> list = GoogleBooksUtils.convertFromResponse(response);

                    books.addAll(list);
                    adapter.setBooks(books);

                    if (books.isEmpty()) {
                        setFetchResponse(FetchResponse.EMPTY);
                    } else {
                        setFetchResponse(FetchResponse.OK);
                    }

                    canScroll = true;
                }, throwable -> {
                    Log.e("fetch", throwable.getMessage());

                    if (books.isEmpty()) {
                        setFetchResponse(FetchResponse.ERROR);
                    }
                }));
    }

    @Override
    protected void writeData() {
        int recyclerViewItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        InternalStorageUtils.writeObject(requireContext(), RECYCLER_VIEW_ITEM_POSITION_STATE, recyclerViewItemPosition);

        InternalStorageUtils.writeObject(requireContext(), ADAPTER_LIST_STATE, books);

        InternalStorageUtils.writeObject(requireContext(), START_INDEX_STATE, startIndex);
    }

    @Override
    protected void readData() {
        int recyclerViewState = (int) InternalStorageUtils.readObject(requireContext(), RECYCLER_VIEW_ITEM_POSITION_STATE);
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(recyclerViewState, 0);

        books = (List<BookEntity>) InternalStorageUtils.readObject(getContext(), ADAPTER_LIST_STATE);
        adapter.setBooks(books);

        startIndex = (int) InternalStorageUtils.readObject(requireContext(), START_INDEX_STATE);
    }

    // Render views
    private void setFetchResponse(FetchResponse fetchResponse) {
        switch (fetchResponse) {
            case OK:
                showListWithData();
                break;

            case EMPTY:
                showMessage(getResources().getString(R.string.no_results));
                break;

            case ERROR:
                showMessage(getResources().getString(R.string.book_error));
                break;
        }
    }

    private void showMessage(String msg) {
        messagePanel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorMessage.setText(msg);
    }

    private void showLoadingScreen() {
        messagePanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showListWithData() {
        messagePanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}