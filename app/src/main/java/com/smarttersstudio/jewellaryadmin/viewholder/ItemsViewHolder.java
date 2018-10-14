package com.smarttersstudio.jewellaryadmin.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.smarttersstudio.jewellaryadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ItemsViewHolder extends RecyclerView.ViewHolder {
    private ImageView img;
    private ToggleButton fav;
    private TextView nameText,soldText;
    private View v;
    private Button removeButton;
    public ItemsViewHolder(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.item_image);
        nameText=itemView.findViewById(R.id.item_row_name);
        soldText=itemView.findViewById(R.id.item_row_sold);
        removeButton=itemView.findViewById(R.id.item_remove);
        v=itemView;
    }
    public void setName(String name){
        nameText.setText(name);
    }
    public void setSold(String sold){
        soldText.setText(sold);
    }
    public void setImage(final String url, final Context c){
        Picasso.with(c).load(url).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.pic_item)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Picasso.with(c).load(url).placeholder(R.drawable.pic_item).into(img);
                    }
                });
    }
    public void remove(final DatabaseReference itemRef){
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemRef.removeValue();
            }
        });
    }
}
