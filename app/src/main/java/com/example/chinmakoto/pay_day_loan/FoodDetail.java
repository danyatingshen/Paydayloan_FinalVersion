package com.example.chinmakoto.pay_day_loan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinmakoto.pay_day_loan.Common.Common;
import com.example.chinmakoto.pay_day_loan.Database.Database;
import com.example.chinmakoto.pay_day_loan.Model.Food;
import com.example.chinmakoto.pay_day_loan.Model.Lottery;
import com.example.chinmakoto.pay_day_loan.Model.Order;
import com.example.chinmakoto.pay_day_loan.Model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.Manifest;

public class FoodDetail extends AppCompatActivity {
    TextView food_name, food_price, food_description, food_phone, food_time;
    ImageView food_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentFood;

    //fire base submit after call:
    List<Order> cart = new ArrayList<>();
    private static List Alreadyview=new ArrayList<>();
    DatabaseReference requests;
    DatabaseReference lotterys;
    static FoodDetail foodDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodDetail = this;
        setContentView(R.layout.activity_food_detail);


        //firebase:
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");

        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        food_description = (TextView) findViewById(R.id.food_description);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_phone = (TextView) findViewById(R.id.food_phone);
        food_time = (TextView) findViewById(R.id.food_time);
        food_image = (ImageView) findViewById(R.id.img_food);

        //food_phone=(String) findViewById(R.id.btnCart);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //after call firebase
        requests = database.getReference("Requests");
        lotterys=database.getReference("Lottery");

        //get food id:

        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()) {
            getDetailFood(foodId);

        }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //get phone number
        //call to make record
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(FoodDetail.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(FoodDetail.this, Manifest.permission.READ_CALL_LOG)) {
                        ActivityCompat.requestPermissions(FoodDetail.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                    } else {
                        ActivityCompat.requestPermissions(FoodDetail.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                    }
                } else {
                    //if the person has never called
                    if (!Alreadyview.contains(foodId)){
                        //////////Make the call:did not call beofre
                        String phoneNumber = food_phone.getText().toString();
                        Intent call = new Intent(Intent.ACTION_CALL);
                        call.setData(Uri.parse("tel:" +phoneNumber ));
                        Toast.makeText(FoodDetail.this, "Make the call", Toast.LENGTH_SHORT).show();
                        startActivity(call);}
                        else//the person has already called
                    {
                        Toast.makeText(FoodDetail.this,"You already called this Store, Please try another one!", Toast.LENGTH_SHORT).show();
                    }
                   //////////////check the duration:
                        if (ActivityCompat.checkSelfPermission(foodDetail, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(foodDetail, Manifest.permission.READ_CALL_LOG)) {
                                ActivityCompat.requestPermissions(foodDetail, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                            } else {
                                ActivityCompat.requestPermissions(foodDetail, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                            }
                        } else {
                            //////////get the duration:
                            Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                    null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

                            int duration = cur.getColumnIndex(CallLog.Calls.DURATION);
                            String callDuration = null;
                            while (cur.moveToNext()) {
                                callDuration = cur.getString(duration);
                                Toast.makeText(foodDetail, "Duration is " + callDuration, Toast.LENGTH_SHORT).show();

                            }
                            int callDurationInt = Integer.parseInt(callDuration);
                            int foodTimeInt = Integer.parseInt(food_time.getText().toString());


                            ///check the duration:
                            if (callDurationInt==foodTimeInt|callDurationInt>foodTimeInt)
                            {
                                new Database(getBaseContext()).addToCart(new Order(
                                        foodId,
                                        currentFood.getName(),
                                        currentFood.getPrice()
                                ));

                                ///////////to firebase:
                                Request request = new Request(
                                        Common.currentUser.getPhone(),
                                        Common.currentUser.getName(),
                                        Common.currentUser.getPhone(),
                                        food_price.getText().toString(),
                                        cart
                                );

                                Lottery lottery=new Lottery(Common.currentUser.getPhone());


                                lotterys.child(String.valueOf(System.currentTimeMillis())).setValue(lottery);
                                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                                Alreadyview.add(foodId);
                            }else
                            {
                                Toast.makeText(FoodDetail.this,"You Didn't pass the minimum duration!", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }

                }

        });
    }


    public static String getLastCallDetails(Context context) {

        //CallDetails callDetails = new CallDetails();

        Uri contacts = CallLog.Calls.CONTENT_URI;
        try {

            Cursor managedCursor = context.getContentResolver().query(contacts, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int incomingtype = managedCursor.getColumnIndex(String.valueOf(CallLog.Calls.INCOMING_TYPE));

            if(managedCursor.moveToFirst()){ // added line

                while (managedCursor.moveToNext()) {
                    String callType;
                    String phNumber = managedCursor.getString(number);
                  //  String callerName = getContactName(context, phNumber);
                    if(incomingtype == -1){
                        callType = "incoming";
                    }
                    else {
                        callType = "outgoing";
                    }
                    String callDate = managedCursor.getString(date);
                    String callDayTime = new      Date(Long.valueOf(callDate)).toString();

                    String callDuration = managedCursor.getString(duration);
                    Toast.makeText(context, "call duration is "+callDuration, Toast.LENGTH_SHORT).show();

//                    callDetails.setCallerName(callerName);
//                    callDetails.setCallerNumber(phNumber);
//                    callDetails.setCallDuration(callDuration);
//                    callDetails.setCallType(callType);
//                    callDetails.setCallTimeStamp(callDayTime);
                    return callDuration;

                }
            }
            managedCursor.close();

        } catch (SecurityException e) {
            Log.e("Security Exception", "User denied call log permission");

        }

        return "";

    }

/*    public void saveCallDurationToFirebase(long duration)

    {   if (ActivityCompat.checkSelfPermission(foodDetail, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(foodDetail, Manifest.permission.READ_CALL_LOG)) {
            ActivityCompat.requestPermissions(foodDetail, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        } else {
            ActivityCompat.requestPermissions(foodDetail, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
    } else {
        Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

        int durationa = cur.getColumnIndex(CallLog.Calls.DURATION);
        Toast.makeText(foodDetail, "Duration is " + durationa, Toast.LENGTH_SHORT).show();
        ///////create order: add to cart

        new Database(getBaseContext()).addToCart(new Order(
                foodId,
                currentFood.getName(),
                currentFood.getPrice()
        ));

        ///////////to firebase:
        Request request = new Request(
                Common.currentUser.getPhone(),
                Common.currentUser.getName(),
                Common.currentUser.getPhone(),
                food_price.getText().toString(),
                cart
        );

        //firebase submit:

        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
    }
    }*/

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_phone.setText(currentFood.getTell());
                food_time.setText(currentFood.getTime());
                food_description.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public  void readDuration()
    {
        if (ActivityCompat.checkSelfPermission(foodDetail, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(foodDetail, Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(foodDetail, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            } else {
                ActivityCompat.requestPermissions(foodDetail, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
        } else {


            //////////get the duration:
            Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

            int duration = cur.getColumnIndex(CallLog.Calls.DURATION);
            String callDuration = null;
            while (cur.moveToNext()) {
                callDuration = cur.getString(duration);
                Toast.makeText(foodDetail, "duration is " + callDuration, Toast.LENGTH_SHORT).show();
                //Toast.makeText(foodDetail, "requirement is " + food_time, Toast.LENGTH_SHORT).show();

            }
            int callDurationInt = Integer.parseInt(callDuration);
            int foodTimeInt = Integer.parseInt(food_time.getText().toString());
            if (callDurationInt==foodTimeInt|callDurationInt>foodTimeInt)
            {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        currentFood.getPrice()
                ));

                ///////////to firebase:
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        Common.currentUser.getPhone(),
                        food_price.getText().toString(),
                        cart
                );

                Lottery lottery=new Lottery(Common.currentUser.getPhone());


                lotterys.child(String.valueOf(System.currentTimeMillis())).setValue(lottery);
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                Alreadyview.add(foodId);
            }

        }
    }

    public static class CallDurationReceiver extends BroadcastReceiver {

        static boolean flag = false;
        static long start_time, end_time;

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_RINGING)) {
                    start_time = System.currentTimeMillis();
                }
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_IDLE)) {
                    end_time = System.currentTimeMillis();
                    //Total time talked =
                    long total_time = end_time - start_time;
                    total_time = total_time / 1000;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            foodDetail.readDuration();
                        }
                    }, 1000);



                    //////////get the duration:


                   // foodDetail.saveCallDurationToFirebase(total_time);
                   // getLastCallDetails(foodDetail);

                    //Store total_time somewhere or pass it to an Activity using intent

                }
            }
        }
    }
}
















