package com.smarttersstudio.jewellaryadmin.fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.jewellaryadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SearchFragment extends Fragment {
    private Spinner gosSelect,typeSelect;
    private EditText idText;
    private Button searchButton;
    private ImageView img;
    private TextView nameText;
    private View v;
    private AppCompatActivity a;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_search, container, false);
        a=(AppCompatActivity)getActivity();
        a.getSupportActionBar().setTitle("Search Item");
        NavigationView navView=a.findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_search);
        gosSelect=v.findViewById(R.id.search_spinner_gos);
        typeSelect=v.findViewById(R.id.search_spinner_category);
        idText=v.findViewById(R.id.search_id);
        searchButton=v.findViewById(R.id.search_button);
        img=v.findViewById(R.id.search_image);
        nameText=v.findViewById(R.id.search_name);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id=idText.getText().toString().trim();
                if(!TextUtils.isEmpty(id)){
                    final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("items").child(gosSelect.getSelectedItem().toString()).child(typeSelect.getSelectedItem().toString());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(id)){
                                ref.child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {
                                        Picasso.with(a).load(dataSnapshot.child("image").getValue(String.class)).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_image_black_24dp)
                                                .into(img, new Callback() {
                                                    @Override
                                                    public void onSuccess() {
                                                    }
                                                    @Override
                                                    public void onError() {
                                                        Picasso.with(a).load(dataSnapshot.child("image").getValue(String.class)).placeholder(R.drawable.ic_image_black_24dp).into(img);
                                                    }
                                                });
                                        nameText.setText(dataSnapshot.child("name").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }else{
                                Toast.makeText(a, "No results found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        return v;
    }

}
