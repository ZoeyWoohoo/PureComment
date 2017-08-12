package cn.showzeng.demo.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.showzeng.demo.Interface.DialogFragmentDataCallback;
import cn.showzeng.demo.R;

/**
 * Created by showzeng on 17-8-11.
 * Email: kingstageshow@gmail.com
 * Github: https://github.com/showzeng
 */

public class CommentDialogFragment extends DialogFragment implements View.OnClickListener{

    private Dialog mDialog;
    private EditText commentEditText;
    private ImageView photoButton;
    private ImageView atButton;
    private ImageView sendButton;
    private InputMethodManager inputMethodManager;
    private DialogFragmentDataCallback dataCallback;

    @Override
    public void onAttach(Context context) {
        if (!(getActivity() instanceof DialogFragmentDataCallback)) {
            throw new IllegalStateException("DialogFragment 所在的 activity 必须实现 DialogFragmentDataCallback 接口");
        }
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDialog = new Dialog(getActivity(), R.style.BottomDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_fragment_comment_layout);
        mDialog.setCanceledOnTouchOutside(true);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);

        commentEditText = (EditText) mDialog.findViewById(R.id.edit_comment);
        photoButton = (ImageView) mDialog.findViewById(R.id.image_btn_photo);
        atButton = (ImageView) mDialog.findViewById(R.id.image_btn_at);
        sendButton = (ImageView) mDialog.findViewById(R.id.image_btn_comment_send);

        fillEditText();
        setSoftKeyboard();

        commentEditText.addTextChangedListener(mTextWatcher);
        photoButton.setOnClickListener(this);
        atButton.setOnClickListener(this);

        sendButton.setOnClickListener(this);

        return mDialog;
    }

    private void fillEditText() {
        dataCallback = (DialogFragmentDataCallback) getActivity();
        commentEditText.setText(dataCallback.getCommentText());
        commentEditText.setSelection(dataCallback.getCommentText().length());
        if (dataCallback.getCommentText().length() == 0) {
            sendButton.setEnabled(false);
            sendButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.iconCover));
        }
    }

    private void setSoftKeyboard() {
        commentEditText.setFocusable(true);
        commentEditText.setFocusableInTouchMode(true);
        commentEditText.requestFocus();

        // TODO: 17-8-11 为何这里要延时才能弹出软键盘, 延时时长又如何判断？ 目前是手动调试
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 110);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 0) {
                sendButton.setEnabled(true);
                sendButton.setClickable(true);
                sendButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            } else {
                sendButton.setEnabled(false);
                sendButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.iconCover));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_btn_photo:
                Toast.makeText(getActivity(), "Pick photo Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_btn_at:
                Toast.makeText(getActivity(), "Pick people you want to at Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_btn_comment_send:
                Toast.makeText(getActivity(), commentEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                commentEditText.setText("");
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dataCallback.setCommentText(commentEditText.getText().toString());
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dataCallback.setCommentText(commentEditText.getText().toString());
        super.onCancel(dialog);
    }
}
