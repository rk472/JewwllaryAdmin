package com.smarttersstudio.jewellaryadmin.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarttersstudio.jewellaryadmin.AddItemActivity;
import com.smarttersstudio.jewellaryadmin.Pojo.Items;
import com.smarttersstudio.jewellaryadmin.R;
import com.smarttersstudio.jewellaryadmin.viewholder.ItemsViewHolder;


public class ItemFragment extends Fragment {
    private View v;
    private AppCompatActivity a;
    private RecyclerView list;
    private DatabaseReference itemRef;
    private Spinner gosSelect,categorySelect;
    private FloatingActionButton addFab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_item, container, false);
        a=(AppCompatActivity)getActivity();
        a=(AppCompatActivity)getActivity();
        a.getSupportActionBar().setTitle("Items");
        list=v.findViewById(R.id.item_list);
        list.setLayoutManager(new GridLayoutManager(a,2,LinearLayoutManager.VERTICAL,false));
        list.setHasFixedSize(true);
        NavigationView nav=a.findViewById(R.id.nav_view);
        nav.setCheckedItem(R.id.nav_items);
        gosSelect=v.findViewById(R.id.spinner_gos);
        categorySelect=v.findViewById(R.id.spinner_category);
        addFab=v.findViewById(R.id.add_item);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(a,AddItemActivity.class);
                i.putExtra("gos",gosSelect.getSelectedItem().toString());
                i.putExtra("category",categorySelect.getSelectedItem().toString());
                startActivity(i);
            }
        });
        gosSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gos=gosSelect.getSelectedItem().toString();
                String category=categorySelect.getSelectedItem().toString();
                refresh(gos,category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gos=gosSelect.getSelectedItem().toString();
                String category=categorySelect.getSelectedItem().toString();
                if(category.equals("coins and bars")) category="coin";
                refresh(gos,category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //refresh("gold","necklace");
        return v;
    }
    void refresh(String gos,String category){
        itemRef=FirebaseDatabase.getInstance().getReference().child("items").child(gos).child(category);
        FirebaseRecyclerOptions<Items> options=new FirebaseRecyclerOptions.Builder<Items>()
                .setQuery(itemRef,Items.class)
                .build();
        FirebaseRecyclerAdapter<Items,ItemsViewHolder> f=new FirebaseRecyclerAdapter<Items, ItemsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemsViewHolder holder, int position, @NonNull Items model) {
                holder.setImage(model.getImage(),getActivity());
                holder.setSold(model.getSold());
                holder.setName(model.getName());
                holder.remove(getRef(position));
            }

            @NonNull
            @Override
            public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ItemsViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.all_items_row,parent,false));
            }
        };
        f.startListening();
        list.setAdapter(f);
    }

}
