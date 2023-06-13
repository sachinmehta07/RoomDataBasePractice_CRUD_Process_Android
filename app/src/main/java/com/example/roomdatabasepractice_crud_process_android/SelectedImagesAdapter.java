package com.example.roomdatabasepractice_crud_process_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder> {
    private List<SelectedImage> selectedImages;
    private Context context;

    public SelectedImagesAdapter(Context context, List<SelectedImage> selectedImages) {
        this.context = context;
        this.selectedImages = selectedImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelectedImage selectedImage = selectedImages.get(position);
        Glide.with(context).load(selectedImage.getImagePath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return selectedImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
