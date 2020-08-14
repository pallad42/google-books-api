package com.app.books_api.api.google_books;

import com.app.books_api.api.google_books.pojo.Item;
import com.app.books_api.api.google_books.pojo.Response;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class GoogleBooksApi {
    private static Retrofit instance;
    private static ApiService apiService;

    private static final String API_URL = "https://www.googleapis.com/books/v1/";

    private static Retrofit getInstance() {
        if (instance == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            instance = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getInstance().create(ApiService.class);
        }
        return apiService;
    }

    public interface ApiService {
        @GET("volumes")
        Observable<Response> getBooks
                (
                        @Query("q") String title,
                        @Query("start_index") int startIndex,
                        @Query("maxResults") int maxResults,
                        @Query("filter") String filter,
                        @Query("orderBy") String orderBy,
                        @Query("printType") String printType
                );

        @GET("volumes/{id}")
        Observable<Item> getBook
                (
                        @Path("id") String id
                );
    }

    public enum QueryFilter {
        PARTIAL("partial"),
        FULL("full"),
        FREE_EBOOKS("free-ebooks"),
        PAID_EBOOKS("paid-ebooks"),
        EBOOKS("ebooks");

        private String name;

        QueryFilter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    public static QueryFilter DefaultQueryFilter = QueryFilter.EBOOKS;

    public enum QueryOrderBy {
        RELEVANCE("relevance"),
        NEWEST("newest");

        private String name;

        QueryOrderBy(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    public static QueryOrderBy DefaultQueryOrderBy = QueryOrderBy.RELEVANCE;

    public enum QueryPrintType {
        ALL("all"),
        BOOKS("books"),
        MAGAZINES("magazines");

        private String name;

        QueryPrintType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    public static QueryPrintType DefaultQueryPrintType = QueryPrintType.ALL;


    public enum Saleability {
        UNKNOWN, FOR_SALE, NOT_FOR_SALE, FREE;

        public static boolean contains(String value) {
            for (Saleability saleability : Saleability.values()) {
                if (saleability.name().equals(value)) {
                    return true;
                }
            }
            return false;
        }

        public static Saleability stringToSaleability(String value) {
            if (contains(value)) {
                return Saleability.valueOf(value);
            }
            return UNKNOWN;
        }
    }
}
