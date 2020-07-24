package ru.job4j.retrofitexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mViewsForPosts;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_posts);
        mViewsForPosts = findViewById(R.id.list_of_posts);
        mViewsForPosts.setLayoutManager(new LinearLayoutManager(this));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                posts = response.body();
                updateUI();
            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUI() {
        mViewsForPosts.setAdapter(new PostsAdapter(posts));
    }

    private class PostsHolder extends RecyclerView.ViewHolder {
        private TextView userID;
        private TextView id;
        private TextView title;
        private TextView text;

        public PostsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_post, parent,false));
            userID = itemView.findViewById(R.id.list_item_post_userid);
            id = itemView.findViewById(R.id.list_item_post_id);
            title = itemView.findViewById(R.id.list_item_post_title);
            text = itemView.findViewById(R.id.list_item_post_text);
        }

        public void bind(Post post) {
            userID.setText(Integer.toString(post.getUserId()));
            id.setText(Integer.toString(post.getId()));
            title.setText(post.getTitle());
            text.setText(post.getText());
        }
    }

    private class PostsAdapter extends RecyclerView.Adapter<PostsHolder> {
        private List<Post> posts;

        public PostsAdapter(List<Post> crimes) {
            this.posts = crimes;
        }

        @NonNull
        @Override
        public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostsHolder(LayoutInflater.from(getApplicationContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
            holder.bind(posts.get(position));
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }
}