package com.versusmind.barwave.barwave.item;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        final int itemCount = state.getItemCount();

        if(position == 0) {
            outRect.top = space;
        } else if (position == itemCount -1) {
            outRect.bottom = space;
        }
    }

    public void setSpace(int space) {
        this.space = space;
    }
}