package com.example.sohancaterers.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.R;

import java.util.ArrayList;
import java.util.List;

public class NoteOrderItemAdapter extends RecyclerView.Adapter<NoteOrderItemAdapter.OrderItemHolder> {
    public static List<Order> List;
    public static Context context;
    public OnItemClickListener listner;

    public NoteOrderItemAdapter(List<Order> List, Context context) {
        this.List = new ArrayList<>();
        this.context = context;
        this.List = List;

    }


    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_item_list_category, parent, false);
        OrderItemHolder orderItemHolder = new OrderItemHolder(view);
        return orderItemHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder orderItemHolder, int i) {

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(i + 1), Color.RED);
        orderItemHolder.cart_orderitem_count.setImageDrawable(drawable);
        orderItemHolder.cart_orderitem_name.setText(List.get(i).getItem_name().toUpperCase());
        orderItemHolder.cart_orderprice.setText((List.get(i).getPrice()) + "/-");



    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class OrderItemHolder extends RecyclerView.ViewHolder {
        public TextView cart_orderitem_name, cart_orderprice;
        public ImageView order_delete, cart_orderitem_count;

        public OrderItemHolder(@NonNull final View itemView) {

            super(itemView);
            cart_orderitem_name = itemView.findViewById(R.id.cart_orderitem_name);
            cart_orderprice = itemView.findViewById(R.id.cart_orderprice);
            order_delete = itemView.findViewById(R.id.order_delete);
            cart_orderitem_count = itemView.findViewById(R.id.cart_orderitem_count);
            order_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listner.onItemClick(position);
                    notifyDataSetChanged();

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listner = listener;
    }

}
