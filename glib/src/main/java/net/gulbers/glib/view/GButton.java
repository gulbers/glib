package net.gulbers.glib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import net.gulbers.glib.GGlobals;
import net.gulbers.glib.R;
import net.gulbers.glib.utils.FontCache;

public class GButton extends android.support.v7.widget.AppCompatButton {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public GButton(Context context) {
        super(context);

        applyCustomFont(context, null);
    }

    public GButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public GButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.GButton);
        String fontName = attributeArray.getString(R.styleable.GButton_gAppFont);

        Typeface customFont = FontCache.getTypeface(GGlobals.APP_FONT, context);
        if (fontName != null) {
            customFont = FontCache.getTypeface("fonts/" + fontName, context);
        }

        if (attrs != null && !isInEditMode()) {
            int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface
                    .NORMAL);
            setTypeface(customFont, textStyle);
        } else {
            setTypeface(customFont);
        }

        attributeArray.recycle();
    }

}
