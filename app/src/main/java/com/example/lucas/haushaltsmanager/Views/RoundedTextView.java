package com.example.lucas.haushaltsmanager.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.lucas.haushaltsmanager.Utils.ViewUtils;

public class RoundedTextView extends ViewUtils {
    private final static String TAG = RoundedTextView.class.getSimpleName();

    private Paint mCirclePaint;
    private int mDesiredSize = ViewUtils.dpToPx(40);

    private Rect mTextBounds;
    private Paint mTextPaint;
    private String mCenterText = "";
    private float mTextSize = 62f;//todo schriftgröße des angezeigten Buchstabens soll abhängig von der viewgröße sein

    // TODO: Hier sollte ich die Logik implementieren welche die Farbe der Schrift ändert basierend auf der BackgroundColor
    public RoundedTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoundedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoundedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {

        //todo den durchmesser auch in der xml datei setzen
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.LTGRAY);
        mCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mViewBounds = new RectF();

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
    }

    private Rect getDesiredSize() {
        return new Rect(0, 0, mDesiredSize, mDesiredSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //TODO consider padding
        //TODO wenn die width oder height größer ist als die andere soll der sich der kreis automatisch in der mitte platzieren
        setMaxViewBounds(widthMeasureSpec, heightMeasureSpec);

        mTextBounds = getTextBounds(mCenterText, mTextSize);
        setMeasuredDimension((int) mViewBounds.width(), (int) mViewBounds.height());
    }

    /**
     * Methode um die Maximale Größe der View zu ermitteln
     *
     * @param widthMeasureSpec  WidthMeasureSpec
     * @param heightMeasureSpec HeightMeasureSpec
     */
    private void setMaxViewBounds(int widthMeasureSpec, int heightMeasureSpec) {
        mViewBounds.set(0, 0, 0, 0);

        mViewBounds.right = reconcileSize(getDesiredSize().width(), widthMeasureSpec);
        mViewBounds.bottom = reconcileSize(getDesiredSize().height(), heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(mViewBounds.centerX(), mViewBounds.centerY(), mViewBounds.width() / 2, mCirclePaint);
        canvas.drawText(mCenterText, mViewBounds.centerX() - (mTextBounds.width() / 2), mViewBounds.centerY() + (mTextBounds.height() / 2), mTextPaint);
    }

    public void setCenterText(@NonNull String centerText) {
        mCenterText = centerText.toUpperCase();
    }

    public void setTextColor(@ColorInt int textColor) {
        mTextPaint.setColor(textColor);
    }

    @NonNull
    public String getCenterText() {
        return mCenterText;
    }

    public void setCircleColor(@NonNull String circleColor) {
        mCirclePaint.setColor(Color.parseColor(circleColor));

        this.invalidate();
    }

    public void setCircleColor(@ColorInt int color) {
        mCirclePaint.setColor(color);

        this.invalidate();
    }

    /**
     * Methode um den Durchmesser des Kreises zu verändern.
     *
     * @param diameterInPixels Neuer Durchmesser des Kreises
     */
    public void setCircleDiameter(int diameterInPixels) {
        mDesiredSize = ViewUtils.dpToPx(diameterInPixels);
        invalidate();
    }
}
