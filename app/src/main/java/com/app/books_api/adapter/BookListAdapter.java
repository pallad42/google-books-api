package com.app.books_api.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.books_api.R;
import com.app.books_api.database.entity.BookEntity;
import com.app.books_api.listener.AdapterItemListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private List<BookEntity> books = new ArrayList<>();
    private AdapterItemListener itemListener;
    private Context context;

    public BookListAdapter(AdapterItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setBooks(List<BookEntity> googleBooksModelEntities) {
        books.clear();
        books.addAll(googleBooksModelEntities);
        notifyDataSetChanged();
    }

    private BookEntity getBookEntity(int position) {
        if (position < 0 || position >= books.size()) {
            return null;
        }
        return books.get(position);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_book, parent, false);
        context = parent.getContext();
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookEntity book = getBookEntity(position);

        Resources stringResources = context.getResources();

        holder.itemView.setOnClickListener(view -> itemListener.onClick(book));

        holder.bookTitle.setText(book.getTitle());

        if (!book.getAuthors().isEmpty()) {
            holder.bookAuthors.setText(book.getAuthors());
        } else {
            holder.bookAuthors.setVisibility(View.GONE);
        }

        if (!book.getImageUrl().isEmpty()) {
            Picasso.get().load(book.getImageUrl()).into(holder.bookImage);
        } else {
            holder.bookImage.setImageResource(R.drawable.default_book);
        }

        if (book.isEbook()) {
            holder.bookType.setText(stringResources.getString(R.string.book_details_ebook));
        } else {
            holder.bookType.setVisibility(View.GONE);
        }

        switch (book.getSaleability()) {
            case FREE:
                holder.bookPrice.setText(stringResources.getString(R.string.book_details_saleability_free));
                break;

            case FOR_SALE:
                String value = String.format(Locale.getDefault(), "%.2f", book.getPrice()) + " " + book.getPriceCurrencyCode();
                holder.bookPrice.setText(value);
                break;

            case NOT_FOR_SALE:
                holder.bookPrice.setText(stringResources.getString(R.string.book_details_saleability_not_for_sale));
                break;

            case UNKNOWN:
                holder.bookPrice.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitle;
        public TextView bookAuthors;
        public ImageView bookImage;
        public TextView bookPrice;
        public TextView bookType;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.card_book_title);
            bookAuthors = itemView.findViewById(R.id.card_book_authors);
            bookImage = itemView.findViewById(R.id.card_book_image);
            bookPrice = itemView.findViewById(R.id.card_book_price);
            bookType = itemView.findViewById(R.id.card_book_type);
        }
    }
}
