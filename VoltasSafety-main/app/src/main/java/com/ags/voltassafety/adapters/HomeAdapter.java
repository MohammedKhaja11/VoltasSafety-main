package com.ags.voltassafety.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.voltassafety.GoogleMapActivity;
import com.ags.voltassafety.ObservationViewActivity;
import com.ags.voltassafety.R;
import com.ags.voltassafety.ReportsActivity;
import com.ags.voltassafety.SupportActivity;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyView> {
    Context context;
    ArrayList<String> name;
    ArrayList<Integer> image;
    ArrayList<Integer> color;

    public HomeAdapter(Context context, ArrayList<String> name, ArrayList<Integer> image, ArrayList<Integer> color) {

        this.context = context;
        this.name = name;
        this.image = image;
        this.color = color;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyView holder, final int position) {
        try {
            holder.textViewOne.setText(name.get(position));
            holder.imageOne.setImageResource(image.get(position));
            holder.view.setBackgroundResource(color.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionAdapter = holder.getAdapterPosition();
                    if (positionAdapter == 0) {
                        Intent intent = new Intent(context, ObservationViewActivity.class);
                        context.startActivity(intent);

                    } else if (positionAdapter == 1) {
                        Intent intent = new Intent(context, ReportsActivity.class);
                        context.startActivity(intent);

                    } else if (positionAdapter == 2) {
                        Intent intent = new Intent(context, GoogleMapActivity.class);
                        Bundle bd = new Bundle();
                        bd.putString("FLAG", "");
                        intent.putExtras(bd);
                        context.startActivity(intent);

                    } else if (positionAdapter == 3) {
                        Intent intent = new Intent(context, SupportActivity.class);
                        context.startActivity(intent);

                    }

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        public TextView textViewOne;
        public ImageView image, imageOne;
        public RelativeLayout view;


        public MyView(@NonNull View itemView) {
            super(itemView);
            try {
                textViewOne = (TextView) itemView.findViewById(R.id.text);
                imageOne = (ImageView) itemView.findViewById(R.id.imageOne);
                view = (RelativeLayout) itemView.findViewById(R.id.cardview);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
}
