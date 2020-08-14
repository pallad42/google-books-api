package com.app.books_api.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.books_api.database.entity.BookEntity;
import com.app.books_api.database.dao.BookDao;

@Database(entities = BookEntity.class, exportSchema = false, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    private static final String DB_NAME = "books_db";
    private static BookDatabase instance;

    public static synchronized BookDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder
                    (
                            context.getApplicationContext(),
                            BookDatabase.class,
                            DB_NAME
                    )
                    .build();
        }
        return instance;
    }

    public abstract BookDao getBookDao();
}
