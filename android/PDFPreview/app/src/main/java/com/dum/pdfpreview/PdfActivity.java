package com.dum.pdfpreview;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PdfActivity extends AppCompatActivity {
    private static final String TAG = PdfActivity.class.getName();

    private PdfiumCore mPdfCore;

    private PdfDocument mPdfDoc = null;

    private GestureDetector mSlidingDetector;
    private ScaleGestureDetector mZoomingDetector;

    private int mCurrentPageIndex = 0;
    private int mPageCount = 0;

    private SurfaceHolder mPdfSurfaceHolder;
    private boolean isSurfaceCreated = false;

    private final Rect mPageRect = new Rect();
    private final RectF mPageRectF = new RectF();
    private final Rect mScreenRect = new Rect();
    private final Matrix mTransformMatrix = new Matrix();
    private boolean isScaling = false;
    private boolean isReset = true;


    private final ExecutorService mPreLoadPageWorker = Executors.newSingleThreadExecutor();
    private final ExecutorService mRenderPageWorker = Executors.newSingleThreadExecutor();
    private Runnable mRenderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        mPdfCore = new PdfiumCore(this);

        mSlidingDetector = new GestureDetector(this, new SlidingDetector());
        mZoomingDetector = new ScaleGestureDetector(this, new ZoomingDetector());

        Intent intent = getIntent();
        Uri fileUri;
        if ((fileUri = intent.getData()) == null) {
            finish();
            return;
        }

        mRenderRunnable = new Runnable() {
            @Override
            public void run() {
                loadPageIfNeed(mCurrentPageIndex);

                resetPageFit(mCurrentPageIndex);
                mPdfCore.renderPage(mPdfDoc, mPdfSurfaceHolder.getSurface(), mCurrentPageIndex,
                        mPageRect.left, mPageRect.top,
                        mPageRect.width(), mPageRect.height());

                mPreLoadPageWorker.submit(new Runnable() {
                    @Override
                    public void run() {
                        loadPageIfNeed(mCurrentPageIndex + 1);
                        loadPageIfNeed(mCurrentPageIndex + 2);
                    }
                });
            }
        };

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.view_surface_main);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isSurfaceCreated = true;
                updateSurface(holder);
                if (mPdfDoc != null) {
                    mRenderPageWorker.submit(mRenderRunnable);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.w(TAG, "Surface Changed");
                updateSurface(holder);
                if (mPdfDoc != null) {
                    mRenderPageWorker.submit(mRenderRunnable);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isSurfaceCreated = false;
                Log.w(TAG, "Surface Destroy");
            }
        });

        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(fileUri, "r");

            mPdfDoc = mPdfCore.newDocument(pfd);
            Log.d(TAG, "Open Document");

            mPageCount = mPdfCore.getPageCount(mPdfDoc);
            Log.d(TAG, "Page Count: " + mPageCount);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Data uri: " + fileUri.toString());
        }
    }

    private void loadPageIfNeed(final int pageIndex) {
        if (pageIndex >= 0 && pageIndex < mPageCount && !mPdfDoc.hasPage(pageIndex)) {
            Log.d(TAG, "Load page: " + pageIndex);
            mPdfCore.openPage(mPdfDoc, pageIndex);
        }
    }

    private void updateSurface(SurfaceHolder holder) {
        mPdfSurfaceHolder = holder;
        mScreenRect.set(holder.getSurfaceFrame());
    }

    private void resetPageFit(int pageIndex) {
        float pageWidth = mPdfCore.getPageWidth(mPdfDoc, pageIndex);
        float pageHeight = mPdfCore.getPageHeight(mPdfDoc, pageIndex);
        float screenWidth = mPdfSurfaceHolder.getSurfaceFrame().width();
        float screenHeight = mPdfSurfaceHolder.getSurfaceFrame().height();

        /**Portrait**/
        if (screenWidth < screenHeight) {
            if ((pageWidth / pageHeight) < (screenWidth / screenHeight)) {
                //Situation one: fit height
                pageWidth *= (screenHeight / pageHeight);
                pageHeight = screenHeight;

                mPageRect.top = 0;
                mPageRect.left = (int) (screenWidth - pageWidth) / 2;
                mPageRect.right = (int) (mPageRect.left + pageWidth);
                mPageRect.bottom = (int) pageHeight;
            } else {
                //Situation two: fit width
                pageHeight *= (screenWidth / pageWidth);
                pageWidth = screenWidth;

                mPageRect.left = 0;
                mPageRect.top = (int) (screenHeight - pageHeight) / 2;
                mPageRect.bottom = (int) (mPageRect.top + pageHeight);
                mPageRect.right = (int) pageWidth;
            }
        } else {

            /**Landscape**/
            if (pageWidth > pageHeight) {
                //Situation one: fit height
                pageWidth *= (screenHeight / pageHeight);
                pageHeight = screenHeight;

                mPageRect.top = 0;
                mPageRect.left = (int) (screenWidth - pageWidth) / 2;
                mPageRect.right = (int) (mPageRect.left + pageWidth);
                mPageRect.bottom = (int) pageHeight;
            } else {
                //Situation two: fit width
                pageHeight *= (screenWidth / pageWidth);
                pageWidth = screenWidth;

                mPageRect.left = 0;
                mPageRect.top = 0;
                mPageRect.bottom = (int) (mPageRect.top + pageHeight);
                mPageRect.right = (int) pageWidth;
            }
        }

        isReset = true;
    }

    private void rectF2Rect(RectF inRectF, Rect outRect) {
        outRect.left = (int) inRectF.left;
        outRect.right = (int) inRectF.right;
        outRect.top = (int) inRectF.top;
        outRect.bottom = (int) inRectF.bottom;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret;

        ret = mZoomingDetector.onTouchEvent(event);
        if (!isScaling) ret |= mSlidingDetector.onTouchEvent(event);
        ret |= super.onTouchEvent(event);

        return ret;
    }

    private class SlidingDetector extends GestureDetector.SimpleOnGestureListener {

        private boolean checkFlippable() {
            return (mPageRect.left >= mScreenRect.left &&
                    mPageRect.right <= mScreenRect.right);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isSurfaceCreated) return false;
            Log.d(TAG, "Drag");

            distanceX *= -1f;
            distanceY *= -1f;

            if ((mPageRect.left <= mScreenRect.left && mPageRect.right <= mScreenRect.right && distanceX < 0) ||
                    (mPageRect.right >= mScreenRect.right && mPageRect.left >= mScreenRect.left && distanceX > 0))
                distanceX = 0f;
            if ((mPageRect.top <= mScreenRect.top && mPageRect.bottom <= mScreenRect.bottom && distanceY < 0) ||
                    (mPageRect.bottom >= mScreenRect.bottom && mPageRect.top >= mScreenRect.top && distanceY > 0))
                distanceY = 0f;

            //Portrait restriction
            if (isReset && mScreenRect.width() < mScreenRect.height()) distanceX = distanceY = 0f;
            if (isReset && mScreenRect.height() <= mScreenRect.width()) distanceX = 0f;

            if (distanceX == 0f && distanceY == 0f) return false;

            Log.d(TAG, "DistanceX: " + distanceX);
            Log.d(TAG, "DistanceY: " + distanceY);
            mPageRect.left += distanceX;
            mPageRect.right += distanceX;
            mPageRect.top += distanceY;
            mPageRect.bottom += distanceY;

            mPdfCore.renderPage(mPdfDoc, mPdfSurfaceHolder.getSurface(), mCurrentPageIndex,
                    mPageRect.left, mPageRect.top,
                    mPageRect.width(), mPageRect.height());

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!isSurfaceCreated) return false;
            if (velocityX == 0f) return false;

            if (!checkFlippable()) {
                Log.d(TAG, "Not flippable");
                return false;
            }

            if (velocityX < -200f) { //Forward
                if (mCurrentPageIndex < mPageCount - 1) {
                    Log.d(TAG, "Flip forward");
                    mCurrentPageIndex++;
                    Log.d(TAG, "Next Index: " + mCurrentPageIndex);

                    mRenderPageWorker.submit(mRenderRunnable);
                }
                return true;
            }

            if (velocityX > 200f) { //Backward
                Log.d(TAG, "Flip backward");
                if (mCurrentPageIndex > 0) {
                    mCurrentPageIndex--;
                    Log.d(TAG, "Next Index: " + mCurrentPageIndex);

                    mRenderPageWorker.submit(mRenderRunnable);
                }
                return true;
            }

            return false;
        }
    }

    private class ZoomingDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mAccumulateScale = 1f;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isScaling = true;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (!isSurfaceCreated) return false;


            mAccumulateScale *= detector.getScaleFactor();
            mAccumulateScale = Math.max(1f, mAccumulateScale);
            float scaleValue = (mAccumulateScale > 1f) ? detector.getScaleFactor() : 1f;
            mTransformMatrix.setScale(scaleValue, scaleValue,
                    detector.getFocusX(), detector.getFocusY());
            mPageRectF.set(mPageRect);

            mTransformMatrix.mapRect(mPageRectF);

            rectF2Rect(mPageRectF, mPageRect);

            mPdfCore.renderPage(mPdfDoc, mPdfSurfaceHolder.getSurface(), mCurrentPageIndex,
                    mPageRect.left, mPageRect.top,
                    mPageRect.width(), mPageRect.height());

            isReset = false;

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (mAccumulateScale == 1f && !mScreenRect.contains(mPageRect)) {
                resetPageFit(mCurrentPageIndex);

                mPdfCore.renderPage(mPdfDoc, mPdfSurfaceHolder.getSurface(), mCurrentPageIndex,
                        mPageRect.left, mPageRect.top,
                        mPageRect.width(), mPageRect.height());
            }

            isScaling = false;
        }
    }

    @Override
    public void onDestroy() {
        if (mPdfDoc != null) {
            mPdfCore.closeDocument(mPdfDoc);
            Log.d(TAG, "Close Document");
        }
        super.onDestroy();
    }
}