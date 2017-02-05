package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.DbHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Ashish on 11-12-2016.
 */

class StockWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;
    private int mWidgetId;
    final private DecimalFormat dollarFormatWithPlus;
    final private DecimalFormat dollarFormat;
    private DbHelper dbHelper;

    StockWidgetFactory(Context context, Intent intent) {
        mContext = context;
        dbHelper = new DbHelper(mContext);
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        if (mCursor.moveToPosition(position)) {
            rv.setTextViewText(R.id.symbol,
                    mCursor.getString(Contract.Quote.POSITION_SYMBOL));

            rv.setTextViewText(R.id.price,
                    dollarFormat.format(mCursor.getFloat(Contract.Quote.POSITION_PRICE)));

            float rawAbsoluteChange = mCursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            String changeString = dollarFormatWithPlus.format(rawAbsoluteChange);

            if (rawAbsoluteChange > 0) {
                rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }

            rv.setTextViewText(R.id.change,
                    changeString);
        }
        return rv;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        if (mCursor != null) {
            mCursor.close();
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        mCursor = db.query(Contract.Quote.TABLE_NAME,
                new String[]{Contract.Quote._ID,Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE,
                        Contract.Quote.COLUMN_ABSOLUTE_CHANGE},
                null,
                null,
                null,
                null,
                null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }
}
