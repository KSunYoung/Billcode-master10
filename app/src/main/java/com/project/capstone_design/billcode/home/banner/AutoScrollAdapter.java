package com.project.capstone_design.billcode.home.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.project.capstone_design.billcode.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AutoScrollAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> data;

    public AutoScrollAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.auto_viewpager,null);
        ImageView image_container = (ImageView) mView.findViewById(R.id.image_container);
        Picasso.get().load(data.get(position)).into(image_container);
        container.addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
