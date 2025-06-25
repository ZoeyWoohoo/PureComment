package cn.showzeng.demo.Fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import cn.showzeng.demo.Interface.DialogFragmentDataCallback
import cn.showzeng.demo.R

/**
 * Created by zoey on 17-8-11.
 *
 * Email: kingstageshow@gmail.com
 *
 * GitHub: [zoey](https://github.com/zoeywoohoo)
 */
class CommentDialogFragment : DialogFragment(), View.OnClickListener {
    private var commentEditText: EditText? = null
    private var photoButton: ImageView? = null
    private var atButton: ImageView? = null
    private var sendButton: ImageView? = null
    private var inputMethodManager: InputMethodManager? = null
    private var dataCallback: DialogFragmentDataCallback? = null

    override fun onAttach(context: Context) {
        check(activity is DialogFragmentDataCallback) {
            "DialogFragment 所在的 activity 必须实现 DialogFragmentDataCallback 接口"
        }
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog = Dialog(requireContext(), R.style.BottomDialog)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_fragment_comment_layout)
        mDialog.setCanceledOnTouchOutside(true)

        val window = mDialog.window
        val layoutParams: WindowManager.LayoutParams
        if (window != null) {
            layoutParams = window.attributes
            layoutParams.gravity = Gravity.BOTTOM
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            window.setAttributes(layoutParams)
        }

        commentEditText = mDialog.findViewById<View?>(R.id.edit_comment) as EditText
        photoButton = mDialog.findViewById<View?>(R.id.image_btn_photo) as ImageView
        atButton = mDialog.findViewById<View?>(R.id.image_btn_at) as ImageView
        sendButton = mDialog.findViewById<View?>(R.id.image_btn_comment_send) as ImageView

        fillEditText()
        setSoftKeyboard()

        commentEditText?.addTextChangedListener(mTextWatcher)
        photoButton?.setOnClickListener(this)
        atButton?.setOnClickListener(this)

        sendButton?.setOnClickListener(this)

        return mDialog
    }

    private fun fillEditText() {
        dataCallback = activity as? DialogFragmentDataCallback
        commentEditText?.setText(dataCallback?.getCommentText() ?: "")
        commentEditText?.setSelection(dataCallback?.getCommentText()?.length ?: 0)
        if (dataCallback?.getCommentText()?.length == 0) {
            sendButton?.setEnabled(false)
            sendButton?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.iconCover))
        }
    }

    private fun setSoftKeyboard() {
        commentEditText?.setFocusable(true)
        commentEditText?.setFocusableInTouchMode(true)
        commentEditText?.requestFocus()

        // 为 commentEditText 设置监听器，在 DialogFragment 绘制完后立即呼出软键盘，呼出成功后即注销
        commentEditText?.getViewTreeObserver()?.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    inputMethodManager = activity?.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as? InputMethodManager
                    if (inputMethodManager?.showSoftInput(commentEditText, 0) == true) {
                        commentEditText?.getViewTreeObserver()?.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        )
    }

    private val mTextWatcher: TextWatcher = object : TextWatcher {
        private var temp: CharSequence? = null

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            temp = s
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (temp?.isNotEmpty() == true) {
                sendButton?.setEnabled(true)
                sendButton?.isClickable = true
                sendButton?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.colorAccent)
                )
            } else {
                sendButton?.setEnabled(false)
                sendButton?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.iconCover)
                )
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.image_btn_photo -> Toast.makeText(
                context,
                "Pick photo Activity",
                Toast.LENGTH_SHORT
            ).show()

            R.id.image_btn_at -> Toast.makeText(
                context,
                "Pick people you want to at Activity",
                Toast.LENGTH_SHORT
            ).show()

            R.id.image_btn_comment_send -> {
                Toast.makeText(
                    context,
                    commentEditText?.getText().toString(),
                    Toast.LENGTH_SHORT
                ).show()
                commentEditText?.setText("")
                dismiss()
            }

            else -> {}
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        dataCallback?.setCommentText(commentEditText?.getText()?.toString())
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        dataCallback?.setCommentText(commentEditText?.getText()?.toString())
        super.onCancel(dialog)
    }
}