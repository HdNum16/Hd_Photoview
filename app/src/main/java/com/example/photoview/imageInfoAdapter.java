package com.example.photoview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class imageInfoAdapter extends RecyclerView.Adapter<imageInfoAdapter.UserItemViewHolder> {
    private List<imageInfo> users;
    private Context context;

    public imageInfoAdapter(List<imageInfo> users, Context c) {
        this.users = users;
        this.context = c;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public UserItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_info, parent, false);

        return new UserItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserItemViewHolder holder, final int position) {
        final imageInfo u = users.get(position);
        Picasso.with(context)
                .load(u.getWebformatURL())
                .into(holder.ivImage);
        holder.tvTags.setText(u.getTags());

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ImageFullscreenActivity.class);
                intent.putExtra("largeImageURL", u.getLargeImageURL());
                intent.putExtra("webformatURL", u.getWebformatURL());
                intent.putExtra("tags", u.getTags());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public static class UserItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTags;
        public ImageView ivImage;

        public UserItemViewHolder(View itemView) {
            super(itemView);
            tvTags = (TextView) itemView.findViewById(R.id.tv_tags);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
