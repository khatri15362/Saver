package com.example.whatsapp_saver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private List<File> statusFiles;
    private List<File> filteredFiles;

    public enum FilterType {
        ALL, IMAGES, VIDEOS
    }

    public StatusAdapter(List<File> statusFiles) {
        this.statusFiles = statusFiles;
        this.filteredFiles = new ArrayList<>(statusFiles);
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        File statusFile = filteredFiles.get(position);
        Uri uri = Uri.fromFile(statusFile);
        if (statusFile.getName().endsWith(".jpg")) {
            holder.imageView.setImageURI(uri);
            holder.imageView.setOnClickListener(v -> openImage(holder.imageView.getContext(), uri));
        } else if (statusFile.getName().endsWith(".mp4")) {
            holder.imageView.setImageResource(R.drawable.ic_video); // Thumbnail for videos
            holder.imageView.setOnClickListener(v -> openVideo(holder.imageView.getContext(), uri));
        }
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public void filter(FilterType filterType) {
        filteredFiles.clear();
        switch (filterType) {
            case ALL:
                filteredFiles.addAll(statusFiles);
                break;
            case IMAGES:
                for (File file : statusFiles) {
                    if (file.getName().endsWith(".jpg")) {
                        filteredFiles.add(file);
                    }
                }
                break;
            case VIDEOS:
                for (File file : statusFiles) {
                    if (file.getName().endsWith(".mp4")) {
                        filteredFiles.add(file);
                    }
                }
                break;
        }
        notifyDataSetChanged();
    }

    private void openImage(Context context, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);
    }

    private void openVideo(Context context, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/*");
        context.startActivity(intent);
    }

    static class StatusViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
