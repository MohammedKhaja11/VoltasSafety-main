package com.ags.voltassafety.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.ags.voltassafety.R;
import com.ags.voltassafety.utility.Utilities;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {
    private Context context;
    String[] def;
    private ArrayList<Integer> images;
    private ArrayList<String> defnationArray;
    ArrayList<String> nameArray;
    private LayoutInflater inflater;

    public SlidingImageAdapter(Context context, ArrayList<Integer> images, ArrayList<String> defnationArray, ArrayList<String> nameArray) {
        this.context = context;
        this.images = images;
        this.defnationArray = defnationArray;
        this.nameArray = nameArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size() - 1;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        View imageLayout = inflater.inflate(R.layout.accident_defnation_layout, (ViewGroup) container, false);
        assert imageLayout != null;

        final ImageView image = (ImageView) imageLayout.findViewById(R.id.image);
        final TextView answerText = (TextView) imageLayout.findViewById(R.id.answerText);
        final TextView titleText = (TextView) imageLayout.findViewById(R.id.titleText);
        answerText.setTypeface(Utilities.fontBold(context));
        image.setImageResource(images.get(position));
        answerText.setText(defnationArray.get(position));
        titleText.setText(nameArray.get(position));
//        imageView.addView(images.get(position));
        container.addView(imageLayout, 0);
//        String value = AppPreferences.getInstance(context).getString("kay");
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return super.saveState();
    }
}
