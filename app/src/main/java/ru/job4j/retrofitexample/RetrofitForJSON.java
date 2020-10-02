package ru.job4j.retrofitexample;

import android.content.Context;

import androidx.multidex.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitForJSON {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Context callback;

    public interface getAllPostsFromAPI {
        void successGetAllPostsFromAPI(boolean response, List<Post> body, int code);
    }

    public interface addNewPostIntoAPI {
        void successAddNewPostIntoAPI(boolean response, Post post, int code);
    }

    public interface editPostInAPI {
        void successEditPostInAPI(boolean response, Post post, int code);
    }

    public interface deletePostInAPI {
        void successDeletePostInAPI(boolean response, int code);
    }

    public interface allActionsWithAPI {
        void failedAnswerFromAPI(String response);
    }

    public RetrofitForJSON(Context callback) {
        this.callback = callback;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)  // logs
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }

    public void getAllPosts() {
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {
                ((getAllPostsFromAPI)callback)
                        .successGetAllPostsFromAPI(response.isSuccessful(), response.body(), response.code());
            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {
                ((allActionsWithAPI)callback)
                        .failedAnswerFromAPI(t.getMessage());
            }
        });
    }

    public void addNewPost(int userId, String title, String text) {
        Call<Post> call = jsonPlaceHolderApi.createPost(userId, title, text);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                ((addNewPostIntoAPI)callback)
                        .successAddNewPostIntoAPI(response.isSuccessful(), response.body(), response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                ((allActionsWithAPI)callback)
                        .failedAnswerFromAPI(t.getMessage());
            }
        });
    }

    public void editPost(int id, int userId, String title, String text) {
        Post post = new Post(userId, title, text);
        Call<Post> call = jsonPlaceHolderApi.putPost(id, post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                ((editPostInAPI)callback)
                        .successEditPostInAPI(response.isSuccessful(), response.body(), response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                ((allActionsWithAPI)callback)
                        .failedAnswerFromAPI(t.getMessage());
            }
        });
    }

    public void deletePost(int id) {
        Call<Void> call = jsonPlaceHolderApi.deletePost(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                ((deletePostInAPI)callback)
                        .successDeletePostInAPI(response.isSuccessful(), response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                ((allActionsWithAPI)callback)
                        .failedAnswerFromAPI(t.getMessage());
            }
        });
    }
}
