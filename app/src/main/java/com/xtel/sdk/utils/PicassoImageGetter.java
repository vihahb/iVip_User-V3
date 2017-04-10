package com.xtel.sdk.utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.xtel.sdk.utils.component.LoadFromUriAsyncTask;
import com.xtel.sdk.utils.component.URLDrawable;

/**
 * Created by vivhp on 4/3/2017.
 */

public class PicassoImageGetter implements Html.ImageGetter {

    private final TextView mTextView;

    /**
     * Construct an instance of {@link android.text.Html.ImageGetter}
     *
     * @param view {@link android.widget.TextView} that holds HTML which contains $lt;img&gt; tag to load
     */
    public PicassoImageGetter(TextView view) {
        mTextView = view;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        final Uri uri = Uri.parse(source);
        if (uri.isRelative()) {
            return null;
        }
        final URLDrawable urlDrawable = new URLDrawable(mTextView.getResources(), null);
        new LoadFromUriAsyncTask(mTextView, urlDrawable).execute(uri);
        return urlDrawable;
    }
}
