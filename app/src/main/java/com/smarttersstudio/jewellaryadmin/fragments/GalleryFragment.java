package com.smarttersstudio.jewellaryadmin.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarttersstudio.jewellaryadmin.Pojo.Gallery;
import com.smarttersstudio.jewellaryadmin.R;
import com.smarttersstudio.jewellaryadmin.viewholder.GalleryViewHolder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class GalleryFragment extends Fragment {
    private AppCompatActivity a;
    private View v;
    private RecyclerView list;
    private DatabaseReference galleryRef;
    private FloatingActionButton addPhoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_gallery, container, false);
        a=(AppCompatActivity)getActivity();
        a.getSupportActionBar().setTitle("Gallery");
        NavigationView nav=a.findViewById(R.id.nav_view);
        nav.setCheckedItem(R.id.nav_gallery);
        list=v.findViewById(R.id.gallery_list);
        addPhoto=v.findViewById(R.id.add_photo);
        list.setHasFixedSize(true);
        list.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        galleryRef=FirebaseDatabase.getInstance().getReference().child("gallery");
        FirebaseRecyclerOptions<Gallery> options=new FirebaseRecyclerOptions.Builder<Gallery>()
                                                        .setQuery(galleryRef,Gallery.class)
                                                        .build();
        FirebaseRecyclerAdapter<Gallery,GalleryViewHolder> f=new FirebaseRecyclerAdapter<Gallery, GalleryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GalleryViewHolder holder, int position, @NonNull Gallery model) {
                holder.setImage(model.getImage(),a);
            }

            @NonNull
            @Override
            public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new GalleryViewHolder(LayoutInflater.from(a).inflate(R.layout.gallery_row,viewGroup,false));
            }
        };
        f.startListening();
        list.setAdapter(f);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(a);
            }
        });
        return v;
    }

}
