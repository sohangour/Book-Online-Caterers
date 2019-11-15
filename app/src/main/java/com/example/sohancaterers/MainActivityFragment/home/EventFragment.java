package com.example.sohancaterers.MainActivityFragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.ViewPager;


import com.example.sohancaterers.AdapterClass.CustomeSwipeAdapter;
import com.example.sohancaterers.AdapterClass.NoteEventAdapter;
import com.example.sohancaterers.MainActivity;
import com.example.sohancaterers.ModelClass.NoteEvent;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.CartANDPlaceOrder.MyCartActivity;
import com.example.sohancaterers.R;
import com.example.sohancaterers.database.Database;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class EventFragment extends Fragment {
    private RecyclerView event_recycler_view;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private NoteEventAdapter noteEventAdapter;
    private Toolbar toolbar;
    private List<Order> cart = new ArrayList<>();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private ViewPager viewPager;
    private CustomeSwipeAdapter customeSwipeAdapter;
    private Timer timer;
    private int current_position = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_event, container, false);

        //image offer slider
        viewPager = view.findViewById(R.id.view_pager);
        customeSwipeAdapter = new CustomeSwipeAdapter(getContext());
        viewPager.setAdapter(customeSwipeAdapter);
        createSlideShow();


        event_recycler_view = view.findViewById(R.id.event_recycler_view);

        // show menu category
        showMenuCategory();
        return view;
    }


    private void showMenuCategory() {
        try {

            CollectionReference query = firebaseFirestore.collection("EVENT");
            FirestoreRecyclerOptions<NoteEvent> options = new FirestoreRecyclerOptions.Builder<NoteEvent>()
                    .setQuery(query, NoteEvent.class).build();

            noteEventAdapter = new NoteEventAdapter(options);
            event_recycler_view.setHasFixedSize(true);
            event_recycler_view.setLayoutManager(new GridLayoutManager(getContext(), 1));
            event_recycler_view.setAdapter(noteEventAdapter);
            RecyclerView.ItemAnimator animator = event_recycler_view.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    } //show menu

    @Override
    public void onStart() {
        super.onStart();
        noteEventAdapter.startListening();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.layout_main_top, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_cart:
                cart = new Database(getContext()).getDataFromDB();
                if (user == null) {
                    Toast.makeText(getContext(), "Login First", Toast.LENGTH_SHORT).show();
                } else if (cart.isEmpty()) {
                    Toast.makeText(getContext(), "Cart Is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getContext(), MyCartActivity.class);
                    startActivity(i);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void createSlideShow() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (current_position == 4) {
                    current_position = 0;

                }
                viewPager.setCurrentItem(current_position++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 250, 2500);

    }


}


