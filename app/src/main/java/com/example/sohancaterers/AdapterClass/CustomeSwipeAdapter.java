package com.example.sohancaterers.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.sohancaterers.R;
import com.example.sohancaterers.SliderImageShow;

public class CustomeSwipeAdapter extends PagerAdapter {
    private int[] img_resource={R.drawable.slider1,R.drawable.slider2,R.drawable.slider3,R.drawable.slider4};
    private Context context;
    private LayoutInflater layoutInflater;


    public CustomeSwipeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return img_resource.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.layout_swipe_image,container,false);
        final ImageView imageView=view.findViewById(R.id.slide_image_view);
        imageView.setImageResource(img_resource[position]);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SliderImageShow.class);
                Bundle bundle=new Bundle();
                bundle.putInt("image",img_resource[position]);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);

    }
}
