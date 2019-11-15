package com.example.sohancaterers.MainActivityFragment.order;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sohancaterers.AdapterClass.NoteOrderItemAdapter;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.ModelClass.PlaceOrder;
import com.example.sohancaterers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;


public class OrderItemListActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    List<Order> listOrder = new ArrayList<>();
    private RecyclerView recycler_orderitem_view;
    private String id;
    private NoteOrderItemAdapter orderitemAdapterNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_list);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        recycler_orderitem_view = findViewById(R.id.recycler_orderitem_view);
        documentReference = firebaseFirestore.collection("UserOrder").document(user.getUid())
                .collection(user.getUid()).document(id);
        AdapterSetting();
    }


    private void AdapterSetting() {
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    return;
                }
                if(documentSnapshot.exists()) {
                    final PlaceOrder placeOrder = documentSnapshot.toObject(PlaceOrder.class);
                    listOrder = placeOrder.getItemList();
                    recycler_orderitem_view.setHasFixedSize(true);
                    recycler_orderitem_view.setLayoutManager(new LinearLayoutManager(OrderItemListActivity.this));
                    orderitemAdapterNote = new NoteOrderItemAdapter(listOrder, OrderItemListActivity.this);
                    recycler_orderitem_view.setAdapter(orderitemAdapterNote);
                    orderitemAdapterNote.notifyDataSetChanged();
                    orderitemAdapterNote.setOnItemClickListener(new NoteOrderItemAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(OrderItemListActivity.this, placeOrder.getItemList().get(position).getItem_name(), Toast.LENGTH_SHORT).show();


                            orderitemAdapterNote.notifyItemChanged(position);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (orderitemAdapterNote != null) {
            orderitemAdapterNote.notifyDataSetChanged();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orderitemAdapterNote != null)
            orderitemAdapterNote.notifyDataSetChanged();
    }
}
