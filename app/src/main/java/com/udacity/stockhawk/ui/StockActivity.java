package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;

import butterknife.ButterKnife;
import android.app.FragmentTransaction;

public class StockActivity extends AppCompatActivity {
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        symbol = intent.getStringExtra("symbol");
        if (findViewById(R.id.fragment_cointainer)!=null)
        {
            if (savedInstanceState!=null){return;}
            StockFragment sf = new StockFragment();
            Bundle bundle = new Bundle();
            bundle.putString("symbol", symbol);
            sf.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_cointainer, sf).commit();
        }

    }

}
