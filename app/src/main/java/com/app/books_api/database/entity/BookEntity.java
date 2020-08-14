package com.app.books_api.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import static com.app.books_api.api.google_books.GoogleBooksApi.*;
import com.app.books_api.database.entity.converters.DateConverter;
import com.app.books_api.database.entity.converters.SaleabilityConverter;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookEntity implements Serializable {
    @NonNull
    @PrimaryKey
    private String id;

    private String title;
    private String authors;
    private String imageUrl;
    private String publishedDate;
    private String description;
    private String publisher;
    private int pageCount;
    private String categories;
    private double averageRating;
    private int ratingsCount;
    private double price;
    private String priceCurrencyCode;
    private boolean ebook;
    private String printType;

    @TypeConverters(SaleabilityConverter.class)
    private Saleability saleability;

    @TypeConverters(DateConverter.class)
    private Date updatedAt;
}
