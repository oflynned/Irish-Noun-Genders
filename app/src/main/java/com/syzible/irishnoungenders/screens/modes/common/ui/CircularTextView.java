package com.syzible.irishnoungenders.screens.modes.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.syzible.irishnoungenders.R;

public class CircularTextView extends AppCompatTextView {
    private float strokeWidth;
    int strokeColor, solidColor;

    public CircularTextView(Context context) {
        super(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        setStrokeWidth(2);
        setSolidColor(R.color.action);
        setStrokeColor(R.color.white);

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int h = this.getHeight();
        int w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter / 2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle((float) diameter / 2, (float) diameter / 2, radius, strokePaint);
        canvas.drawCircle((float) diameter / 2, (float) diameter / 2, radius - strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp * scale;
    }

    public void setStrokeColor(int colorResource) {
        strokeColor = ContextCompat.getColor(getContext(), colorResource);
    }

    public void setSolidColor(int colorResource) {
        solidColor = ContextCompat.getColor(getContext(), colorResource);
    }
}
