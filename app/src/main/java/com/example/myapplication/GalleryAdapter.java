package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<Image> {
    Context context;
    ArrayList<Image> items;
    GalleryAdapter(@NonNull Context context, ArrayList<Image> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
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
        Button deleteBtn = listItem.findViewById(R.id.deleteBtn);
//        deleteBtn.setBackgroundColor(Color.RED);
//        deleteBtn.setTextColor(Color.WHITE);
        Picasso.get()
                .load("http://api.imbento.com/others/ctu2023_motorcycle_anti_theft/uploads/"+currentItem.img)
                .into(imageView);
//        picture.setBackgroundResource(currentItem.getPicture());
//
        TextView title = listItem.findViewById(R.id.tvTitle);
        TextView subtitle = listItem.findViewById(R.id.tvSubtitle);
        title.setText(currentItem.getDate());
        subtitle.setText(currentItem.getTime());
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open full screen activity with the clicked image
                Intent intent = new Intent(getContext(), FullScreenImageActivity.class);
                intent.putExtra("imageUrl", currentItem.img);
                getContext().startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open full screen activity with the clicked image
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do you want to delete this image? ");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call a method to override the values from the database
                        currentItem.deleteImageFromDb(1);
                        items.remove(currentItem);
                        notifyDataSetChanged();
//                        Log.d("Current Item",which+"");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call a method to override the values from the database
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return listItem;
    }

}
