package com.example.sohancaterers.MainActivityFragment.home;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sohancaterers.AdapterClass.NoteItemAdapter;
import com.example.sohancaterers.CartANDPlaceOrder.MyCartActivity;
import com.example.sohancaterers.MainActivity;
import com.example.sohancaterers.ModelClass.NoteItem;
import com.example.sohancaterers.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;


public class ItemActivity extends AppCompatActivity {
    private RecyclerView item_recycler_view;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private NoteItemAdapter noteItemAdapter;
    private String value;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().setTitle("Item Category");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        item_recycler_view = findViewById(R.id.item_recycler_view);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        if (getIntent() != null)
            value = getIntent().getExtras().getString("Menu");
        if (!value.isEmpty()) {
            showItemCategory();

        }
    }

    private void showItemCategory() {

        CollectionReference collectionReference = firebaseFirestore.collection("MENU").document(value).collection(value);
        FirestoreRecyclerOptions<NoteItem> options = new FirestoreRecyclerOptions.Builder<NoteItem>()
                .setQuery(collectionReference.orderBy("price", Query.Direction.DESCENDING), NoteItem.class).build();


        noteItemAdapter = new NoteItemAdapter(options);
        item_recycler_view.setItemAnimator(null);
        item_recycler_view.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        item_recycler_view.setAdapter(noteItemAdapter);
        item_recycler_view.setHasFixedSize(false);
        item_recycler_view.setNestedScrollingEnabled(false);
    } //close showItemCategory method


    @Override
    protected void onStart() {
        super.onStart();

        noteItemAdapter.startListening();
     //   UpdateView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_main_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_cart:
                if(user==null){
                    Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent i = new Intent(this, MyCartActivity.class);
                    startActivity(i);
                }
                return true;

            case android.R.id.home:
                  finish();
                  return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }




}
