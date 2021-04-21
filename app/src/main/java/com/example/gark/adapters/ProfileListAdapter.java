package com.example.gark.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gark.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileElementHolder> {

    private final Context mContext;
    private final ArrayList<Integer> icons;
    private final ArrayList<String> titles;
    private final ArrayList<String> descriptions;

    public ProfileListAdapter(Context mContext,ArrayList<Integer> icons,ArrayList<String> titles,ArrayList<String> descriptions ){
        this.mContext = mContext;
        this.icons = icons;
        this.titles = titles;
        this.descriptions = descriptions;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ProfileElementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_single_row_profile,parent,false);
        return new ProfileListAdapter.ProfileElementHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ProfileElementHolder holder, int position) {
        String title = titles.get(position);
        Integer icon = icons.get(position);
        String description = descriptions.get(position);
        holder.titleTextView.setText(title);
        holder.iconName.setImageResource(icon);
        holder.descriptionTextView.setText(description);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ProfileElementHolder extends RecyclerView.ViewHolder {
        ImageView iconName;
        TextView titleTextView,descriptionTextView;

        public ProfileElementHolder(@NonNull View itemView) {
            super(itemView);
            iconName = itemView.findViewById(R.id.iconName);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
