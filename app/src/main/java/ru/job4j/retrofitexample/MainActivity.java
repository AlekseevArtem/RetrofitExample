package ru.job4j.retrofitexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements RetrofitForJSON.getAllPostsFromAPI,
        RetrofitForJSON.addNewPostIntoAPI, RetrofitForJSON.editPostInAPI,
        RetrofitForJSON.deletePostInAPI, RetrofitForJSON.allActionsWithAPI {
    private RecyclerView mViewsForPosts;
    private List<Post> posts;
    private RetrofitForJSON api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_posts);
        mViewsForPosts = findViewById(R.id.list_of_posts);
        mViewsForPosts.setLayoutManager(new LinearLayoutManager(this));
        api = new RetrofitForJSON(this);
        getAllPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_of_posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new_task:
                addNewPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getAllPosts() {
        api.getAllPosts();
    }

    private void addNewPost() {
        api.addNewPost(1, "title1", "title1");
    }

    private void editPost() {
        api.editPost(1, 1, "title test", "text test");
    }

    private void deletePost() {
        api.deletePost(1);
    }

    private void updateUI() {
        mViewsForPosts.setAdapter(new PostsAdapter(posts));
    }

    @Override
    public void successGetAllPostsFromAPI(boolean response, List<Post> body, int code) {
        if (response) {
            this.posts = body;
            updateUI();
        } else {
            toastForAnswerWithError(code);
        }
    }

    @Override
    public void successAddNewPostIntoAPI(boolean response, Post post, int code) {
        if (response) {
            int position = post.getId() - 1;
            posts.add(position, post);
            mViewsForPosts.getAdapter().notifyItemInserted(position);
        } else {
            toastForAnswerWithError(code);
        }
    }

    @Override
    public void successEditPostInAPI(boolean response, Post post, int code) {
        if (response) {
            int position = post.getId() - 1;
            posts.add(position, post);
            mViewsForPosts.getAdapter().notifyItemChanged(position);
        } else {
            toastForAnswerWithError(code);
        }
    }

    @Override
    public void successDeletePostInAPI(boolean response, int code) {
        if (response) {
            posts.remove(0);
            mViewsForPosts.getAdapter().notifyItemRemoved(0);
        } else {
            toastForAnswerWithError(code);
        }
    }

    private void toastForAnswerWithError(int code) {
        Toast.makeText(getApplicationContext(),
                String.format("Ошибка, статус ответа: %s", code), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedAnswerFromAPI(String response) {
        Intent intent = new Intent(MainActivity.this, ErrorActivity.class);
        intent.putExtra("Error", response);
        startActivity(intent);
    }

    private class PostsHolder extends RecyclerView.ViewHolder {
        private CircleButton editPost, deletePost;
        private TextView userID, id, title, text;

        public PostsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_post, parent,false));
            editPost = itemView.findViewById(R.id.edit_button);
            deletePost = itemView.findViewById(R.id.delete_button);
            userID = itemView.findViewById(R.id.list_item_post_userid);
            id = itemView.findViewById(R.id.list_item_post_id);
            title = itemView.findViewById(R.id.list_item_post_title);
            text = itemView.findViewById(R.id.list_item_post_text);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Post post) {
            userID.setText(Integer.toString(post.getUserId()));
            id.setText(Integer.toString(post.getId()));
            title.setText(post.getTitle());
            text.setText(post.getText());
            editPost.setOnClickListener(v -> editPost());
            deletePost.setOnClickListener(v -> deletePost());
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