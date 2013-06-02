/**
 * @(#)AutoResizeTextView.java, Jun 2, 2013. 
 * 
 */
package me.cocodrum.autoresize;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * @author xuhongfeng
 *
 */
public class AutoResizeTextView extends TextView {
    private static final int MAX_SIZE = 1000;

    private static final int MIN_SIZE = 5;

    private TextPaint mTextPaint;

    private float mSpacingMult = 1.0f;

    private float mSpacingAdd = 0.0f;
    
    private boolean needAdapt = false;
    
    private boolean adapting = false;

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoResizeTextView(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        mTextPaint = new TextPaint();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (adapting) {
            return;
        }
        if (needAdapt) {
            adaptTextSize();
        } else {
            super.onDraw(canvas);
        }
    }
    
    private void adaptTextSize() {
        CharSequence text = getText();
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        
        if (viewWidth==0 || viewHeight==0
                || TextUtils.isEmpty(text)) {
            return;
        }
        
        adapting = true;
        /* binary search */
        int bottom=MIN_SIZE, top=MAX_SIZE, mid = 0;
        while (bottom <= top) {
            mid = (bottom + top)/2;
            mTextPaint.setTextSize(mid);
            int textWidth = (int) mTextPaint.measureText(text, 0, text.length());
            int textHeight = getTextHeight(text, viewWidth);
            if (textWidth<viewWidth && textHeight<viewHeight) {
                bottom = mid+1;
            } else {
                top = mid-1;
            }
        }
        
        int newSize = mid-1;
        setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
        
        adapting=false;
        needAdapt = false;
        
        invalidate();
    }

    private int getTextHeight(CharSequence text, int targetWidth) {
        StaticLayout layout = new StaticLayout(text, mTextPaint, targetWidth,
                Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);
        return layout.getHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        needAdapt = true;
    }
    
    @Override
    protected void onTextChanged(CharSequence text, int start,
            int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        needAdapt = true;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mSpacingMult = mult;
        mSpacingAdd = add;
    }
}
