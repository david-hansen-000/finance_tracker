package home.david.finances;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class Bill implements Comparable<Bill> {
    private String bill_name;
    private String bill_duedate;
    private String bill_amount;
    private String bill_text;
    private String bill_button_text;
    private View.OnClickListener button_action;
    private Date date;
    private Date compareDate;
    private TextView amount_tv;
    private TextView duedate_tv;
    private TextView total_tv;

    public Bill() {
        bill_amount=null;
        bill_button_text=null;
        bill_duedate=null;
        bill_name=null;
        bill_text=null;
        button_action=null;
    }

    public String getBill_name() {
        return bill_name;
    }

    public void setBill_name(String bill_name) {
        this.bill_name = bill_name;
    }

    public String getBill_duedate() {
        return bill_duedate;
    }

    public void setBill_duedate(String bill_duedate) {
        this.bill_duedate = bill_duedate;
        compareDate = Bill.convertDate(bill_duedate);
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getBill_text() {
        return bill_text;
    }

    public void setBill_text(String bill_text) {
        this.bill_text = bill_text;
    }


    public View.OnClickListener getButton_action() {
        return button_action;
    }

    public void setButton_action(View.OnClickListener button_action) {
        this.button_action = button_action;
    }


    public static Date convertDate(String d) {
        Date date=null;
        int month=0;
        int day=0;
        int year=0;
        if (d.matches("\\d\\d[-|/]\\d\\d[-|/]\\d\\d")) {
            month=Integer.parseInt(d.substring(0,2));
            day=Integer.parseInt(d.substring(3,5));
            year=2000+Integer.parseInt(d.substring(6, 7));
        }
        if (d.matches("\\d\\d\\d\\d[-|/]\\d\\d[-|/]\\d\\d")) {
            year=Integer.parseInt(d.substring(0,4));
            month=Integer.parseInt(d.substring(5,7));
            day=Integer.parseInt(d.substring(8,9));
        }
        if (d.matches("\\d\\d[-|/]\\d\\d[-|/]\\d\\d\\d\\d")) {
            month=Integer.parseInt(d.substring(0,2));
            day=Integer.parseInt(d.substring(3,5));
            year=2000+Integer.parseInt(d.substring(6, 10));
        }

        Calendar.Builder builder=new Calendar.Builder();
        builder.setDate(year, month, day);
        date=builder.build().getTime();
        return date;
    }

    public void setAmount_tv(EditText et) {
        amount_tv =et;
    }

    public void setDuedate_tv(EditText et) {
        duedate_tv =et;
    }

    public String getAmoutEtText() {
        return amount_tv.getText().toString();
    }

    public String getDuedateEtText() {
        return duedate_tv.getText().toString();
    }

    @Override
    public int compareTo(Bill b) {
        return this.compareDate.compareTo(b.compareDate);
    }
}
