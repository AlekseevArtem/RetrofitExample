package ru.job4j.retrofitexample;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitForJSON {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Context callback;

    public RetrofitForJSON (Context callback) {
        this.callback = callback;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }

    public interface getAllPostsFromAPI {
        void successGetAllPostsFromAPI(boolean response, int code, List<Post> body);
        void failedGetAllPostsFromAPI(String t);
    }

    public interface addNewPostIntoAPI {
        void successAddNewPostIntoAPI(boolean response, Post post);
        void failedAddNewPostIntoAPI(String t);
    }

    public interface editPostInAPI {
        void successEditPostInAPI(boolean response, Post post);
        void failedEditPostInAPI(String t);
    }

    public interface deletePostInAPI {
        void successDeletePostInAPI(boolean response);
        void failedDeletePostInAPI(String t);
    }

    public void getAllPosts() {
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {
                ((getAllPostsFromAPI)callback)
                        .successGetAllPostsFromAPI(response.isSuccessful(),response.code(),response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {
                ((getAllPostsFromAPI)callback)
                        .failedGetAllPostsFromAPI(t.getMessage());
            }
        });
    }

    public void addNewPost(int userId, String title, String text) {
        Call<Post> call = jsonPlaceHolderApi.createPost(userId, title, text);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                ((addNewPostIntoAPI)callback)
                        .successAddNewPostIntoAPI(response.isSuccessful(), response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                ((addNewPostIntoAPI)callback)
                        .failedAddNewPostIntoAPI(t.toString());
            }
        });
    }

    public void editPost() {
        Post post = new Post(1, "title test", "text test");
        Call<Post> call = jsonPlaceHolderApi.putPost(1, post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                ((editPostInAPI)callback)
                        .successEditPostInAPI(response.isSuccessful(), response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                ((editPostInAPI)callback)
                        .failedEditPostInAPI(t.toString());
            }
        });
    }

    public void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(1);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                ((deletePostInAPI)callback)
                        .successDeletePostInAPI(response.isSuccessful());
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                ((deletePostInAPI)callback)
                        .failedDeletePostInAPI(t.toString());
            }
        });
    }
}
