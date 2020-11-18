package com.ifixhubke.trendyhairstyles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    private final ArrayList<Saved> savedList;

    public SavedAdapter(ArrayList<Saved> savedList) {
        this.savedList = savedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_recycler_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Saved saved_list = savedList.get(position);

        Picasso.get()
                .load(saved_list.getStyleImage())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.style_image);

        holder.style_name.setText(saved_list.getStyleName());
        holder.style_price.setText(saved_list.getStylePrice());
        holder.style_salon_weaved.setText(saved_list.getSalonName());

    }

    @Override
    public int getItemCount() {
        return savedList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView style_name,style_price,style_salon_weaved;
        ImageView style_image;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            style_name = itemView.findViewById(R.id.style_name);
            style_price = itemView.findViewById(R.id.style_price);
            style_salon_weaved = itemView.findViewById(R.id.saved_style_salon_weaved);
            style_image = itemView.findViewById(R.id.saved_style_image);

        }
    }
}
