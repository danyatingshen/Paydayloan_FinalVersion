package com.example.chinmakoto.pay_day_loan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.chinmakoto.pay_day_loan.Interface.ItemClickListener;
import com.example.chinmakoto.pay_day_loan.Model.Food;
import com.example.chinmakoto.pay_day_loan.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;



public class SearchActivityforBid extends AppCompatActivity {

    //seach actvity
    FirebaseRecyclerAdapter<Food,FoodViewHolder>adapter;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Firebase:
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Key Word");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //when user type in, suggest change
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);

                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When searchbar closed setore original suggest list
                if (!enabled)
                    recyclerView.setAdapter(adapter);

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when seach over, show result of search adapter
                startSearch(text);


            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        loadAllFood();
    }

    private void loadAllFood() {
        foodList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren() )
                {
                    Food item=postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                    //suggestList.add(item.getBid());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,

                foodList.orderByChild("Name").equalTo(text.toString())



        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                //viewHolder.food_bid.setText(model.getBid());
                viewHolder.food_name.setText((CharSequence) model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
                final Food local =model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent foodDetail = new Intent(SearchActivityforBid.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",searchAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren() )
                {
                    Food item=postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                    //suggestList.add(item.getBid());
                }
                materialSearchBar.setLastSuggestions(suggestList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*@Override
    protected void onStop(){
        if (adapter!=null)
            //adapter.stopListening();
        super.onStop();

    }*/

}



