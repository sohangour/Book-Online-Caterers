package com.example.sohancaterers.AdapterClass;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.ModelClass.NoteItem;
import com.example.sohancaterers.R;

import com.example.sohancaterers.database.Database;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class NoteItemAdapter extends FirestoreRecyclerAdapter<NoteItem, NoteItemAdapter.NoteHolder> {
    List<Order> list = new ArrayList<>();

    public NoteItemAdapter(@NonNull FirestoreRecyclerOptions<NoteItem> options) {
        super(options);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_category, parent, false);
        return new NoteHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NoteHolder noteHolder, final int i, @NonNull final NoteItem noteItem) {
        noteHolder.item.setText(noteItem.getItem());
        int pr = noteItem.getPrice();
        String priceItem = "Rs.";
        String concat = priceItem + String.valueOf(pr);
        noteHolder.price.setText(concat);
        Glide.with(noteHolder.itemView.getContext()).load(noteItem.getUrl()).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(30))).into(noteHolder.url);

        noteHolder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking for selecting duplicate cart view

                boolean temp = true;
                list = new Database(noteHolder.itemView.getContext()).getDataFromDB();
                int size = list.size();
                //checking duplication inside cart
                for (int i = 0; i < size; i++) {
                    String item = list.get(i).getItem_name();
                    if (noteItem.getItem().equals(item)) {
                        Toast.makeText(noteHolder.itemView.getContext(), "Item Already Added", Toast.LENGTH_SHORT).show();
                        temp = false;
                    }
                }//for loop

                //inset if we dont get that item into cart

                if (temp == true) {
                    new Database(noteHolder.itemView.getContext()).addToCart(new Order(
                            noteItem.getItem(),
                            noteItem.getPrice()
                    ));
                    Toast.makeText(noteHolder.itemView.getContext(),"Successfully Added " +noteItem.getItem() , Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        public ImageView url;
        public TextView item, price;
        public CardView item_card_view;
        public TextView addToCart;
        public TextView alreadyAdded;

        public NoteHolder(@NonNull final View itemView) {
            super(itemView);
            url = itemView.findViewById(R.id.url);
            item = itemView.findViewById(R.id.item);
            price = itemView.findViewById(R.id.price);
            item_card_view = itemView.findViewById(R.id.item_card_view);
            addToCart = itemView.findViewById(R.id.add_to_cart);


        }
    }

}
