package cn.showzeng.demo.Activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import cn.showzeng.demo.Fragment.CommentDialogFragment
import cn.showzeng.demo.Interface.DialogFragmentDataCallback
import cn.showzeng.demo.R

class MainActivity : FragmentActivity(), View.OnClickListener, DialogFragmentDataCallback {

    private var commentFakeButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        commentFakeButton = findViewById<View?>(R.id.tv_comment_fake_button) as? TextView
        commentFakeButton?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_comment_fake_button -> {
                CommentDialogFragment().show(supportFragmentManager, "CommentDialogFragment")
            }

            else -> {}
        }
    }

    override fun getCommentText(): String {
        return commentFakeButton?.getText()?.toString() ?: ""
    }

    override fun setCommentText(commentTextTemp: String?) {
        commentFakeButton?.text = commentTextTemp
    }
}
