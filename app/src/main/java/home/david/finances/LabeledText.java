package home.david.finances;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by david on 2/16/18.
 */

public class LabeledText extends LinearLayout {
    private EditText edit;
    private TextView label;
    private View nextFocusView;

    public LabeledText(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabeledField, 0, 0);
        String label_text=a.getString(R.styleable.LabeledField_label);
        String edit_text=a.getString(R.styleable.LabeledField_text);
        int input_type=a.getInteger(R.styleable.LabeledField_input_type, 3);
        a.recycle();
        setOrientation(LinearLayout.HORIZONTAL);
        setWeightSum(1);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.labeledtext, this, true);
        label= (TextView) getChildAt(0);
        edit=(EditText)getChildAt(1);
        label.setText(label_text);
        edit.setText(edit_text);
        //edit.setOnEditorActionListener((v, actionId, event) -> editAction());
            switch (input_type) {
                case 0: {
                    edit.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                    break;
                }
                case 1: {
                    edit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
                    break;
                }
                case 2: {
                    edit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    break;
                }
                default: {
                    edit.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

    }

    private boolean editAction() {
        if (nextFocusView!=null) {
            nextFocusView.requestFocus();
        }
        return false;
    }

    public void addTextListener(TextWatcher watcher) {
        edit.addTextChangedListener(watcher);
    }

    public LabeledText(Context context) {
        this(context, null);
    }

    public String getText() {
        return edit.getText().toString();
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setNextFocus(View v) {
        nextFocusView=v;
    }
}
