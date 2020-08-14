package com.app.books_api.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.app.books_api.database.entity.BookEntity;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class BookDao {

    /* SELECT */
    @Query("SELECT * FROM BookEntity")
    public abstract LiveData<List<BookEntity>> getAll();

    @Query("SELECT * FROM BookEntity ORDER BY updatedAt DESC")
    public abstract LiveData<List<BookEntity>> getAllOrderByUpdatedAt();

    @Query("SELECT COUNT(id) FROM BookEntity")
    public abstract Single<Integer> getCount();


    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void _insert(BookEntity bookEntity);

    public Completable insert(BookEntity bookEntity) {
        return Completable.fromAction(() -> {
            bookEntity.setUpdatedAt(new Date());
            _insert(bookEntity);
        });
    }


    /* DELETE */
    @Query("DELETE FROM BookEntity")
    public abstract Completable deleteAll();

}
