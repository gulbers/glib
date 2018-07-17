package net.gulbers.glib.view;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import net.gulbers.glib.R;

public class GLoadingDialog {

    private Dialog dialog;

    /**
     * Loading dialog with custom res image loading
     *
     * @param context
     * @param res     Resource image loading, 0 as using lib default
     */
    public GLoadingDialog(Context context, int res) {
        dialog = new Dialog(context, R.style.GDialogTheme);
        dialog.setContentView(R.layout.g_dialog_loading);

        ImageView imgDialog = dialog.findViewById(R.id.imgDialog);
        if (res != 0) {
            imgDialog.setImageResource(res);
        }
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(900);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        imgDialog.startAnimation(rotateAnimation);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
