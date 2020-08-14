package com.app.books_api.database.entity.converters;

import androidx.room.TypeConverter;

import static com.app.books_api.api.google_books.GoogleBooksApi.*;

public class SaleabilityConverter {
    @TypeConverter
    public static Saleability stringToSaleability(String value) {
        return value == null ? null : Saleability.stringToSaleability(value);
    }

    @TypeConverter
    public static String saleabilityToString(Saleability saleability) {
        return saleability == null ? null : saleability.name();
    }
}
