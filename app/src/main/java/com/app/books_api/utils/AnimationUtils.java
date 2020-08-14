package com.app.books_api.utils;

import androidx.navigation.NavOptions;

import com.app.books_api.R;

public class AnimationUtils {
    public static NavOptions fromRightToLeft() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.slide_out)
                .setPopEnterAnim(R.anim.slide_in_previous)
                .setPopExitAnim(R.anim.slide_out_previous)
                .build();
    }

    public static NavOptions fromLeftToRight() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_previous)
                .setExitAnim(R.anim.slide_out_previous)
                .setPopEnterAnim(R.anim.slide_in)
                .setPopExitAnim(R.anim.slide_out)
                .build();
    }

    public static NavOptions fade() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .build();
    }

    public static NavOptions fromTopToBottom() {
        return new NavOptions.Builder()
                .setPopEnterAnim(R.anim.slide_in_bottom)
                .setPopExitAnim(R.anim.slide_out_bottom)
                .setEnterAnim(R.anim.slide_in_top)
                .setExitAnim(R.anim.slide_out_top)
                .build();
    }
}
