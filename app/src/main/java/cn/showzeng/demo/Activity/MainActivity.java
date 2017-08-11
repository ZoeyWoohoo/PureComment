package cn.showzeng.demo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.showzeng.demo.Fragment.CommentDialogFragment;
import cn.showzeng.demo.Interface.DialogFragmentDataCallback;
import cn.showzeng.demo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogFragmentDataCallback{

    private TextView commentFakeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commentFakeButton = (TextView) findViewById(R.id.tv_comment_fake_button);
        commentFakeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_comment_fake_button:
                CommentDialogFragment commentDialogFragment = new CommentDialogFragment();
                commentDialogFragment.show(getFragmentManager(), "CommentDialogFragment");
                break;
            default:
                break;
        }
    }

    @Override
    public String getCommentText() {
        return commentFakeButton.getText().toString();
    }

    @Override
    public void setCommentText(String commentTextTemp) {
        commentFakeButton.setText(commentTextTemp);
    }
}
