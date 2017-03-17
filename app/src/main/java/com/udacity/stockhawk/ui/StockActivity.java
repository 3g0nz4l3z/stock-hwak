package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class StockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int STOCK_LOADER = 0;
    @BindView(R.id.textViewName) TextView textViewName;
    @BindView(R.id.textViewSymbol) TextView textViewSymbol;
    @BindView(R.id.chartStockOverTime)  LineChartView lineChartView;
    String symbol;
    String companyName;
    List<PointValue> values;
    Line line;
    List<Line> lines;
    LineChartData cData;
    List<AxisValue> axisValueX;
    List<AxisValue> axisValueY;
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
        values = new ArrayList<>();
        List<AxisValue> axisValueX = new ArrayList<>();
        List<AxisValue> axisValueY = new ArrayList<>();

        try{
            data.moveToFirst();
            do{
                companyName = data.getString(Contract.Quote.POSITION_NAME);
                parseHistory(data.getString(Contract.Quote.POSITION_HISTORY), axisValueX, axisValueY);
            }while (data.moveToNext());
        }finally {
            data.close();
        }


        textViewName.setText(companyName);
        line = new  Line(values).setColor(Color.BLUE).setCubic(false);
        lines = new ArrayList<>();
        lines.add(line);


        cData = new LineChartData();
        cData.setLines(lines);
        Axis axisX = new Axis(axisValueX);
        cData.setAxisXBottom(axisX);
        Axis axisY = new Axis(axisValueY);
        cData.setAxisYLeft(axisY);

        lineChartView.setInteractive(false);
        lineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChartView.setLineChartData(cData);
        lineChartView.setVisibility(View.VISIBLE    );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Simple method to convert the history to and array of entries for the chart
     * @param string
     * @param axisValueX
     *@param axisValueY @return
     */
    private void parseHistory(String string, List<AxisValue> axisValueX, List<AxisValue> axisValueY){

        float counter = 0;
        for (String stringPair: string.split("\n")) {
            String [] stringPairSplited = stringPair.split(", ");
            values.add(new PointValue(Float.parseFloat(stringPairSplited[0]), Float.parseFloat(stringPairSplited[1])));
            String auxDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(stringPairSplited[0])));
            AxisValue auxAxisValueX = new AxisValue(counter);
            auxAxisValueX.setLabel(auxDate);
            AxisValue auxAxisValueY = new AxisValue(counter);
            auxAxisValueY.setLabel(stringPairSplited[1]);
            axisValueX.add(auxAxisValueX);
            axisValueY.add(auxAxisValueY);
            counter++;

        }
    }

}
