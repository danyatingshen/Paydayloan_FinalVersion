package com.example.chinmakoto.pay_day_loan;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinmakoto.pay_day_loan.Common.Common;
import com.example.chinmakoto.pay_day_loan.Database.Database;
import com.example.chinmakoto.pay_day_loan.Model.Order;
import com.example.chinmakoto.pay_day_loan.Model.Request;
import com.example.chinmakoto.pay_day_loan.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FloatingActionButton delete;

    List<Order> cart=new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase:
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //Init:
        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)findViewById(R.id.total);

        delete=(FloatingActionButton)findViewById(R.id.delete);


        //delete cart:
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Delete Complete",Toast.LENGTH_LONG).show();
                finish();
            }
        });


        //btnPlace=(FButton)findViewById(R.id.btnPlaceOrder);

        /*btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();

            }
        });
*/

        loadListFood();

    }

    /*private void showAlertDialog() {
        AlertDialog.Builder alertDioalog=new AlertDialog.Builder(Cart.this);
        alertDioalog.setTitle("One More Step To GO");
        alertDioalog.setMessage("Enter your public key Address for your Wallet:");

        final EditText edtpublic_key=new EditText(Cart.this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtpublic_key.setLayoutParams(lp);
        alertDioalog.setView(edtpublic_key);
        alertDioalog.setIcon(R.drawable.ic_assignment_black_24dp);

        alertDioalog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //create new request:
                Request request=new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtpublic_key.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                //firebase submit:

                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                //delete cart:
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank you, lottery Place",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        alertDioalog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDioalog.show();


    }*/

    private void loadListFood() {
        cart=new Database(this).getCarts();
        adapter=new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //total price:
        int total =0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*1;

        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));




    }
}
























