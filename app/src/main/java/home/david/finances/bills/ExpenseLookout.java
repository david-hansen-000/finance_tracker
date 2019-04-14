package home.david.finances.bills;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import home.david.finances.Database;
import home.david.finances.R;
import home.david.finances.TableRow;

public class ExpenseLookout extends AppCompatActivity {
    private Database database;
    private home.david.finances.bills.BillAdapter billAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_lookout);
        ListView listView=findViewById(R.id.expense_lookout_list);
        float total=440.96f;
        billAdapter=new BillAdapter(this, total);
        database = new Database(this);

        ArrayList<TableRow> current = database.selectRaw("select * from bills");
        ArrayList<TableRow> defaults = database.selectRaw("select * from defaults");
        ArrayList<Bill> current_bills = new ArrayList<>();
        ArrayList<String> addThisDefault =new ArrayList<>();
        Date today = new Date();
        for (TableRow row : current) {
            Bill bill = new Bill(this);
            bill.setDatabase(database);
            bill.setType(row.getRowItemAsString("bill"));
            bill.setAmount(row.getRowItemAsString("amount"));
            bill.setDuedate(row.getRowItemAsString("duedate"));
            bill.setNegative(true);
            if (bill.getDuedate().compareTo(today) >= 0) {
                current_bills.add(bill);
            } else {
                addThisDefault.add(bill.getType());
            }

        }
        ArrayList<Bill> default_bills = new ArrayList<>();
        TableRow expense=new TableRow();
        expense.addRowItem("bill", "expense");
        expense.addRowItem("amount", "300");
        expense.addRowItem("duedate", "10");
        defaults.add(expense);
        for (TableRow row : defaults) {
            for (String name:addThisDefault) {
                if (row.getColumnNamesAsString().equals(name)) {
                    Bill bill = new Bill(this);
                    bill.setDatabase(database);
                    bill.setType(row.getRowItemAsString("bill"));
                    bill.setAmount(row.getRowItemAsString("amount"));
                    bill.setNegative(true);
                    if (!bill.getType().equals("paycheck")) {
                        String day = row.getRowItemAsString("duedate");
                        bill.setDuedate(getConvertedDefaultdate(today, day, 1, true));
                        if (bill.getDuedate().compareTo(today)>=0) {
                            default_bills.add(bill);
                        }
                    }
                }
            }
        }

        ArrayList<String> paydays = getPayDays(90);
        for (String day : paydays) {
            log("payday:"+day);
            Bill p = new Bill(this);
            p.setType("paycheck");
            p.setAmount(1060f);
            p.setNegative(false);
            p.setDuedate(day);
            default_bills.add(p);
        }

        //compareFirstValues(current_bills, default_bills);

        for (TableRow row : defaults) {
            Bill bill = new Bill(this);
            bill.setDatabase(database);
            bill.setType(row.getRowItemAsString("bill"));
            bill.setAmount(row.getRowItemAsString("amount"));
            if (!bill.getType().equals("paycheck")) {
                String day = row.getRowItemAsString("duedate");
                bill.setDuedate(getConvertedDefaultdate(today, day, 2, false));
                if (bill.getDuedate().compareTo(today)>=0) {
                    default_bills.add(bill);
                }
                Bill thirdBill = bill.copy();
                thirdBill.setDuedate(getConvertedDefaultdate(today, day, 3, false));
                if (bill.getDuedate().compareTo(today)>=0) {
                    default_bills.add(thirdBill);
                }
            }
        }
        ArrayList<Bill> completeList=new ArrayList<>();
        completeList.addAll(current_bills);
        completeList.addAll(default_bills);

        billAdapter.addAll(completeList);

        log("billAdapter size:"+billAdapter.getCount());
        listView.setAdapter(billAdapter);
        log("list size:"+listView.getCount());

        for (Bill bill:completeList) {
            if (bill.isNegative()) {
                total-=bill.getAmount();
            } else {
                total+=bill.getAmount();
            }
        }
        log("total comes to:"+total);
    }

    private void compareFirstValues(ArrayList<Bill> current_bills, ArrayList<Bill> default_bills) {
        ArrayList<String> types = new ArrayList<>();
        for (Bill bill : current_bills) {
            String type = bill.getType();
        }
        int limit = default_bills.size();
        for (int i = 0; i < limit; i++) {
            Bill bill = default_bills.get(i);
            if (!bill.getType().equals("paycheck")) {
                for (String name : types) {
                    if (bill.getType().equals(name)) {
                        default_bills.remove(bill);
                    }
                }
            }
        }
    }

    private ArrayList<String> getPayDays(int numDaysFromNow) {
        ArrayList<String> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate knownPayDate = LocalDate.of(2018, Month.SEPTEMBER, 20);
        for (int i = 0; i < numDaysFromNow; i++) {
            today = today.plusDays(1);
            Duration duration = Duration.between(knownPayDate.atStartOfDay(), today.atStartOfDay());
            int month = today.getMonthValue();
            if (duration.toDays() % 14 == 0) {
                result.add(today.getYear() + "-" + (month < 9 ? "0" + month : month) + "-" + today.getDayOfMonth());
            }
        }
        return result;
    }


    public Date getConvertedDefaultdate(Date today, String day, int months, boolean currentMonth) {
        String defaultDay = day;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(defaultDay));
        if (currentMonth) {
            if (c.getTime().compareTo(today) <= 0) {
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
            }
        } else {
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
        }

        return c.getTime();
    }

    private void log(String message) {
        Log.i("ExpenseLookout", message);
    }
}
