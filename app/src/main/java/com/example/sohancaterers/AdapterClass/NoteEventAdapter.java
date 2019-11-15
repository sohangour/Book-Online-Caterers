package com.example.sohancaterers.AdapterClass;


import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.sohancaterers.MainActivityFragment.home.MenuActivity;
import com.example.sohancaterers.ModelClass.NoteEvent;
import com.example.sohancaterers.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class NoteEventAdapter extends FirestoreRecyclerAdapter<NoteEvent, NoteEventAdapter.NoteHolder> {


    public NoteEventAdapter(@NonNull FirestoreRecyclerOptions<NoteEvent> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NoteHolder noteHolder, int i, @NonNull final NoteEvent noteEvent) {


        noteHolder.eventname.setText(noteEvent.getEventname());
        Glide.with(noteHolder.itemView.getContext()).load(noteEvent.getEventimageLink()).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50)))
                .placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error)
                .into(noteHolder.eventimageLink);

        //show image on another by touch
        noteHolder.event_card_view.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime <300) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(view.getContext(), MenuActivity.class);
                intent.putExtra("Event", noteEvent.getEventname());
                view.getContext().startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_category, parent, false);
        return new NoteHolder(v);
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        public ImageView eventimageLink;
        public TextView eventname;
        public CardView event_card_view;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            event_card_view = itemView.findViewById(R.id.event_card_view);
            eventimageLink = itemView.findViewById(R.id.eventimageLink);
            eventname = itemView.findViewById(R.id.eventname);


        }
    }


}
