package home.david.finances;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class DailyExpense extends AppWidgetProvider {

    private static final double allotted = 10.71;
    private static Database database;
    private static NumberFormat currency;

    private static String getDaysAmount(String day) {
        String result = "none";
        ArrayList<TableRow> data = database.selectRaw("select sum(cost) as total from expenses where date(datetime) = date('" + day + "') group by date(datetime)");
        if (data.size() > 0) {
            TableRow row = data.get(0);
            if (row.hasRowItem("total")) {
                result = currency.format(row.getRowItem("total"));
            }
        }
        return result;
    }

    private static String getMonthsAmount() {
        String result = "none";
        ArrayList<TableRow> data = database.selectRaw("select sum(cost) as total from expenses group by strftime('%Y%m', datetime) order by strftime('%Y%m', datetime) desc limit 1");
        if (data.size() > 0) {
            TableRow row = data.get(0);
            if (row.hasRowItem("total")) {
                result = currency.format(row.getRowItem("total"));
            }
        }
        return result;
    }

    private static String getWeeksAmount() {
        String result = "none";
        ArrayList<TableRow> data = database.selectRaw("select sum(cost) as total from expenses group by strftime('%Y%W', datetime) order by strftime('%Y%W', datetime) desc limit 1");
        if (data.size() > 0) {
            TableRow row = data.get(0);
            if (row.hasRowItem("total")) {
                result = currency.format(row.getRowItem("total"));
            }
        }
        return result;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        database = new Database(context);
        currency = NumberFormat.getCurrencyInstance();
        // There may be multiple widgets active, so update all of them
        ComponentName thisWidget = new ComponentName(context, DailyExpense.class);
        //TODO: form strings of today and the last couple of days
        //TODO: get database data for each of these days
        //TODO: what is the monthly total spent?
        //TODO: what is the weekly total spent?
        //TODO: compared to what they should be if I want to stay in good
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar day = Calendar.getInstance();
        Calendar now = (Calendar) day.clone();
        String today = sdf.format(day.getTime());
        day.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = sdf.format(day.getTime());
        day.add(Calendar.DAY_OF_YEAR, -1);
        String twoDaysAgo = sdf.format(day.getTime());
        day.add(Calendar.DAY_OF_YEAR, -1);
        String threeDaysAgo = sdf.format(day.getTime());
        day.add(Calendar.DAY_OF_YEAR, -1);
        String fourDaysAgo = sdf.format(day.getTime());
        double dailyMoney = (now.get(Calendar.DAY_OF_MONTH) * allotted);
        double weeklyMoney = (7 * allotted);
        double thisMonthlyMoney = (now.getActualMaximum(Calendar.DAY_OF_MONTH) * allotted);
        String text1 = "So far this month : " + currency.format(dailyMoney) +
                "\nweekly : " + currency.format(weeklyMoney) +
                "\nthis monthly : " + currency.format(thisMonthlyMoney);
        String text2 = today + " : " + getDaysAmount(today) + "\n"
                + yesterday + " : " + getDaysAmount(yesterday) + "\n"
                + twoDaysAgo + " : " + getDaysAmount(twoDaysAgo) + "\n"
                + threeDaysAgo + " : " + getDaysAmount(threeDaysAgo) + "\n"
                + fourDaysAgo + " : " + getDaysAmount(fourDaysAgo) +
                "\nThis week : " + getWeeksAmount()+
                "\nThis month : " + getMonthsAmount();

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.daily_expense);
            remoteViews.setTextViewText(R.id.appwidget_text1, text1);
            remoteViews.setTextViewText(R.id.appwidget_text2, text2);
            Intent intent = new Intent(context, DailyExpense.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_text2, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        //database=new Database(context);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void log(String message) {
        Log.i("DailyExpense", message);
    }
}

