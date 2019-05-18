package com.edischool.utils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.edischool.MainActivity;
import com.edischool.R;
import android.content.Context;

import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatDelegate;

import org.jetbrains.annotations.NotNull;

import butterknife.internal.Utils;

public class ActivityUtils {

    public static void showAboutDialog(Context context){
            MaterialDialog build = new MaterialDialog.Builder(context).customView(R.layout.about_dialog_layout, false)
                    .title(context.getString(R.string.activity_about_title))
                    .positiveText(context.getString(R.string.dialog_ok_answer))
                    .canceledOnTouchOutside(true)
                  .build();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                  build.setIcon(context.getDrawable(R.drawable.ic_info_black_24dp));
            }else{
                  build.setIcon(context.getResources().getDrawable(R.drawable.ic_info_black_24dp));
            }
            View customView = build.getCustomView();
            ((TextView) customView.findViewById(R.id.about_app_name)).setText(TextHelper.fromHtml(
                    context.getString(R.string.app_name)));

            ((TextView) customView.findViewById(R.id.about_app_version)).
                    setText(context.getString(R.string.about_app_version));

            TextView textView = customView.findViewById(R.id.about_author_name);
            textView.setText(TextHelper.fromHtml(context.getString(R.string.about_author_name)));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView = customView.findViewById(R.id.about_contact_name);
            textView.setText(TextHelper.fromHtml(context.getString(R.string.about_contact_name, new Object[]
                    {context.getString(R.string.edischool_app_email)})));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            TextView tvAcknowledgment = customView.findViewById(R.id.about_acknowledgement_name);
            tvAcknowledgment.setText(TextHelper.fromHtml(context.getString(R.string.about_acknowledgement_name)));

            TextView tvCopyright = customView.findViewById(R.id.about_copyright);
            tvCopyright.setText(TextHelper.fromHtml(context.getString(R.string.about_copyright, DateUtils.getCurrentYear()+"")));
            build.show();
    }
        public static boolean isNightMode() {
            return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        }

        public static boolean isDayMode() {
            return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        }
}
