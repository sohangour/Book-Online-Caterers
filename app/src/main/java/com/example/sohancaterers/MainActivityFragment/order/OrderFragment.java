package com.example.sohancaterers.MainActivityFragment.order;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohancaterers.AdapterClass.NoteOrderAdapter;
import com.example.sohancaterers.ModelClass.PlaceOrder;
import com.example.sohancaterers.R;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;


public class OrderFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private CollectionReference collectionReference;
    private NoteOrderAdapter noteOrderAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        if (user == null) {
            Toast.makeText(getContext(), "Login First to see order Details", Toast.LENGTH_SHORT).show();

        } else {

            recyclerView = view.findViewById(R.id.order_recycler_view);
            setUprecyclerView();

        }
        return view;
    }

    private void setUprecyclerView() {
        String email = user.getUid();
        collectionReference = firebaseFirestore.collection("UserOrder").document(email).collection(email);
        Query query = collectionReference;
        FirestoreRecyclerOptions<PlaceOrder> options = new FirestoreRecyclerOptions.Builder<PlaceOrder>()
                .setQuery(query, PlaceOrder.class)
                .build();
        noteOrderAdapter = new NoteOrderAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(noteOrderAdapter);
        noteOrderAdapter.setOnItemClickListener(new NoteOrderAdapter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(getContext(), OrderItemListActivity.class);
                PlaceOrder placeOrder = documentSnapshot.toObject(PlaceOrder.class);
                placeOrder.setDocumentId(documentSnapshot.getId());
                String id = placeOrder.getDocumentId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (noteOrderAdapter != null)
            noteOrderAdapter.startListening();
    }


}//main class