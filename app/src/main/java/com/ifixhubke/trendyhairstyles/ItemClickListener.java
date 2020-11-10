package com.ifixhubke.trendyhairstyles;

public interface ItemClickListener {
         void  likePost(Post post, int position);
         void  dislikePost(Post post, int position);
         void  savePost(Post post, int position);
    }

