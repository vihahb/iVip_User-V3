package com.xtel.ivipu.view.activity;

import android.app.Dialog;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtel.ivipu.R;
import com.xtel.ivipu.view.widget.WidgetHelper;

/**
 * Created by vivhp on 1/24/2017
 */

public abstract class IActivity extends BasicActivity {
    protected void showQrCode(String url) {
        final Dialog bottomSheetDialog = new Dialog(IActivity.this, R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(R.layout.qr_code_bottom_sheet);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);

        Button txt_close = (Button) bottomSheetDialog.findViewById(R.id.dialog_txt_close);
        ImageView img_qr_code = (ImageView) bottomSheetDialog.findViewById(R.id.dialog_qr_code);

        WidgetHelper.getInstance().setImageURL(img_qr_code, url);

        if (txt_close != null)
            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog.show();
            }
        }, 200);
    }

    protected Toolbar findToolbar(int id) {
        return (Toolbar) findViewById(id);
    }

    protected DrawerLayout findDrawerLayout(int id) {
        return (DrawerLayout) findViewById(id);
    }

    protected NavigationView findNavigationView(int id) {
        return (NavigationView) findViewById(id);
    }

    protected BottomNavigationView findBottomNavigationView(int id) {
        return (BottomNavigationView) findViewById(id);
    }

    protected FloatingActionButton findFloatingActionButton(int id) {
        return (FloatingActionButton) findViewById(id);
    }

    protected RecyclerView findRecyclerView(int id) {
        return (RecyclerView) findViewById(id);
    }

    protected EditText findEditText(int id) {
        return (EditText) findViewById(id);
    }

    protected TextView findTextView(int id) {
        return (TextView) findViewById(id);
    }

    protected Button findButton(int id) {
        return (Button) findViewById(id);
    }

    protected CheckBox findCheckBox(int id) {
        return (CheckBox) findViewById(id);
    }

    protected Spinner findSpinner(int id) {
        return (Spinner) findViewById(id);
    }

    protected ProgressBar findProgressBar(int id) {
        return (ProgressBar) findViewById(id);
    }

    protected SeekBar findSeekBar(int id) {
        return (SeekBar) findViewById(id);
    }

    protected RatingBar findRatingBar(int id) {
        return (RatingBar) findViewById(id);
    }

    protected ImageView findImageView(int id) {
        return (ImageView) findViewById(id);
    }

    protected ImageButton findImageButton(int id) {
        return (ImageButton) findViewById(id);
    }

    protected SwipeRefreshLayout findSwipeRefreshLayout(int id) {
        return (SwipeRefreshLayout) findViewById(id);
    }

    protected LinearLayout findLinearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }

    protected View findView(int id) {
        return (View) findViewById(id);
    }
}
