package com.example.kalarilab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;

public class TiledImageView extends View {
    private Drawable mImage;
    private int mImageWidth;
    private int mImageHeight;
    private int mNumCols;
    private int mNumRows;
    private int mTileWidth;
    private int mTileHeight;
    private Rect mVisibleRect = new Rect();
    private Paint mMarkerPaint = new Paint();
    private ArrayList<PointF> mMarkers = new ArrayList<>();

    public TiledImageView(Context context) {
        super(context);
        init();
    }

    public TiledImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TiledImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mMarkerPaint.setColor(Color.RED);
        mMarkerPaint.setStyle(Paint.Style.FILL);
        mMarkerPaint.setStrokeWidth(5f);
        mMarkerPaint.setAntiAlias(true);
    }

    public void setImage(Drawable image) {
        mImage = image;
        mImageWidth = image.getIntrinsicWidth();
        mImageHeight = image.getIntrinsicHeight();
        calculateTiles();
        invalidate();
    }

    private void calculateTiles() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        float scale = Math.min((float) screenWidth / mImageWidth, (float) screenHeight / mImageHeight);
        mTileWidth = (int) (screenWidth / scale);
        mTileHeight = (int) (screenHeight / scale);
        mNumCols = (int) Math.ceil((float) mImageWidth / mTileWidth);
        mNumRows = (int) Math.ceil((float) mImageHeight / mTileHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mImage == null) {
            return;
        }

        mVisibleRect.set(getLeft(), getTop(), getRight(), getBottom());

        for (int row = 0; row < mNumRows; row++) {
            for (int col = 0; col < mNumCols; col++) {
                int x = col * mTileWidth;
                int y = row * mTileHeight;
                Rect tileRect = new Rect(x, y, x + mTileWidth, y + mTileHeight);
                if (Rect.intersects(tileRect, mVisibleRect)) {
                    int saveCount = canvas.save();
                    canvas.translate(-getScrollX(), -getScrollY());
                    canvas.clipRect(tileRect);
                    mImage.setBounds(tileRect);
                    mImage.draw(canvas);
                    canvas.restoreToCount(saveCount);
                }
            }
        }

        for (PointF marker : mMarkers) {
            float x = marker.x * mImageWidth;
            float y = marker.y * mImageHeight;
            canvas.drawCircle(x, y, 10f, mMarkerPaint);
        }
    }

    public void addMarker(float x, float y) {
        mMarkers.add(new PointF(x, y));
        invalidate();
    }
}
