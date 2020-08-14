package com.app.books_api.fragment.book;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.books_api.R;
import com.app.books_api.database.entity.BookEntity;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class BookDetailsFragment extends Fragment {
    public static final String BOOK_ARG = "book";

    private BookEntity book;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = (BookEntity) getArguments().getSerializable(BOOK_ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = view.findViewById(R.id.book_details_title);
        TextView authors = view.findViewById(R.id.book_details_authors);
        ImageView image = view.findViewById(R.id.book_details_image);
        TextView publisher = view.findViewById(R.id.book_details_publisher);
        TextView averageRating = view.findViewById(R.id.book_details_average_rating);
        TextView ratingsCount = view.findViewById(R.id.book_details_ratings_count);
        TextView pageCount = view.findViewById(R.id.book_details_page_count);
        TextView description = view.findViewById(R.id.book_details_description);
        TextView price = view.findViewById(R.id.book_details_price);
        TextView priceCurrency = view.findViewById(R.id.book_details_price_currency);

        title.setText(book.getTitle());
        authors.setText(book.getAuthors());

        if (!book.getImageUrl().isEmpty()) {
            Picasso.get().load(book.getImageUrl()).into(image);
        } else {
            image.setImageResource(R.drawable.default_book);
        }

        averageRating.setText(String.format(Locale.getDefault(), "%.1f", book.getAverageRating()));
        ratingsCount.setText(String.format(Locale.getDefault(), "%d", book.getRatingsCount()));
        pageCount.setText(String.format(Locale.getDefault(), "%d", book.getPageCount()));
        description.setText(book.getDescription());

//        price.setText(String.format(Locale.getDefault(), "%.2f", book.getPrice()));
//        priceCurrency.setText(book.getPriceCurrencyCode());

//        if (book.isEbook()) {
//            holder.bookType.setText(stringResources.getString(R.string.book_details_ebook));
//        } else {
//            holder.bookType.setVisibility(View.GONE);
//        }

        switch (book.getSaleability()) {
            case FREE:
                price.setText(getResources().getString(R.string.book_details_saleability_free));
                priceCurrency.setVisibility(View.GONE);
                break;

            case FOR_SALE:
                price.setText(String.format(Locale.getDefault(), "%.2f", book.getPrice()));
                priceCurrency.setText(book.getPriceCurrencyCode());
                break;

            case NOT_FOR_SALE:
                price.setText(getResources().getString(R.string.book_details_saleability_not_for_sale));
                priceCurrency.setVisibility(View.GONE);
                break;

            case UNKNOWN:
                price.setVisibility(View.GONE);
                break;
        }

        publisher.setText(book.getPublisher());
    }
}