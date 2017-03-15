package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int STOCK_LOADER = 0;
    @BindView(R.id.textViewName) TextView textViewName;
    @BindView(R.id.textViewSymbol) TextView textViewSymbol;
    @BindView(R.id.chartStockOverTime) LineChart lineChartStockOverTime;
    String symbol;
    String companyName;
    ArrayList<Entry> arrayListHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        symbol = intent.getStringExtra("symbol");
        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
        textViewSymbol.setText(symbol);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Quote.makeUriForStock(symbol),
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try{
            data.moveToFirst();
            do{
                companyName = data.getString(Contract.Quote.POSITION_NAME);
                arrayListHistory = parseHistory(data.getString(Contract.Quote.POSITION_HISTORY));
            }while (data.moveToNext());
        }finally {
            data.close();
        }
        textViewName.setText(companyName);
        LineDataSet dataSet = new LineDataSet(arrayListHistory, "Label");
        LineData lineData = new LineData(dataSet);
        lineChartStockOverTime.setData(lineData);
        lineChartStockOverTime.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Simple method to convert the history to and array of entries for the chart
     * @param string
     * @return
     */
    private ArrayList<Entry> parseHistory(String string){
        ArrayList<Entry> arrayDateXValue = new ArrayList<>();

        for (String stringPair: string.split("\n")) {
            String [] stringPairSplited = stringPair.split(", ");
            arrayDateXValue.add(new Entry(Float.parseFloat(stringPairSplited[0]), Float.parseFloat(stringPairSplited[1])));
        }
        return arrayDateXValue;
    }
}
