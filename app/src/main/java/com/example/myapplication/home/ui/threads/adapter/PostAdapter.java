package com.example.myapplication.home.ui.threads.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.ui.threads.PostDetailActivity;
import com.example.myapplication.home.ui.threads.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        ImageView postImageView;
        Button commentButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            postImageView = itemView.findViewById(R.id.postImageView);
            commentButton = itemView.findViewById(R.id.commentButton);
        }

        public void bind(Post post) {
            contentTextView.setText(post.content);
            if (post.imageUrl != null && !post.imageUrl.isEmpty()) {
                // Load image using a library like Glide or Picasso
                Glide.with(itemView.getContext()).load(post.imageUrl).into(postImageView);
            }

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open post detail activity
                    Intent intent = new Intent(itemView.getContext(), PostDetailActivity.class);
                    intent.putExtra("postId", post.postId);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
