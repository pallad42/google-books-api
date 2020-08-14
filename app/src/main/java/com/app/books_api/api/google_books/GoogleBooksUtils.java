package com.app.books_api.api.google_books;

import com.app.books_api.api.google_books.pojo.Item;
import com.app.books_api.api.google_books.pojo.Response;
import com.app.books_api.api.google_books.pojo.SaleInfo;
import com.app.books_api.api.google_books.pojo.VolumeInfo;
import com.app.books_api.database.entity.BookEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleBooksUtils {
    public static List<BookEntity> convertFromResponse(Response response) {
        List<BookEntity> list = new ArrayList<>();

        for (Item item : response.getItems()) {
            list.add(convertFromItem(item));
        }

        return list;
    }

    public static BookEntity convertFromItem(Item item) {
        VolumeInfo vol = item.getVolumeInfo();
        if (vol != null) {
            BookEntity book = new BookEntity();

            book.setId(item.getId());
            book.setTitle(vol.getTitle());
            book.setDescription(vol.getDescription());
            String authors = "";
            if (vol.getAuthors() != null) {
                authors = Arrays.toString(vol.getAuthors().toArray());
                authors = authors.substring(1, authors.length() - 1);
            }
            book.setAuthors(authors);
            book.setImageUrl(vol.getImageLinks() != null ? vol.getImageLinks().getThumbnail() : "");
            book.setRatingsCount((int) vol.getRatingsCount());
            book.setAverageRating(vol.getAverageRating());
            book.setPageCount((int) vol.getPageCount());
            book.setPublishedDate(vol.getPublishedDate());
            book.setPublisher(vol.getPublisher());
            book.setPrintType(vol.getPrintType());

            SaleInfo sale = item.getSaleInfo();
            book.setPrice(sale.getListPrice() != null ? sale.getListPrice().getAmount() : 0.0);
            book.setPriceCurrencyCode(sale.getListPrice() != null ? sale.getListPrice().getCurrencyCode() : "");
            book.setEbook(sale.isIsEbook());

            book.setSaleability(GoogleBooksApi.Saleability.stringToSaleability(sale.getSaleability()));

            return book;
        }
        return null;
    }
}
