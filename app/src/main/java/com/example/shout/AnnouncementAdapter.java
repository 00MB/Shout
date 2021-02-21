package com.example.shout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ExampleViewHolder> {
    private ArrayList<AnnouncementItem> mExampleList1;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.ann_userimage);
            mTextView1 = itemView.findViewById(R.id.ann_name);
            mTextView2 = itemView.findViewById(R.id.ann_text);
            mTextView3= itemView.findViewById(R.id.ann_timestamp);
        }
    }
    public AnnouncementAdapter(ArrayList<AnnouncementItem> exampleList) {
        mExampleList1 = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageitemcard, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        AnnouncementItem currentItem = mExampleList1.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
    }
    @Override
    public int getItemCount() {
        return mExampleList1.size();
    }
}