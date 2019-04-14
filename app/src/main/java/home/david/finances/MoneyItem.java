package home.david.finances;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MoneyItem implements Comparable<MoneyItem> {
    private String name;
    private String amount;
    private String duedate;
    private Date compareDate;

    public MoneyItem() {
        name=null;
        amount=null;
        duedate=null;
        compareDate=null;
    }

    public MoneyItem(Bill bill) {
        name=bill.getBill_name();
        amount=bill.getBill_amount();
        duedate=bill.getBill_duedate();
        compareDate=convertDate(duedate);
    }

    public MoneyItem(TableRow row, boolean makeNegative) {
        if (row.hasRowItem("item")) {
            name=row.getRowItemAsString("item");
        }
        if (row.hasRowItem("datetime")) {
            duedate=row.getRowItemAsString("datetime");
        }
        if (row.hasRowItem("cost")) {
            amount=row.getRowItemAsString("cost");
        }
        if (row.hasRowItem("bill")) {
            name=row.getRowItemAsString("bill");
        }
        if (row.hasRowItem("amount")) {
            String negSign="";
            if (makeNegative) {
                negSign="-";
            }
            amount=negSign+row.getRowItemAsString("amount");
        }
        if (row.hasRowItem("duedate")) {
            duedate=row.getRowItemAsString("duedate");
            if (duedate.length()==2 || duedate.length()==1) {
                duedate=convertFutureDueDate(duedate);
            }
        }
        compareDate=convertDate(duedate);
    }

    public String convertFutureDueDate(String num) {
        Date date=new Date();
        String defaultDay = num;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(defaultDay));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date.compareTo(c.getTime()) < 0) {
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        }
        return sdf.format(c.getTime());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
        compareDate=convertDate(duedate);
    }

    public String id(String obj) {
        if (obj==name) return "name";
        if (obj==amount) return "amount";
        if (obj==duedate) return "duedate";
        return null;
    }

    public Date convertDate(String d) {
        Date date=null;
        SimpleDateFormat sdf=null;
        if (d.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d\\.\\d")) {
            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");

        }
        if (d.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
            sdf=new SimpleDateFormat("yyyy-MM-dd");
        }
        if (sdf!=null) {
            try {
                date = sdf.parse(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    @Override
    public int compareTo(@NonNull MoneyItem o) {
        return compareDate.compareTo(o.compareDate);
    }
}
