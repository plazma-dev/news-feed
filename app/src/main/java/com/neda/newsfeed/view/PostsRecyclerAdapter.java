package com.neda.newsfeed.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neda.newsfeed.databinding.PostListItemBinding;
import com.neda.newsfeed.model.Post;

import java.util.ArrayList;
import java.util.List;

class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.PostViewHolder> {
    private List<Post> posts = new ArrayList<>();
    private final PostClickListener postClickListener;

    public PostsRecyclerAdapter(PostClickListener postClickListener) {
        this.postClickListener = postClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostListItemBinding binding = PostListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(posts.get(position), postClickListener);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {

        PostListItemBinding binding;

        public PostViewHolder(PostListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Post post, PostClickListener postClickListener) {
            binding.postTitle.setText(post.getTitle());
            binding.postBody.setText(post.getBody());
            binding.getRoot().setOnClickListener(v -> {
                if (postClickListener != null) {
                    postClickListener.onPostClicked(post);
                }
            });
        }
    }
}
