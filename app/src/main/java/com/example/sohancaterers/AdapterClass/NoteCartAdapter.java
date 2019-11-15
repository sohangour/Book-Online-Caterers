package com.example.sohancaterers.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.example.sohancaterers.ModelClass.Order;
import com.example.sohancaterers.R;
import com.example.sohancaterers.database.Database;

import java.util.ArrayList;
import java.util.List;

public class NoteCartAdapter extends RecyclerView.Adapter<NoteCartAdapter.ViewHolder> {

    public static List<Order> dbList;
    public static Context context;
    public static Database db;
    private static ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemClickListener myClickListener) {
        NoteCartAdapter.itemClickListener = myClickListener;
    }

    public NoteCartAdapter(List<Order> dbList, Context context) {
        this.dbList = new ArrayList<>();
        this.context = context;
        this.dbList = dbList;
        db = new Database(context);
    }

    @Override
    public NoteCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_my_cart_category, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteCartAdapter.ViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(position + 1), Color.RED);
        holder.cart_item_count.setImageDrawable(drawable);
        holder.item_name.setText(dbList.get(position).getItem_name());
        holder.price.setText((dbList.get(position).getPrice())+"/-");

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView item_name, price;
        public ImageView delete, cart_item_count;
        public TextView total_price;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            item_name = itemLayoutView
                    .findViewById(R.id.cart_item_name);
            price = itemLayoutView.findViewById(R.id.cart_price);
            delete = itemLayoutView.findViewById(R.id.delete);
            cart_item_count = itemLayoutView.findViewById(R.id.cart_item_count);
            total_price = itemLayoutView.findViewById(R.id.total);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition());
            db.delete(dbList.get(getAdapterPosition()).getItem_name());
            dbList.remove(getAdapterPosition());

            //  notifyDataSetChanged();
            notifyDataSetChanged();
            Toast.makeText(NoteCartAdapter.context, "Removed Item Successfully ", Toast.LENGTH_LONG).show();
        }
    }

}
