package com.ags.voltassafety.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ags.voltassafety.R;
import com.ags.voltassafety.model.ItemsActionDetailsModel;
import com.ags.voltassafety.utility.BaseActivity;

import java.util.List;

public class ObservtionActionAdapter extends BaseAdapter {

    Context context;
    private GridView gv;
    List<ItemsActionDetailsModel> itemsActionDetailsModels;
    private GalleryAdapter galleryAdapter;

    public ObservtionActionAdapter(Context context, List<ItemsActionDetailsModel> observationitemsactondetailsModel) {
        this.context = context;
        itemsActionDetailsModels = observationitemsactondetailsModel;
    }

    @Override
    public int getCount() {
        return itemsActionDetailsModels.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsActionDetailsModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView action_data_textview, action_textview;
        View itemView = null;
        final ImageView addButton, item_delete, add_actions;
        try {
            itemView = LayoutInflater.from(context).inflate(R.layout.activity_observtion_action, viewGroup, false);
            action_data_textview = itemView.findViewById(R.id.action_data_textview);
            action_textview = itemView.findViewById(R.id.action_textview);
            action_textview.setText("Task " + (i + 1) + " need to take care of");
            action_data_textview.setText(itemsActionDetailsModels.get(i).getActionName());
            /*addButton = itemView.findViewById(R.id.addButton);
            //item_delete = itemView.findViewById(R.id.item_delete);
            gv = itemView.findViewById(R.id.gv);
            if (itemsActionDetailsModels.get(i).getObservationAttachmentModels() != null) {

                galleryAdapter = new GalleryAdapter(context, itemsActionDetailsModels.get(i).getObservationAttachmentModels(), "");
                gv.setAdapter(galleryAdapter);
                gv.setVerticalSpacing(gv.getHorizontalSpacing());
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv.getLayoutParams();
                mlp.setMargins(0, gv.getHorizontalSpacing(), 0, 0);
            }

            gv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gv.hasFocus()) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                    return false;
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListView) viewGroup).performItemClick(v, i, 0); // Let the event be handled in onItemClick()
                }
            });*/


        }catch (Exception e) {
            e.getMessage();
        }
        return itemView;
    }
}
