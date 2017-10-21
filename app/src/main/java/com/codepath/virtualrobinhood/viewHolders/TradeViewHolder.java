package com.codepath.virtualrobinhood.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Trade;

import java.text.DecimalFormat;


public class TradeViewHolder extends RecyclerView.ViewHolder {

    public TextView tvSymbol;
    public TextView tvPrice;
    public TextView tvDate;

    public TradeViewHolder(View itemView) {
        super(itemView);

        tvSymbol = itemView.findViewById(R.id.tvSymbol);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvDate = itemView.findViewById(R.id.tvDate);
    }

    public void bindToPost(Trade trade) {
        DecimalFormat df = new DecimalFormat("##.##");
        tvSymbol.setText(trade.symbol);
        tvPrice.setText(df.format(trade.price));
        tvDate.setText(trade.filledOn);
    }
}
