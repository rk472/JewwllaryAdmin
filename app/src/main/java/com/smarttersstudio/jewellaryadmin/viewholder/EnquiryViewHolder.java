package com.smarttersstudio.jewellaryadmin.viewholder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.jewellaryadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class EnquiryViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText, descText, itemNameText;
    private Button closeButton;
    private ImageButton callButton;
    private ImageView img;
    private View v;

    public EnquiryViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        nameText = v.findViewById(R.id.enquiry_name);
        callButton = v.findViewById(R.id.call_button);
        descText = v.findViewById(R.id.enquiry_item_desc);
        itemNameText = v.findViewById(R.id.enquiry_item_name);
        img = v.findViewById(R.id.enquiry_image);
        closeButton=v.findViewById(R.id.close_enquiry);
    }

    public void setName(String name) {
        nameText.setText(name);
    }

    public void setCall(final String number, final AppCompatActivity a) {
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:+91" + number));
                if (ActivityCompat.checkSelfPermission(a, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(a,new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                a.startActivity(i);
            }
        });
    }
    public void setDesc(String desc){
        descText.setText(desc);
    }
    public void setItem(String gos, String type, String id, final AppCompatActivity a){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("items").child(gos).child(type).child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String image=dataSnapshot.child("image").getValue(String.class);
                String name=dataSnapshot.child("name").getValue(String.class);
                itemNameText.setText(name);
                Picasso.with(a).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_image_black_24dp)
                        .into(img, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError() {
                                Picasso.with(a).load(image).placeholder(R.drawable.ic_image_black_24dp).into(img);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void close(final DatabaseReference ref){
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue();
            }
        });

    }
}
