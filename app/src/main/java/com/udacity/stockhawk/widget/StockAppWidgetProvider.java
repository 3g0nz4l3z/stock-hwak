package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
 if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stock_appwidget);
            Intent swsIntent = new Intent(context, StockWidgetService.class);
            swsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            swsIntent.setData(Uri.parse(swsIntent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(appWidgetId, R.id.stockWidgetListView, swsIntent);

            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int appWidgetIds[] = appWidgetManager
                    .getAppWidgetIds(new ComponentName(context, StockAppWidgetProvider.class));

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                    R.id.stockWidgetListView);

     }
    }
}
