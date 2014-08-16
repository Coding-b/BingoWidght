package com.bingo.widgt;

import com.bingo.bingowidgt.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView{
	/*
	 * border width
	 */
	private static final int DEFAULT_BORDER_THICKNESS = 0;
	/*
	 * border color
	 */
	private static final int DEFAULT_COLOR = Color.BLACK;

	/*
	 * bitmap config
	 */
	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	/*
	 * colorDrawable dimension
	 */
    private static final int COLORDRAWABLE_DIMENSION = 1;
	/*
	 * context
	 */
	private Context mContext;
	
	private int bCircleImageWidth = 0;
	private int bCircleImageHeight = 0;
	
	//attributes
	private int mBorderThinkness;
	private int mBorderOutsideColor;
	private int mBorderInsideColor;

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init(attrs);
	}
	
	public CircleImageView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public CircleImageView(Context context) {
		this(context,null,0);
	}
	
	/**
	 * get all attributes
	 * @param attrs AttributsSet
	 */
	private void init(AttributeSet attrs) {
		if(attrs == null){
			return;
		}
		
		TypedArray style = mContext.obtainStyledAttributes(attrs, R.styleable.bingoCircleImage);
		
		mBorderInsideColor = style.getColor(R.styleable.bingoCircleImage_border_InsideColor, DEFAULT_COLOR);
		mBorderOutsideColor = style.getColor(R.styleable.bingoCircleImage_border_OutsideColor, DEFAULT_COLOR);
		mBorderThinkness = style.getDimensionPixelSize(R.styleable.bingoCircleImage_border_width, DEFAULT_BORDER_THICKNESS);
		
		style.recycle();
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if(drawable == null || getHeight() == 0 || getWidth() == 0){
			return;
		}
		
		//Get the picture width and height
		if(bCircleImageHeight == 0)
			bCircleImageHeight = getHeight();
		if(bCircleImageWidth == 0)
			bCircleImageWidth = getWidth();
		
		int radius = 0;
		
		if (mBorderInsideColor != DEFAULT_COLOR  
                && mBorderOutsideColor != DEFAULT_COLOR) {  
            radius = (bCircleImageWidth < bCircleImageHeight ? bCircleImageWidth  
                    : bCircleImageHeight) / 2 - 2 * mBorderThinkness;  
            
            drawCircleBorder(canvas, radius + bCircleImageWidth / 2,  
                    mBorderInsideColor);  
            
            drawCircleBorder(canvas, radius + mBorderThinkness  
                    + bCircleImageWidth / 2, mBorderOutsideColor);  
        } else if (mBorderInsideColor != DEFAULT_COLOR  
                && mBorderOutsideColor == DEFAULT_COLOR) {
            radius = (bCircleImageWidth < bCircleImageHeight ? bCircleImageWidth  
                    : bCircleImageHeight) / 2 - mBorderThinkness;  
            drawCircleBorder(canvas, radius + bCircleImageWidth / 2,  
                    mBorderInsideColor);  
        } else if (mBorderInsideColor == DEFAULT_COLOR  
                && mBorderOutsideColor != DEFAULT_COLOR) {
            radius = (bCircleImageWidth < bCircleImageHeight ? bCircleImageWidth  
                    : bCircleImageHeight) / 2 - mBorderThinkness;  
            drawCircleBorder(canvas, radius + bCircleImageWidth / 2,  
                    mBorderOutsideColor);  
        } else {
            radius = (bCircleImageWidth < bCircleImageHeight ? bCircleImageWidth  
                    : bCircleImageHeight) / 2;  
        }  
          
        //画圆形图像  
        Bitmap roundBitmap = getCroppedBitmap(getBitmapFromDrawable(drawable), radius);  
        canvas.drawBitmap(roundBitmap, bCircleImageWidth/2 - radius,  
        		bCircleImageHeight/2 - radius, null);  
	}
	
	private void drawCircleBorder(Canvas canvas, int radius, int color) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setAntiAlias(true);  
        paint.setColor(color);  
        paint.setDither(true);  
        paint.setFilterBitmap(true);  
        paint.setStyle(Paint.Style.STROKE);  
        paint.setStrokeWidth(mBorderThinkness);  
        canvas.drawCircle(bCircleImageWidth/2, bCircleImageHeight/2, radius, paint);  
	}

	/**
	 * Get cropped bitmap
	 * @param bitmap Bitmap to crop
	 * @param radius 
	 * @return
	 */
	private Bitmap getCroppedBitmap(Bitmap bitmap,float radius) {
		Bitmap scaledSrcBmp;  
        int diameter = (int)radius * 2;  
  
        int bmpWidth = bitmap.getWidth();  
        int bmpHeight = bitmap.getHeight();  
        int squareWidth = 0, squareHeight = 0;  
        int x = 0, y = 0;  
        Bitmap squareBitmap;  
        if (bmpHeight > bmpWidth) { 
            squareWidth = squareHeight = bmpWidth;  
            x = 0;  
            y = (bmpHeight - bmpWidth) / 2;  
            // 截取正方形图片  
            squareBitmap = Bitmap.createBitmap(bitmap, x, y, squareWidth,  
                    squareHeight);  
        } else if (bmpHeight < bmpWidth) {
            squareWidth = squareHeight = bmpHeight;  
            x = (bmpWidth - bmpHeight) / 2;  
            y = 0;  
            squareBitmap = Bitmap.createBitmap(bitmap, x, y, squareWidth,  
                    squareHeight);  
        } else {  
            squareBitmap = bitmap;  
        }  
  
        if (squareBitmap.getWidth() != diameter  
                || squareBitmap.getHeight() != diameter) {  
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,  
                    diameter, true);  
  
        } else {  
            scaledSrcBmp = squareBitmap;  
        }  
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),  
                scaledSrcBmp.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
  
        Paint paint = new Paint();  
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),  
                scaledSrcBmp.getHeight());  
  
        paint.setAntiAlias(true);  
        paint.setFilterBitmap(true);  
        paint.setDither(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,  
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,  
                paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        bitmap = null;  
        squareBitmap = null;  
        scaledSrcBmp = null;  
        return output; 
	}

	/**
	 * Set all draw parameters 
	 */
	private void setUpParameter() {

	}

	/**
	 * convert drawable to bitmap
	 * @param drawable DrawableResource
	 * @return bitmap
	 */
	private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
 
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
 
        try {
            Bitmap bitmap;
 
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
 
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}
