package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.StockActivity;

/**
 * Created by egonzalez on 4/4/17.
 */

public class StockAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
//            Intent intentStock = new Intent(context, StockActivity.class);
//            intentStock.putExtra("symbol", "GOOG");
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intentStock, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_appwidget);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
