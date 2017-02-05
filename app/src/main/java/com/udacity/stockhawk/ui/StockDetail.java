package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 08-12-2016.
 */

public class StockDetail extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    String symbol, history, name, stockExchange, currency;
    Double price, absoluteChange, percentChange;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    TextView nameTexView;
    @BindView(R.id.stockExchange)
    TextView stockExchangeTexView;
    @BindView(R.id.price)
    TextView priceTextView;
    @BindView(R.id.combinedChart)
    LineChart mChart;
    java.text.DecimalFormat decimalFormat;
    private String formattedPrice;
    //private List<Float> yfloatValues;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        decimalFormat = (java.text.DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        decimalFormat.setMaximumFractionDigits(2);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            symbol = b.getString(StockUtils.SYMBOL);
            history = b.getString(StockUtils.HISTORY);
            name = b.getString(StockUtils.NAME);
            stockExchange = b.getString(StockUtils.STOCK_EXCHANGE);
            currency = b.getString(StockUtils.CURRENCY);

            price = b.getDouble(StockUtils.PRICE);
            formattedPrice = decimalFormat.format(price);
            absoluteChange = b.getDouble(StockUtils.ABSOLUTE_CHANGE);
            percentChange = b.getDouble(StockUtils.PERCENTAGE_CHANGE);

        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(symbol);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String historyList[] = history.split("\n");
        nameTexView.setText(name);
        stockExchangeTexView.setText(stockExchange);
        priceTextView.setText(formattedPrice);

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String st = historyList[i];
            float value = getYvalues(st);
            dates.add(getXvalues(st));
            entries.add(new Entry(value,i));
        }

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        LineData data = new LineData(dates, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        mChart.setData(data);
        mChart.animateY(500);

    }

    private float getYvalues(String st) {

        String splittedXandYvalues[] = st.split(",");

        return Float.parseFloat(splittedXandYvalues[1]);
    }
    private String getXvalues(String st) {

        String splittedXandYvalues[] = st.split(",");

        Long date = Long.parseLong(splittedXandYvalues[0]);

        return convertMillisToDate(date);
    }

    private String convertMillisToDate(Long date) {

        String dateFormat = "dd/MM/yyyy";
// Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat,Locale.US);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
