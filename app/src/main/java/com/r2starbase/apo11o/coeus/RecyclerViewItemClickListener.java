package com.r2starbase.apo11o.coeus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Originally created by apo11o on 2/4/16.
 */
public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener pListener;
    private GestureDetector pGestureDetector;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public RecyclerViewItemClickListener(Context ctx, OnItemClickListener pListener) {
        this.pListener = pListener;
        this.pGestureDetector = new GestureDetector(ctx, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View v = rv.findChildViewUnder(e.getX(), e.getY());
        if (v != null && pListener != null && pGestureDetector.onTouchEvent(e)) {
            pListener.onItemClick(v, rv.getChildAdapterPosition(v));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
