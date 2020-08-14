package com.app.books_api.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.books_api.database.BookDatabase;
import com.app.books_api.database.entity.BookEntity;
import com.app.books_api.database.repository.BookRepository;

import java.util.List;

import io.reactivex.functions.BiConsumer;

public class BookViewModel extends AndroidViewModel {
    private BookRepository bookRepository;

    public BookViewModel(@NonNull Application application) {
        super(application);

        BookDatabase db = BookDatabase.getInstance(application);
        bookRepository = new BookRepository(application);
    }

    /* SELECT */
    public LiveData<List<BookEntity>> getAll() {
        return bookRepository.getAll();
    }

    public LiveData<List<BookEntity>> getAllOrderByUpdatedAt() {
        return bookRepository.getAllOrderByUpdatedAt();
    }

    public void getCount(BiConsumer<Integer, Throwable> callback) {
        bookRepository.getCount(callback);
    }


    /* INSERT */
    public void insert(BookEntity bookEntity) {
        bookRepository.insert(bookEntity);
    }


    /* DELETE */
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
