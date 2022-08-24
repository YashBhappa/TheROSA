package com.finalyear.microproject.adapter;

import com.finalyear.microproject.R;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide("https://image.freepik.com/free-psd/restaurant-food-menu-instagram-stories-post-template_120329-187.jpg");
                break;
            case 1:
                viewHolder.bindImageSlide("https://image.freepik.com/free-psd/food-post-template_92941-116.jpg");
                break;
            case 2:
                viewHolder.bindImageSlide("https://image.freepik.com/free-vector/food-social-media-instagram-post-template_92819-73.jpg");
                break;
            case 3:
                viewHolder.bindImageSlide("https://i.pinimg.com/originals/7b/af/83/7baf8351e585975a92b7b2ccce6b193d.jpg");
                break;
        }
    }
}