package com.versusmind.barwave.barwave;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.versusmind.barwave.barwave.item.SpacesItemDecoration;


public class BarWave extends View {

    private ValueAnimator va; //Animate position
    private ValueAnimator va2; //Animate size

    protected RectF rectOval = new RectF(0, 0, 0, 0);
    protected RectF actualRectOval = new RectF(0, 0, 0, 0);

    private int mTotalScrolled = 0;
    private RecyclerView recyclerView;

    private Paint paint = new Paint();
    private int primaryColor = Color.argb(255, 7, 19, 34);
    private int secondaryColor = Color.argb(255, 0, 24, 93);

    private IBarWave listener;

    public BarWave(Context context) {
        super(context);
        init();
    }

    public BarWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BarWave(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculNewPositionScroll(false);
        modifyRect(actualRectOval, (int)rectOval.left, (int)rectOval.top, (int)rectOval.right, (int)rectOval.bottom);

        paint.setShader(new LinearGradient(0, (float) getMeasuredHeight() /2,
                (float) getMeasuredWidth()/2,  getMeasuredHeight(),
                primaryColor,
                secondaryColor,
                Shader.TileMode.CLAMP));

        if (recyclerView != null) {
            recyclerView.addItemDecoration(new SpacesItemDecoration(getHeight()));
            animatorNavigation(true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(actualRectOval,0F, 180F, true, paint);
    }

    private void calculNewPositionScroll(Boolean scrollEnd) {
        int scrollY = mTotalScrolled;

        int value = getHeight() - scrollY;
        int top = scrollY;

        if (!scrollEnd) {
            top = (scrollY - getHeight()) ;
            value = (getHeight() - scrollY) ;
        } else {
            if (value != getHeight()/2 && value != 0) {
                if (value >= getHeight() / 2) {
                    value = getHeight();
                    top = - getHeight();
                    recyclerView.smoothScrollToPosition(0);
                } else {
                    if (mTotalScrolled <= getHeight()) {
                        recyclerView.scrollBy(0, getHeight() - mTotalScrolled);
                    }
                    value = 0;
                    top = 0;
                }
            }
        }

        final int margeWidth = 100;
        modifyRect(rectOval,  -margeWidth, top, getWidth() + margeWidth, value);
    }

    private void modifyRect(RectF rect, int left, int top, int right, int bottom) {
        rect.left = left;
        rect.top = top;
        rect.right = right;
        rect.bottom = bottom;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        animatorNavigation(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) { // 0 = end scroll
                    animatorNavigation(true);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalScrolled += dy;
                animatorNavigation(false);
            }
        });
    }

    private void animatorNavigation(boolean scrollEnd) {
        calculNewPositionScroll(scrollEnd);

        if (va != null) {
            va.cancel();
            va.end();
        }

        if (va2 != null) {
            va2.cancel();
            va2.end();
        }

        va = ValueAnimator.ofFloat(actualRectOval.top, rectOval.top);
        long duration = (long) (Math.abs(actualRectOval.top - rectOval.top) * 1.5);

        va.setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                actualRectOval.top = (float)animation.getAnimatedValue();
                BarWave.this.invalidate();
            }
        });
        va.start();

        va2 = ValueAnimator.ofFloat(actualRectOval.bottom, rectOval.bottom);
        va2.setDuration(duration);
        va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                actualRectOval.bottom = (float)animation.getAnimatedValue();
                BarWave.this.invalidate();
                updatePercent();
            }
        });
        va2.start();
    }

    private void updatePercent() {
        float bottom = actualRectOval.bottom;
        float distance = Math.abs(bottom - getHeight());
        int percent = (int) (distance / (getHeight()) * 100);
        percent = Math.min(percent, 100);

        if (listener != null) {
            listener.percentScrollChange(percent);
        }
    }

    public void setListener(IBarWave listener) {
        this.listener = listener;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }
}