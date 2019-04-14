package home.david.finances;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BillLayout extends LinearLayout {

    private String bill_text;
    private String amount_text;
    private float amount_weight;
    private String date_text;
    private float date_weight;
    private String btn_text;
    private float btn_weight;
    private LinearLayout inner2;

    private TextView bill_textview;
    private EditText bill_amount;
    private EditText bill_date;
    private Button bill_btn;

    private Context context;

    public BillLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BillLayout, 0, 0);
        bill_text=a.getString(R.styleable.BillLayout_text);
        amount_text=a.getString(R.styleable.BillLayout_amount);
        amount_weight=a.getFloat(R.styleable.BillLayout_amount_weight, 0.4f);
        date_text=a.getString(R.styleable.BillLayout_date);
        date_weight=a.getFloat(R.styleable.BillLayout_date_weight, 0.3f);
        btn_text=a.getString(R.styleable.BillLayout_btnText);
        btn_weight=a.getFloat(R.styleable.BillLayout_btn_weight, 0.3f);
        a.recycle();

        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bill_layout, this, true);
        //childrenList(this);

        LinearLayout main= (LinearLayout) getChildAt(0);
        LinearLayout inner1= (LinearLayout) main.getChildAt(0);
        inner2= (LinearLayout) main.getChildAt(1);
        bill_textview= (TextView) inner1.getChildAt(0);
        bill_amount=(EditText)inner2.getChildAt(0);
        bill_date = (EditText)inner2.getChildAt(1);
        bill_btn=(Button)inner2.getChildAt(2);

        bill_textview.setText(bill_text);
        bill_amount.setText(amount_text);
        setAmount_weight(amount_weight);
        bill_date.setText(date_text);
        setDate_weight(date_weight);
        bill_btn.setText(btn_text);
        setBtn_weight(btn_weight);

    }

    public String getAmount_text() {
        return bill_amount.getText().toString();
    }

    public void setAmount_text(String amount_text) {
        this.amount_text = amount_text;
        bill_amount.setText(amount_text);
        invalidate();
        requestLayout();
    }

    public float getAmount_weight() {
        ViewGroup.LayoutParams amount_lp=bill_amount.getLayoutParams();
        ViewGroup.LayoutParams lllp=inner2.getLayoutParams();
        amount_weight=lllp.width/amount_lp.width;
        return amount_weight;
    }

    public void setAmount_weight(float amount_weight) {
        this.amount_weight = amount_weight;
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lp.weight=amount_weight;
        bill_amount.setLayoutParams(lp);
        invalidate();
        requestLayout();
    }

    public String getDate_text() {
        date_text=bill_date.getText().toString();
        return date_text;
    }

    public void setDate_text(String date_text) {
        this.date_text = date_text;
        bill_date.setText(date_text);
        invalidate();
        requestLayout();
    }

    public void setDate_weight(float date_weight) {
        this.date_weight = date_weight;
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lp.weight=date_weight;
        bill_date.setLayoutParams(lp);
        invalidate();
        requestLayout();
    }

    public String getBtn_text() {
        btn_text=bill_btn.getText().toString();
        return btn_text;
    }

    public void setBtn_text(String btn_text) {
        this.btn_text = btn_text;
        bill_btn.setText(btn_text);
        invalidate();
        requestLayout();
    }

    public void setBtn_weight(float btn_weight) {
        this.btn_weight = btn_weight;
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lp.weight = btn_weight;
        bill_btn.setLayoutParams(lp);
        invalidate();
        requestLayout();
    }

    public void setBill_text(String text) {
        bill_text=text;
        bill_textview.setText(text);
        invalidate();
        requestLayout();
    }

    public void setBtnAction(View.OnClickListener action) {
        bill_btn.setOnClickListener(action);
    }

    private void log(String message) {
        Log.v("BillLayout", message);
    }

    private void childrenList(ViewGroup group) {
        log("group:"+group.toString());
        int count=group.getChildCount();
        for (int i=0; i<count; i++) {
            View v=group.getChildAt(i);
            log("child:["+i+"]="+v.toString());
            if (v instanceof ViewGroup) {
                childrenList((ViewGroup)v);
            }
        }
    }
}
