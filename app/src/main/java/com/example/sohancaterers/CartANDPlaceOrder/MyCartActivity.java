package com.example.sohancaterers.CartANDPlaceOrder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohancaterers.AdapterClass.NoteCartAdapter;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.R;
import com.example.sohancaterers.database.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyCartActivity extends AppCompatActivity {
    private Database database;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private TextView total_price_member;
    private Button next;
    private NoteCartAdapter noteCartAdapter;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private List<Order> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().setTitle("Your Cart List");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        recyclerView = findViewById(R.id.listCart);
        total_price_member = findViewById(R.id.total);
        next = findViewById(R.id.next);
        user = firebaseAuth.getCurrentUser();
        database = new Database(this);

        AdapterSetting();
        loadListFood();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteCartAdapter.getItemCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Your Cart Is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    String total_price_membe=total_price_member.getText().toString();

                    Intent intent=new Intent(MyCartActivity.this, PlaceOrderActivity.class);
                    intent.putExtra("per_member_price",total_price_membe);
                    startActivity(intent);
                }
            }
            //  ShowAlertDialog();


        });
        noteCartAdapter.setOnItemClickListener(new NoteCartAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int pr = cart.get(position).getPrice();
                int total = Integer.parseInt(total_price_member.getText().toString());
                int result = total - pr;
                total_price_member.setText(String.valueOf(result));
            }
        });
    }




    private void AdapterSetting() {
        collectionReference = firebaseFirestore.collection("UserOrder").document(user.getEmail())
                .collection(user.getEmail()); //where order stored
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        cart = database.getDataFromDB();
        noteCartAdapter = new NoteCartAdapter(cart, this);
        recyclerView.setAdapter(noteCartAdapter);
        noteCartAdapter.notifyDataSetChanged();
    }


    private void loadListFood() {
        int total = 0;
        for (Order order : cart) {
            total += order.getPrice();
        }
        total_price_member.setText(Integer.toString(total));


    }


    @Override
    public void onStart() {
        super.onStart();
        if (noteCartAdapter != null) {
            noteCartAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}//main class
/*

 private void ShowAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Some Information");
        //  alert.setMessage("Enter Address");
        alert.setIcon(R.drawable.ic_add_shoppin);


        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText title = new EditText(getApplicationContext());
        title.setHint("Enter Order Title");
        layout.addView(title); // Another add method

        // Add a TextView here for the "Address" label
        final EditText address = new EditText(getApplicationContext());
        address.setHint("Enter Address");
        layout.addView(address); // Notice this is an add method

        // Add another TextView here for the "Description" label
        final EditText person = new EditText(getApplicationContext());
        person.setHint("Enter Total Member");
        layout.addView(person); // Another add method

        alert.setView(layout);

        alert.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get user details from database
                int price = Integer.parseInt(total_price.getText().toString());
                PlaceOrder placeOrder = new PlaceOrder(
                        title.getText().toString(),
                        name,
                        contact,
                        address.getText().toString(),
                        person.getText().toString(),
                        price * Integer.parseInt(person.getText().toString()),
                        cart
                );
                collectionReference.add(placeOrder)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyCartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                database.cleanCart();
                noteCartAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Thank You,Your Order Placed Successfully...we will contact with you", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
        alert.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();


    }
 */