package com.smarttersstudio.jewellaryadmin.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarttersstudio.jewellaryadmin.Pojo.Enquiry;
import com.smarttersstudio.jewellaryadmin.R;
import com.smarttersstudio.jewellaryadmin.viewholder.EnquiryViewHolder;

public class EnquiryFragment extends Fragment {
    private AppCompatActivity a;
    private RecyclerView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_enquiry, container, false);
        a=(AppCompatActivity)getActivity();
        a.getSupportActionBar().setTitle("Enquiries");
        NavigationView nav=a.findViewById(R.id.nav_view);
        nav.setCheckedItem(R.id.nav_enquiry);
        list=v.findViewById(R.id.enquiry_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(a,LinearLayoutManager.VERTICAL,false));
        DatabaseReference enquiryRef=FirebaseDatabase.getInstance().getReference().child("enquiry");
        FirebaseRecyclerOptions<Enquiry> options=new FirebaseRecyclerOptions.Builder<Enquiry>()
                .setQuery(enquiryRef,Enquiry.class)
                .build();
        FirebaseRecyclerAdapter<Enquiry,EnquiryViewHolder> f=new FirebaseRecyclerAdapter<Enquiry, EnquiryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final EnquiryViewHolder holder, int position, @NonNull Enquiry model) {
                holder.setCall(model.getNumber(),a);
                holder.setDesc(model.getDesc());
                holder.setItem(model.getGos(),model.getType(),model.getId(),a);
                holder.setName(model.getName());
                holder.close(getRef(position));
                holder.mainCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.cnt == 0) {
                            holder.hiddenPart.setVisibility(View.VISIBLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.mainCard.setCardElevation(20);
                            }
                            holder.cnt = 1;
                        }
                        else{
                            holder.hiddenPart.setVisibility(View.GONE);
                            holder.mainCard.setCardElevation(0);
                            holder.cnt = 0;
                        }
                    }
                });
            }

            @Override
            public EnquiryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new EnquiryViewHolder(LayoutInflater.from(a).inflate(R.layout.enquiry_row,parent,false));
            }
        };
        f.startListening();
        list.setAdapter(f);
        return v;
    }


}
