package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_appwidget);

            Intent swsIntent = new Intent(context, StockWidgetService.class);
            swsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            swsIntent.setData(Uri.parse(swsIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(appWidgetId, R.id.stockWidgetListView, swsIntent);


            appWidgetManager.updateAppWidget(appWidgetId, views);

            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}
