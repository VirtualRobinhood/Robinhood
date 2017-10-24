package com.codepath.virtualrobinhood.adaptors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;

/**
 * Created by SagarMutha on 10/17/17.
 */

public class SearchFeedResultsAdaptor extends SimpleCursorAdapter {
    private static final String tag = SearchFeedResultsAdaptor.class.getName();
    private Context context = null;

    public SearchFeedResultsAdaptor(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvSymbol = view.findViewById(R.id.tvSymbol);
        TextView tvCompanyName = view.findViewById(R.id.tvCompanyName);

        tvSymbol.setText(cursor.getString(1));
        tvCompanyName.setText(cursor.getString(2));
    }
}