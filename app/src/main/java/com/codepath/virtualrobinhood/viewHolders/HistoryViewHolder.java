package com.codepath.virtualrobinhood.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.History;

/**
 * Created by GANESH on 10/19/17.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {

public TextView tvSymbolHistory;
public TextView tvPriceHistory;

public HistoryViewHolder(View itemView) {
        super(itemView);

        tvSymbolHistory= itemView.findViewById(R.id.tvSymbolHistory);
        tvPriceHistory = itemView.findViewById(R.id.tvPriceHistory);
        }

    public void bindToPost(History history) {
        tvSymbolHistory.setText(history.symbol);
        tvPriceHistory.setText(history.stockPrice.toString());
    }
}
