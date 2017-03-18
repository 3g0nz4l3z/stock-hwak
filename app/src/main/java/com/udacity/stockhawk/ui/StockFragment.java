package com.udacity.stockhawk.ui;

import android.database.Cursor;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.view.LineChartView;

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
        drawLineChart(lineChartView);
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

    public void drawLineChart(LineChartView lineChartView){

    }
}
