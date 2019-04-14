package home.david.finances;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class MoneyView extends AppCompatActivity {
    private EditText macu;
    private EditText mn;
    private EditText history;
    private EditText future;
    private Button get;

    private MoneyAdapter historyAdapter;
    private MoneyAdapter futureAdapter;

    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_view_layout);

        database=new Database(this);

        macu=findViewById(R.id.money_view_macu_et);
        mn=findViewById(R.id.money_view_mn_et);
        history=findViewById(R.id.money_view_history_et);
        future=findViewById(R.id.money_view_future_et);
        historyAdapter=new MoneyAdapter(this);
        futureAdapter=new MoneyAdapter(this);
        get=findViewById(R.id.money_view_get_btn);


        get.setOnClickListener(view->{
            Date sqlDate=calculateHistory(history.getText().toString());
            Log.i("MoneyView", "sqlDate:"+sqlDate.toString());
            String historySql="select datetime, sum(cost) as cost from expenses where datetime in (select distinct datetime from expenses where date(datetime)>date('"+sqlDate.toString()+"') order by datetime desc) group by datetime";
            Log.i("MoneyView", "historySql:"+historySql);
            ArrayList<TableRow> rows=database.selectRaw(historySql);
            MoneyItem item=null;
            ArrayList<MoneyItem> items=new ArrayList<>();
            if (rows.size()>0) {
                for(TableRow row:rows) {
                    item = new MoneyItem(row, true);
                    item.setName("receipt expense");
                    items.add(item);
                }
            }

            Log.i("MoneyView", "items has:"+items.size());

           historyAdapter.addAll(items);
            ListView historyList=findViewById(R.id.money_view_history_list);
            historyList.setAdapter(historyAdapter);

            ArrayList<TableRow> bills=database.selectRaw("select * from bills");
            ArrayList<MoneyItem> future_items=new ArrayList<>();
            for (TableRow row:bills) {
                future_items.add(new MoneyItem(row, true));
            }
            log("future_items after bills added has:"+future_items.size());
            ArrayList<TableRow> income=database.selectRaw("select * from income limit 2");
            for (TableRow row:income) {
                future_items.add(new MoneyItem(row, false));
            }
            log("future_items after income added has:"+future_items.size());
            futureAdapter.addAll(future_items);
            ListView futureList=findViewById(R.id.money_view_future_list);
            futureList.setAdapter(futureAdapter);

        });



    }

    private void log(String message) {
        Log.i("MoneyView", message);
    }
    private Date calculateHistory(String s) {
        Date result=null;
        int daysbefore=Integer.parseInt(s);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)-daysbefore);
        result=new Date(c.getTimeInMillis());
        return result;
    }

    //TODO: set up a list, like with the bills
    //--but add entries for income
    //--and other values, money spent in the day
    //--receipt entries (consolidated for receipt, ie distinct datetime sums)
    //allow an initial value for bank input
    //or just a current bank amount for time and date
    //and then a timeset some past purchases and future expected purchases/bills


    //ok, basically

    //Current Macu Bank Account Amount:___________________
    //Current MN Bank Account Amount:_______________
    //Days of history:________________
    //Days of Future:_________________

    //List date   sum (values of distinct datetime entries and the sum(cost) for that datetime entry)
    //List of future  bills (using default value from bills table)
    //--above list interspersed with projected income and projected expenses (like food, entertainment, etc)

    // history list not editable, but future expected bills and income should be.
    // history list should also contain past income.

    //TODO: create table for income (date text, amount text, bankaccount text)
    //TODO: create table future expenses (date text, amount text, category text)
    //calculate out future income by using known Thursday payday and calculating new income payday
    //--every 14 days for duration of future days time set.
    //TODO: add default value in defaults table for income about $900 


}
