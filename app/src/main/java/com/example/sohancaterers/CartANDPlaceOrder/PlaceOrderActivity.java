package com.example.sohancaterers.CartANDPlaceOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sohancaterers.MainActivity;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.ModelClass.OrderEmailList;
import com.example.sohancaterers.ModelClass.PlaceOrder;
import com.example.sohancaterers.R;
import com.example.sohancaterers.database.Database;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.sohancaterers.App.ORDER_PLACED;

public class PlaceOrderActivity extends AppCompatActivity {
    private EditText total_member;  // 1   total member
    private Spinner spinnerEvent;       //2      select event
    private TextView event_date;         //3     select date
    private Spinner spinnerTime;  //4  select time for event  //5 number is only text view
    private TextView total_amount; //6
    private EditText customer_name, customer_email, customer_contact, customer_address, venue_name, venue_address;
    private String event, time, per_member_price;
    private String name, contact, email;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private List<Order> placelist = new ArrayList<>();
    private Database database;
    private ProgressDialog progressDialog;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        notificationManagerCompat=NotificationManagerCompat.from(this);
        progressDialog = new ProgressDialog(this);
        //find view by id
        total_member = findViewById(R.id.total_member);

        spinnerEvent = findViewById(R.id.spinner); ///event spinner

        event_date = findViewById(R.id.event_date);

        spinnerTime = findViewById(R.id.spinnerTime); //time spinner

        total_amount = findViewById(R.id.total_amount);
        customer_name = findViewById(R.id.customer_name);
        customer_email = findViewById(R.id.customer_email);
        customer_contact = findViewById(R.id.customer_contact);
        customer_address = findViewById(R.id.customer_address);
        venue_name = findViewById(R.id.venue_name);
        venue_address = findViewById(R.id.venue_address);
        // get value from  previous activity
        Intent in = getIntent();
        per_member_price = in.getExtras().getString("per_member_price");


        //total member textWatcher
        total_member.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int amt = Integer.parseInt(per_member_price);
                    if (s.toString().isEmpty()) {
                        total_amount.setText(String.valueOf(0));
                    } else {
                        int cal = Integer.parseInt(s.toString());
                        total_amount.setText(String.valueOf(cal * amt));

                    }


                } catch (Exception e) {
                    String TAG_NEW = "hello";
                    Log.i(TAG_NEW, "afterTextChanged: " + e.getMessage());
                }
            }
        });
        database = new Database(this);
        placelist = database.getDataFromDB();
        //event name list
        List<String> eventname = new ArrayList<>();
        eventname.add(0, "Select Event");
        eventname.add("Marriage");
        eventname.add("Reception");
        eventname.add("Birthday Party");
        eventname.add("Engagement");
        eventname.add("Party");

        //time slot list
        List<String> timeslot = new ArrayList<>();
        timeslot.add(0, "Select Time Slot");
        timeslot.add("7:00 AM");
        timeslot.add("8:00 AM");
        timeslot.add("9:00 AM");
        timeslot.add("10:00 AM");
        timeslot.add("11:00 AM");
        timeslot.add("12:00 PM");
        timeslot.add("1:00 PM");
        timeslot.add("2:00 PM");
        timeslot.add("3:00 PM");
        timeslot.add("4:00 PM");
        timeslot.add("5:00 PM");
        timeslot.add("6:00 PM");
        timeslot.add("7:00 PM");
        timeslot.add("8:00 PM");
        timeslot.add("9:00 PM");
        timeslot.add("10:00 PM");


        // style spinner for event
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, eventname);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvent.setAdapter(arrayAdapter);
        //spinner for time slot
        ArrayAdapter<String> arrayAdapterTime = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timeslot);
        arrayAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(arrayAdapterTime);

        //Spinner method on event
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Event")) {
                } else {
                    event = parent.getItemAtPosition(position).toString();
                    Toast.makeText(PlaceOrderActivity.this, event, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //time slot method
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Time Slot")) {
                } else {
                    time = parent.getItemAtPosition(position).toString();
                    Toast.makeText(PlaceOrderActivity.this, time, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // for date selection
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, 2);

        event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(PlaceOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if (day < 10) {
                            String dt = "0" + day;
                            month = month + 1;
                            String datePut = dt + "/" + month + "/" + year;
                            event_date.setText(datePut);
                        } else {
                            month = month + 1;
                            String datePut = day + "/" + month + "/" + year;
                            event_date.setText(datePut);
                        }

                    }
                }, year, month, day);


                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        GetUserDetail(); // get user details from database
    } ///oncreate class

    private void GetUserDetail() {
        DocumentReference documentReference = firebaseFirestore.collection("UserDetail").document(user.getEmail());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("Name");
                    contact = documentSnapshot.getString("Contact");
                    email = documentSnapshot.getString("Email");
                    customer_name.setText(name);
                    customer_contact.setText(contact);
                    customer_email.setText(email);

                } else {
                    Toast.makeText(PlaceOrderActivity.this, "Document Doesnot Exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        GetUserDetail(); // get user details from database


    }

    public void placeOrder(View view) {
        if (total_member.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Total Member", Toast.LENGTH_SHORT).show();
            total_member.requestFocus();
            return;
        } else if (event == null) {
            Toast.makeText(this, "Select Event", Toast.LENGTH_SHORT).show();
            return;
        } else if (event_date.getText().toString().equals("Select Date")) {
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
            return;
        } else if (time == null) {
            Toast.makeText(this, "Select Time Slot", Toast.LENGTH_SHORT).show();
            return;
        } else if (customer_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            customer_name.requestFocus();
            return;
        } else if (customer_email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            customer_email.requestFocus();
            return;
        } else if (customer_contact.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Contact Number ", Toast.LENGTH_SHORT).show();
            customer_contact.requestFocus();
            return;
        } else if (customer_address.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Address", Toast.LENGTH_SHORT).show();
            customer_address.requestFocus();
            return;
        } else if (venue_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Venue Name Like (Kadam Lawns)", Toast.LENGTH_SHORT).show();
            venue_name.requestFocus();
            return;
        } else if (venue_address.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Venue Address", Toast.LENGTH_SHORT).show();
            venue_address.requestFocus();
            return;
        }


        progressDialog.setMessage("Order Placing......");
        progressDialog.show();
        String email = user.getUid();

        CollectionReference collectionReference = firebaseFirestore.collection("UserOrder").document(email)
                .collection(email); //where order stored
        PlaceOrder placeOrder = new PlaceOrder(
                Integer.parseInt(per_member_price),
                Integer.parseInt(total_member.getText().toString()),
                event,
                event_date.getText().toString(),
                time,
                Integer.parseInt(total_amount.getText().toString()),
                customer_name.getText().toString(),
                customer_email.getText().toString(),
                customer_contact.getText().toString(),
                customer_address.getText().toString(),
                venue_name.getText().toString(),
                venue_address.getText().toString(),
                placelist);
        DocumentReference documentReference = firebaseFirestore.collection("UserOrder").document(email);
        OrderEmailList orderEmailList = new OrderEmailList(email);
        documentReference.set(orderEmailList);
        collectionReference.add(placeOrder)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        Toast.makeText(PlaceOrderActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();

                        Notification notification=new NotificationCompat.Builder(PlaceOrderActivity.this,ORDER_PLACED)
                                .setSmallIcon(R.drawable.ic_cart)
                                .setContentTitle("Order Placed")
                                .setContentText( customer_email.getText().toString())
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .build();
                        notificationManagerCompat.notify(2,notification);

                        database.cleanCart();
                        finish();
                        startActivity(new Intent(PlaceOrderActivity.this, MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

    }
}//main class
