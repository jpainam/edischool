package com.edischool.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class TextHelper {

    public static Spanned fromHtml(String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){ // Nougot
            return Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(str);
        }

    }
}
