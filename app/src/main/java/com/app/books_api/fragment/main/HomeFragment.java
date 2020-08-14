package com.app.books_api.fragment.main;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.books_api.R;
import com.app.books_api.activity.MainActivity;

import static com.app.books_api.api.google_books.GoogleBooksApi.*;

import com.app.books_api.fragment.StoreFragment;
import com.app.books_api.fragment.book.BookListFragment;
import com.app.books_api.utils.AnimationUtils;
import com.app.books_api.utils.InternalStorageUtils;
import com.google.android.material.textfield.TextInputEditText;

public class HomeFragment extends StoreFragment {
    private static final String BOOK_TITLE_STATE = "BOOK_TITLE_STATE";
    private static final String BOOK_FILTER_STATE = "BOOK_FILTER_STATE";
    private static final String BOOK_ORDER_BY_STATE = "BOOK_ORDER_BY_STATE";
    private static final String BOOK_PRINT_TYPE_STATE = "BOOK_PRINT_TYPE_STATE";

    private String bookTitle;
    private QueryFilter bookFilter;
    private QueryOrderBy bookOrderBy;
    private QueryPrintType bookPrintType;
    private boolean bookRefresh;

    private MainActivity mainActivity;

    private NavController navController;
    private TextInputEditText searchField;

    private RadioGroup filterRadioGroup;
    private RadioGroup orderByRadioGroup;
    private RadioGroup printTypeRadioGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) requireActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readData();

        mainActivity.showBar();
        mainActivity.enableBarItem(mainActivity.HOME_ITEM);

        navController = Navigation.findNavController(view);

        searchField = view.findViewById(R.id.search_field);
        searchField.setText(bookTitle);

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    onSearch(searchField.getText().toString());
                }
                return false;
            }
        });

        Button searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(view1 -> {
            String bookTitle = searchField.getText().toString();
            onSearch(bookTitle);
        });

        // FILTER RADIO GROUP
        filterRadioGroup = view.findViewById(R.id.filter_radio_group);
        RadioButton filterPartialRadio = view.findViewById(R.id.filter_partial_radio);
        RadioButton filterFullRadio = view.findViewById(R.id.filter_full_radio);
        RadioButton filterFreeEbooksRadio = view.findViewById(R.id.filter_free_ebooks_radio);
        RadioButton filterPaidEbooksRadio = view.findViewById(R.id.filter_paid_ebooks_radio);
        RadioButton filterEbooksRadio = view.findViewById(R.id.filter_ebooks_radio);

        switch (bookFilter) {
            case PARTIAL:
                filterPartialRadio.setChecked(true);
                break;
            case FULL:
                filterFullRadio.setChecked(true);
                break;
            case FREE_EBOOKS:
                filterFreeEbooksRadio.setChecked(true);
                break;
            case PAID_EBOOKS:
                filterPaidEbooksRadio.setChecked(true);
                break;
            case EBOOKS:
                filterEbooksRadio.setChecked(true);
                break;
        }

        // ORDER RADIO GROUP
        orderByRadioGroup = view.findViewById(R.id.order_by_radio_group);
        RadioButton orderByRelevanceRadio = view.findViewById(R.id.order_by_relevance_radio);
        RadioButton orderByNewestRadio = view.findViewById(R.id.order_by_newest_radio);

        switch (bookOrderBy) {
            case RELEVANCE:
                orderByRelevanceRadio.setChecked(true);
                break;
            case NEWEST:
                orderByNewestRadio.setChecked(true);
                break;
        }

        // PRINT TYPE RADIO GROUP
        printTypeRadioGroup = view.findViewById(R.id.print_type_radio_group);
        RadioButton printTypeAllRadio = view.findViewById(R.id.print_type_all_radio);
        RadioButton printTypeBooksRadio = view.findViewById(R.id.print_type_books_radio);
        RadioButton printTypeMagazinesRadio = view.findViewById(R.id.print_type_magazines_radio);

        switch (bookPrintType) {
            case ALL:
                printTypeAllRadio.setChecked(true);
                break;
            case BOOKS:
                printTypeBooksRadio.setChecked(true);
                break;
            case MAGAZINES:
                printTypeMagazinesRadio.setChecked(true);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.disableBarItem(mainActivity.HOME_ITEM);
        writeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void writeData() {
        if (bookRefresh) {
            InternalStorageUtils.writeObject(requireContext(), BOOK_TITLE_STATE, bookTitle);
            InternalStorageUtils.writeObject(requireContext(), BOOK_FILTER_STATE, bookFilter);
            InternalStorageUtils.writeObject(requireContext(), BOOK_ORDER_BY_STATE, bookOrderBy);
            InternalStorageUtils.writeObject(requireContext(), BOOK_PRINT_TYPE_STATE, bookPrintType);
        }
    }

    @Override
    protected void readData() {
        bookTitle = (String) InternalStorageUtils.readObject(requireContext(), BOOK_TITLE_STATE);
        if (bookTitle == null) {
            bookTitle = "";
        }

        bookFilter = (QueryFilter) InternalStorageUtils.readObject(requireContext(), BOOK_FILTER_STATE);
        if (bookFilter == null) {
            bookFilter = DefaultQueryFilter;
        }

        bookOrderBy = (QueryOrderBy) InternalStorageUtils.readObject(requireContext(), BOOK_ORDER_BY_STATE);
        if (bookOrderBy == null) {
            bookOrderBy = DefaultQueryOrderBy;
        }

        bookPrintType = (QueryPrintType) InternalStorageUtils.readObject(requireContext(), BOOK_PRINT_TYPE_STATE);
        if (bookPrintType == null) {
            bookPrintType = DefaultQueryPrintType;
        }
    }

    private void onSearch(String query) {
        searchField.clearFocus();
        InputMethodManager imm = (InputMethodManager)requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

        mainActivity.hideBar();

        if (query.trim().isEmpty()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());

            alertDialog.setTitle(getResources().getString(R.string.search_field_is_empty_title));
            alertDialog.setMessage(getResources().getString(R.string.search_field_is_empty_description));
            alertDialog.setIcon(R.drawable.ic_baseline_error_24);

            alertDialog.create().show();

            return;
        }

        Bundle bundle = new Bundle();

        String _bookTitle = bookTitle;
        QueryFilter _bookFilter = bookFilter;
        QueryOrderBy _bookOrderBy = bookOrderBy;
        QueryPrintType _bookPrintType = bookPrintType;

        bookTitle = query.trim();
        bookFilter = getFilter();
        bookOrderBy = getOrderBy();
        bookPrintType = getPrintType();
        bookRefresh = true;

        if (bookTitle.equals(_bookTitle) &&
                bookFilter == _bookFilter &&
                bookOrderBy == _bookOrderBy &&
                bookPrintType == _bookPrintType
        ) {
            bookRefresh = false;
        }

        bundle.putString(BookListFragment.BOOK_TITLE_ARG, bookTitle);
        bundle.putSerializable(BookListFragment.BOOK_FILTER_ARG, bookFilter);
        bundle.putSerializable(BookListFragment.BOOK_ORDER_BY_ARG, bookOrderBy);
        bundle.putSerializable(BookListFragment.BOOK_PRINT_TYPE_ARG, bookPrintType);
        bundle.putSerializable(BookListFragment.BOOK_REFRESH_ARG, bookRefresh);
        navController.navigate(R.id.bookListFragment, bundle, AnimationUtils.fade());
    }

    private QueryFilter getFilter() {
        switch (filterRadioGroup.getCheckedRadioButtonId()) {
            case R.id.filter_partial_radio:
                return QueryFilter.PARTIAL;
            case R.id.filter_full_radio:
                return QueryFilter.FULL;
            case R.id.filter_free_ebooks_radio:
                return QueryFilter.FREE_EBOOKS;
            case R.id.filter_paid_ebooks_radio:
                return QueryFilter.PAID_EBOOKS;
            case R.id.filter_ebooks_radio:
                return QueryFilter.EBOOKS;
        }
        return DefaultQueryFilter;
    }

    private QueryOrderBy getOrderBy() {
        switch (orderByRadioGroup.getCheckedRadioButtonId()) {
            case R.id.order_by_relevance_radio:
                return QueryOrderBy.RELEVANCE;
            case R.id.order_by_newest_radio:
                return QueryOrderBy.NEWEST;
        }
        return DefaultQueryOrderBy;
    }

    private QueryPrintType getPrintType() {
        switch (printTypeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.print_type_all_radio:
                return QueryPrintType.ALL;
            case R.id.print_type_books_radio:
                return QueryPrintType.BOOKS;
            case R.id.print_type_magazines_radio:
                return QueryPrintType.MAGAZINES;
        }
        return DefaultQueryPrintType;
    }
}