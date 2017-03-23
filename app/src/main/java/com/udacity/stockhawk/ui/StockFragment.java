package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import timber.log.Timber;

/**
 * Created by m4ch1n3 on 18/03/17.
 */

public class StockFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener{
    private static final int STOCK_LOADER = 0;
    @BindView(R.id.textViewCompanyNameValue) TextView TextViewCompanyNameValue;
    @BindView(R.id.textViewSymbolValue) TextView TextViewSymbolValue;
    @BindView(R.id.chartStockOverTime) LineChartView lineChartView;

    @BindView(R.id.stock_fragment_sf) SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;
    private String symbol;
    private String companyName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle !=null)
        {
            symbol = bundle.getString("symbol");
        }
        getLoaderManager().initLoader(STOCK_LOADER, null, this );
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
        return view;

    }



//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(StockFragment.this.getContext(), Contract.Quote.makeUriForStock(symbol), Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}), null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Pair<String, String>> arrayOfPair = new ArrayList<>();
        try {
            if (data.moveToFirst()) {
                do {
                    companyName = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_NAME));
                    historyToArrayOfPair(arrayOfPair, data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY)));
                } while (data.moveToNext());
            }
        }catch(Exception e){

        }finally {
            data.close();
        }
        TextViewSymbolValue.setText(symbol);
        TextViewCompanyNameValue.setText(companyName);
        drawLineChart(arrayOfPair);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);

    }

    public void historyToArrayOfPair(ArrayList<Pair<String, String>> arrayOfPair, String string){
        String [] historyStrings = string.split("\n");

        for (String auxString: historyStrings
             ) {
                String[] dateXValue = auxString.split(",");
                arrayOfPair.add(new Pair<String, String>(dateXValue[0],dateXValue[1]));
        }
    }

    public void drawLineChart(ArrayList<Pair<String, String>> arrayOfPair){
        ArrayList<AxisValue> aVXs = new ArrayList<>();
        ArrayList<PointValue> pValues = new ArrayList<>();
        PointValue auxPointValue;
        int counter = 0;
        for (float i = arrayOfPair.size() - 1; i >= 0; i-- ){
            String auxPairDate = arrayOfPair.get(Math.round(i)).first;
            String auxPairOpen = arrayOfPair.get(Math.round(i)).second;
            String auxDate = new SimpleDateFormat("d/M/yy").format(new Date(Long.parseLong(auxPairDate)));
            auxPointValue = new PointValue(counter, Float.parseFloat(auxPairOpen));
            auxPointValue.setLabel(auxDate);
            pValues.add(auxPointValue);

            if(counter != 0 && counter % 20 == 0)
            {
                AxisValue aVX = new AxisValue(counter);
                aVX.setLabel(auxDate);
                aVXs.add(aVX);

            }


            counter++;
        }

        Line line = new Line(pValues);
        line.setColor(getResources().getColor(R.color.colorPrimaryDark));
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis aX = new Axis(aVXs);
        aX.setHasLines(true);

        data.setAxisXBottom(aX);
        Axis aY = new Axis();
        aY.setAutoGenerated(true);
        data.setAxisYLeft(aY);

        lineChartView.setLineChartData(data);
        lineChartView.setInteractive(false);
    }
}
