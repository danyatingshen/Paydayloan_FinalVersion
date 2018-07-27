package com.example.chinmakoto.pay_day_loan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chinmakoto.pay_day_loan.Model.Food;
import com.example.chinmakoto.pay_day_loan.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    TextView personal_Name,personal_Phone,personal_Wallet,personal_Password;

    User currentuser;
    DatabaseReference table_user;
    String foodId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_user=database.getReference("User");

        personal_Name=(TextView)findViewById(R.id.personal_name);
        personal_Phone=(TextView)findViewById(R.id.personal_phone);
        personal_Wallet=(TextView)findViewById(R.id.personal_wallet);
        personal_Password=(TextView)findViewById(R.id.personal_password);

            getUser(foodId);

        }


    private void getUser(String foodId) {
        table_user.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentuser  = dataSnapshot.getValue(User.class);
                personal_Name.setText(currentuser.getName());
                personal_Phone.setText(currentuser.getPhone());
                personal_Wallet.setText(currentuser.getPhone());
                personal_Password.setText(currentuser.getPassword());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
