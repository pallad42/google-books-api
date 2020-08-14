package com.app.books_api.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.app.books_api.database.BookDatabase;
import com.app.books_api.database.dao.BookDao;
import com.app.books_api.database.entity.BookEntity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class BookRepository {
    private BookDao bookDao;
    private CompositeDisposable compositeDisposable;

    public BookRepository(Context context) {
        BookDatabase db = BookDatabase.getInstance(context);
        bookDao = db.getBookDao();
        compositeDisposable = new CompositeDisposable();
    }

    public void onDestroy() {
        compositeDisposable.dispose();
    }


    /* SELECT */
    public LiveData<List<BookEntity>> getAll() {
        return bookDao.getAll();
    }

    public LiveData<List<BookEntity>> getAllOrderByUpdatedAt() {
        return bookDao.getAllOrderByUpdatedAt();
    }

    public void getCount(BiConsumer<Integer, Throwable> callback) {
        compositeDisposable.add(bookDao
                .getCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback));
    }


    /* INSERT */
    public void insert(BookEntity bookEntity) {
        compositeDisposable.add(bookDao
                .insert(bookEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }


    /* DELETE */
    public void deleteAll() {
        compositeDisposable.add(bookDao
                .deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }
}
