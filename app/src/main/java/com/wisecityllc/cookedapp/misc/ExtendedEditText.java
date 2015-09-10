package com.wisecityllc.cookedapp.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wisecityllc.cookedapp.App;

/**
 * This class overrides the onKeyPreIme method to dispatch a key event if the
 * KeyEvent passed to onKeyPreIme has a key code of KeyEvent.KEYCODE_BACK.
 * This allows key event listeners to detect that the soft keyboard was
 * dismissed.
 *
 */
public class ExtendedEditText extends EditText {

    private OnBackButtonPressedWhileHasFocusListener mListener;

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ExtendedEditText(Context context) {
        super(context);

    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (hasFocus() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // User has pressed Back key. So hide the keyboard InputMethodManager
            InputMethodManager mgr = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            // TODO: Hide your view as you do it in your activity
            //dispatchKeyEvent(event);
            if(mListener != null)
                mListener.pressedBackWithFocus();
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setListener(OnBackButtonPressedWhileHasFocusListener listener){
        mListener = listener;
    }

    public interface OnBackButtonPressedWhileHasFocusListener {
        public void pressedBackWithFocus();
    }

}