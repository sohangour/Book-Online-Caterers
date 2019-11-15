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
import com.example.sohancaterers.MainActivityFragment.home.ItemActivity;
import com.example.sohancaterers.ModelClass.NoteMenu;
import com.example.sohancaterers.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class NoteMenuAdapter extends FirestoreRecyclerAdapter<NoteMenu, NoteMenuAdapter.NoteHolder> {


    public NoteMenuAdapter(@NonNull FirestoreRecyclerOptions<NoteMenu> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NoteHolder noteHolder, int i, @NonNull final NoteMenu noteMenu) {


        noteHolder.menu.setText(noteMenu.getMenu());
        Glide.with(noteHolder.itemView.getContext()).load(noteMenu.getImageLink()).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50))).into(noteHolder.imageLink);

        //show image on another by touch

        noteHolder.menu_card_view.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;

            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 300) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                intent.putExtra("Menu", noteMenu.getMenu());
                view.getContext().startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_category, parent, false);
        return new NoteHolder(v);
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        public ImageView imageLink;
        public TextView menu;
        public CardView menu_card_view;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            menu_card_view = itemView.findViewById(R.id.menu_card_view);
            imageLink = itemView.findViewById(R.id.imageLink);
            menu = itemView.findViewById(R.id.menu);


        }
    }
}
