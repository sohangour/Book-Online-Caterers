package com.example.sohancaterers.AdapterClass;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sohancaterers.ModelClass.PlaceOrder;
import com.example.sohancaterers.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NoteOrderAdapter extends FirestoreRecyclerAdapter<PlaceOrder, NoteOrderAdapter.OrderHolder> {
public onItemClickListner listner;

    public NoteOrderAdapter(@NonNull FirestoreRecyclerOptions<PlaceOrder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final OrderHolder orderHolder, int i, @NonNull final PlaceOrder placeOrder) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(i);
          placeOrder.setDocumentId(documentSnapshot.getId());
             orderHolder.event_name.setText(placeOrder.getEvent_name().toUpperCase());
             orderHolder.event_date.setText(placeOrder.getEvent_date());
             orderHolder.event_time.setText(placeOrder.getEvent_time());
             orderHolder.total_member.setText("Total Member:"+String.valueOf(placeOrder.getTotal_member()));
             orderHolder.name.setText(placeOrder.getCustomer_name().toUpperCase());
             orderHolder.venue_address.setText(placeOrder.getVenue_address().toUpperCase());
             orderHolder.per_member.setText(("Per Member Amount:"+placeOrder.getPer_member_price()));
             int total=placeOrder.getTotal_amount();
             String totalamount="Total Amount: Rs. "+total;
             orderHolder.total_amount.setText(totalamount);


    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_category,parent,false);
        OrderHolder orderHolder=new OrderHolder(view);
        return orderHolder;
    }

    class OrderHolder extends RecyclerView.ViewHolder {
        private TextView event_name,event_date,event_time, name, status, total_amount,total_member,venue_address,per_member;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.event_name);
            event_date = itemView.findViewById(R.id.event_date);
            event_time = itemView.findViewById(R.id.event_time);
            total_member=itemView.findViewById(R.id.total_member);
            per_member=itemView.findViewById(R.id.per_member_price);
            name = itemView.findViewById(R.id.order_name);
            venue_address = itemView.findViewById(R.id.venue_address);
            total_amount = itemView.findViewById(R.id.order_total_amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                int position=getAdapterPosition();
                listner.onItemClick(getSnapshots().getSnapshot(position),position);
                }
            });
        }
    }
    public interface onItemClickListner{
    void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(onItemClickListner listener){
       this.listner=listener;

    }
}
