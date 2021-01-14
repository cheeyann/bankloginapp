package com.example.xinbank;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UploadPicture extends RecyclerView.Adapter<UploadPicture.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;

    public UploadPicture(List<String> fileNameList, List<String> fileDoneList){
        this.fileDoneList = fileDoneList;
        this.fileNameList = fileNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fileName = fileNameList.get(position);
        holder.fileNameView.setText(fileName);

        String fileDone = fileDoneList.get(position);

        if (fileDone.equals("Uploading")){
            holder.fileDoneView.setImageResource(R.drawable.loading_icon2);
        }
        else{
            holder.fileDoneView.setImageResource(R.drawable.check_circle);
        }
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View sView;

        public TextView fileNameView;
        public ImageView fileDoneView;

        public ViewHolder (View itemView){
            super(itemView);

            sView = itemView;

            fileNameView = (TextView) sView.findViewById(R.id.upload_filename);
            fileDoneView = (ImageView) sView.findViewById(R.id.upload_loading);

        }
    }
}