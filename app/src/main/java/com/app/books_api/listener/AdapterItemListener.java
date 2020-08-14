package com.app.books_api.listener;

import com.app.books_api.database.entity.BookEntity;

public interface AdapterItemListener {
    void onClick(BookEntity model);
}
