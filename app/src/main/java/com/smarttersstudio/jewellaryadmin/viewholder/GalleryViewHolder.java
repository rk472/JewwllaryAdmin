package com.smarttersstudio.jewellaryadmin.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.smarttersstudio.jewellaryadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    private ImageView img;
    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.gallery_image);
    }
    public void setImage(final String url, final AppCompatActivity a){
        Picasso.with(a).load(url).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_image_black_24dp)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Picasso.with(a).load(url).placeholder(R.drawable.ic_image_black_24dp).into(img);
                    }
                });
    }

}
