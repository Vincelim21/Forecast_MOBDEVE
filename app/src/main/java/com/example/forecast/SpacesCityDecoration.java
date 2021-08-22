package com.example.forecast;

import android.graphics.Rect;
import android.view.View;
import android.widget.Space;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesCityDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesCityDecoration(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //outRect.left = space;
        //outRect.right = space * 3;
        //outRect.bottom = space;

        //Every odd count meaning all cards in the right side of the two column grid recyclerview
        if (parent.getChildLayoutPosition(view)%2 != 0) {
            outRect.top = space * 5;
            outRect.left = space * 2;
        }
        else {
            outRect.top = 0;
        }

    }

    //HI
}
