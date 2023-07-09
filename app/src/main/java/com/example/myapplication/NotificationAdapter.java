package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Notification;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private List<Notification> notifications;
    private FragmentManager fragmentManager;
    public NotificationAdapter(Context context, List<Notification> notifications,FragmentManager fragmentManager) {
        super(context, 0, notifications);
        this.context = context;
        this.notifications = notifications;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the notification at the specified position
        Notification notification = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        }

        TextView notificationTitle = convertView.findViewById(R.id.notificationTitle);
        Button button1 = convertView.findViewById(R.id.button1);
        Button button2 = convertView.findViewById(R.id.button2);

        notificationTitle.setText(notification.message);

        if (notification.type.equals("Disturbance")) {
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.GONE);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete the notification from the list
                    Fragment fragment = new GalleryFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete the notification from the list
                    notifications.remove(notification);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

}
