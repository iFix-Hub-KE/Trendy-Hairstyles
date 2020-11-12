package com.ifixhubke.trendyhairstyles;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final ArrayList<Post> postsList;
    private final ItemClickListener itemClickListener;

    public PostsAdapter(ArrayList<Post> postsList, ItemClickListener itemClickListener) {
        this.postsList = postsList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts_recycler_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Post post_list = postsList.get(i);

        Picasso.get()
                .load(post_list.getProfile_image_url())
                .fit()
                .centerInside()
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewHolder.poster_prof_image);
        Picasso.get()
                .load(post_list.getStyle_photo_url())
                .fit()
                .centerInside()
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewHolder.style_image);

        viewHolder.poster_name.setText(post_list.getUser_name());
        viewHolder.description.setText(post_list.getStyle_name() + " Weaved at " + post_list.getSalon_name() + " at KSh." + post_list.getStyle_price());
        viewHolder.date_posted.setText(post_list.getDate_time());
        viewHolder.likes.setText(String.valueOf(post_list.getLikes()));
        viewHolder.caption.setText(post_list.getCaption());

        viewHolder.like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                itemClickListener.likePost(postsList.get(i), i);
                likeButton.setLikeDrawableRes(R.drawable.ic_like_love_on);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                itemClickListener.dislikePost(postsList.get(i), i);
                likeButton.setLikeDrawableRes(R.drawable.ic_like_love_off);
            }
        });


        viewHolder.save.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                itemClickListener.savePost(postsList.get(i), i);
                likeButton.setLikeDrawableRes(R.drawable.ic_save_on);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                itemClickListener.savePost(postsList.get(i), i);
                likeButton.setUnlikeDrawableRes(R.drawable.ic_save_off);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView poster_name;
        TextView description;
        TextView likes;
        TextView caption;
        TextView date_posted;
        CircleImageView poster_prof_image;
        ImageView style_image;
        LikeButton like;
        LikeButton save;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster_name = itemView.findViewById(R.id.poster_profile_name);
            description = itemView.findViewById(R.id.style_description);
            likes = itemView.findViewById(R.id.post_likes);
            poster_prof_image = itemView.findViewById(R.id.poster_profile_image);
            style_image = itemView.findViewById(R.id.style_image);
            date_posted = itemView.findViewById(R.id.date_posted);
            caption = itemView.findViewById(R.id.caption_txt);
            like = itemView.findViewById(R.id.like_button);
            save = itemView.findViewById(R.id.save_post);
        }
    }
}
