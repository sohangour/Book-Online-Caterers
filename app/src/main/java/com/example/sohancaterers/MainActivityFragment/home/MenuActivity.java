package com.example.sohancaterers.MainActivityFragment.home;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sohancaterers.AdapterClass.NoteMenuAdapter;
import com.example.sohancaterers.CartANDPlaceOrder.MyCartActivity;
import com.example.sohancaterers.ModelClass.NoteMenu;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.R;
import com.example.sohancaterers.database.Database;
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

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity {
    private RecyclerView menu_recycler_view;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private NoteMenuAdapter noteMenuAdapter;
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private List<Order> cart = new ArrayList<>();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("Menu Category");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        menu_recycler_view = findViewById(R.id.menu_recycler_view);

        user = firebaseAuth.getCurrentUser();

        showMenuCategory();


    }


    private void showMenuCategory() {
        try {

            CollectionReference query = firebaseFirestore.collection("MENU");
            FirestoreRecyclerOptions<NoteMenu> options = new FirestoreRecyclerOptions.Builder<NoteMenu>()
                    .setQuery(query.orderBy("priority", Query.Direction.ASCENDING), NoteMenu.class).build();

            noteMenuAdapter = new NoteMenuAdapter(options);
            menu_recycler_view.setItemAnimator(null);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            gridLayoutManager.isAutoMeasureEnabled();
            menu_recycler_view.setLayoutManager(gridLayoutManager);
            menu_recycler_view.setNestedScrollingEnabled(false);
            menu_recycler_view.setHasFixedSize(true);
            menu_recycler_view.setAdapter(noteMenuAdapter);
            menu_recycler_view.setItemAnimator(null);
            noteMenuAdapter.notifyDataSetChanged();


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    } //show menu

    @Override
    public void onStart() {
        super.onStart();
        noteMenuAdapter.startListening();
      //  UpdateView();

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
                cart = new Database(getApplicationContext()).getDataFromDB();

                if (user == null) {
                    Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                } else if (cart.isEmpty()) {
                    Toast.makeText(this, "Cart Is Empty", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(this, MyCartActivity.class);
                    startActivity(i);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}


