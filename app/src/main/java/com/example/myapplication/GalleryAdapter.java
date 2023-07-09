package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<Image> {
    GalleryAdapter(@NonNull Context context, ArrayList<Image> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.gallery_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Image currentItem = getItem(position);
        Log.d("Image File Name", currentItem.img);
        ImageView imageView = listItem.findViewById(R.id.IvPicture);
        Picasso.get()
                .load("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/uploads/"+currentItem.img)
                .into(imageView);
//        picture.setBackgroundResource(currentItem.getPicture());
//
        TextView title = listItem.findViewById(R.id.tvTitle);
        title.setText(currentItem.dateTime);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open full screen activity with the clicked image
                Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
                intent.putExtra("imageUrl", currentItem.img);
                getContext().startActivity(intent);
            }
        });

        return listItem;
    }

}
