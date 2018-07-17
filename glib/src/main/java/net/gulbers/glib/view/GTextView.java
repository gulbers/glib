package net.gulbers.glib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import net.gulbers.glib.GGlobals;
import net.gulbers.glib.R;
import net.gulbers.glib.utils.FontCache;

public class GTextView extends android.support.v7.widget.AppCompatTextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public GTextView(Context context) {
        super(context);

        applyCustomFont(context, null);
    }

    public GTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public GTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        String fontName = null;
        TypedArray attributeArray = null;
        if (attrs != null) {
            attributeArray = context.obtainStyledAttributes(attrs, R.styleable.GTextView);
            fontName = attributeArray.getString(R.styleable.GTextView_gAppFont);
        }

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

        if (attributeArray != null) {
            attributeArray.recycle();
        }
    }

}
