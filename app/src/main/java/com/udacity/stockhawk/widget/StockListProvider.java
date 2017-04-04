package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.common.util.concurrent.ExecutionError;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;

import yahoofinance.quotes.QuotesProperty;

/**
 * Created by m4ch1n3 on 4/4/2017.
 */

class StockListProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetId;
    private Cursor cursor;

    public StockListProvider(Context applicationContext, Intent intent) {
        this.context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        try{
            cursor.close();
        }catch(Exception e){

        }
        cursor = context.getContentResolver().query(
                Contract.Quote.URI,
                new String[]{Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE,
                        Contract.Quote.COLUMN_PERCENTAGE_CHANGE}, Contract.Quote.POSITION_ID+" = ?", new String[]{"1"}, null);
    }

    @Override
    public void onDestroy() {
        try {
            cursor.close();
        }catch (Exception e){

        }
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
            context.getPackageName(), R.layout.list_item_quote
        );
        if (cursor.moveToPosition(position)){
            remoteView.setTextViewText(R.id.symbol, cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL)));
            remoteView.setTextViewText(R.id.price , cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
            remoteView.setTextViewText(R.id.change , cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE)));

        }
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
