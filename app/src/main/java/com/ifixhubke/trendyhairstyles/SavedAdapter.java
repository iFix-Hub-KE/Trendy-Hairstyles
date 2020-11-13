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

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedViewHolder>{

    private ArrayList<Saved> savedList;

    public SavedAdapter(ArrayList<Saved> savedList){
        this.savedList = savedList;
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_recycler_row, parent, false);
        SavedViewHolder savedViewHolder = new SavedViewHolder(view);
        return savedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        Saved saved = savedList.get(position);
        holder.styleSalon.setText(saved.getSalonName());
        holder.styleName.setText(saved.getStyleName());
        holder.stylePrice.setText(saved.getStylePrice());
        Picasso.get()
                .load(saved.getStyleImage())
                .fit()
                .centerInside()
                .into(holder.styleImage);

    }

    @Override
    public int getItemCount() {
        return savedList.size();
    }

    class SavedViewHolder extends RecyclerView.ViewHolder{

        ImageView styleImage;
        TextView styleName;
        TextView stylePrice;
        TextView styleSalon;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            styleImage = itemView.findViewById(R.id.saved_style_image);
            styleName = itemView.findViewById(R.id.saved_style_name);
            stylePrice = itemView.findViewById(R.id.saved_style_price);
            styleSalon = itemView.findViewById(R.id.saved_style_salon_weaved);
        }
    }
}
