package com.qiniu.droid.niuplayer.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.qiniu.droid.niuplayer.R;

public class CommomDialog extends Dialog implements View.OnClickListener {
    private TextView mTitleTxt;
    private TextView mSubmitTxt;
    private TextView mCancelTxt;
    private EditText mEditText;

    private Activity mActivity;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public CommomDialog(Activity activity) {
        this(activity, R.style.dialog, null);
    }

    public CommomDialog(Activity activity, int themeResId, String content) {
        super(activity, themeResId);
        this.mActivity = activity;
    }

    public CommomDialog(Activity activity, int themeResId, String content, OnCloseListener listener) {
        super(activity, themeResId);
        this.mActivity = activity;
        this.listener = listener;
    }

    protected CommomDialog(Activity activity, boolean cancelable, OnCancelListener cancelListener) {
        super(activity, cancelable, cancelListener);
        this.mActivity = activity;
    }

    public String getEditString() {
        return mEditText.getText().toString();
    }

    public CommomDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommomDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public CommomDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mTitleTxt = (TextView) findViewById(R.id.title);
        mSubmitTxt = (TextView) findViewById(R.id.submit);
        mSubmitTxt.setOnClickListener(this);
        mCancelTxt = (TextView) findViewById(R.id.cancel);
        mEditText = findViewById(R.id.edit_text);
        mCancelTxt.setOnClickListener(this);

        if (!TextUtils.isEmpty(positiveName)) {
            mSubmitTxt.setText(positiveName);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            mCancelTxt.setText(negativeName);
        }

        if (!TextUtils.isEmpty(title)) {
            mTitleTxt.setText(title);
        }

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.75);
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}