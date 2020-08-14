package com.app.books_api.adapter;

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

public class SmallBookListAdapter extends RecyclerView.Adapter<SmallBookListAdapter.ViewHolder> {
    private List<BookEntity> books = new ArrayList<>();
    private AdapterItemListener itemListener;

    public SmallBookListAdapter(AdapterItemListener itemListener) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_small_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookEntity book = getBookEntity(position);

        holder.itemView.setOnClickListener(view -> itemListener.onClick(book));

        holder.bookTitle.setText(book.getTitle());

        if (!book.getImageUrl().isEmpty()) {
            Picasso.get().load(book.getImageUrl()).into(holder.bookImage);
        } else {
            holder.bookImage.setImageResource(R.drawable.default_book);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitle;
        public ImageView bookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.card_book_title);
            bookImage = itemView.findViewById(R.id.card_book_image);
        }
    }
}
