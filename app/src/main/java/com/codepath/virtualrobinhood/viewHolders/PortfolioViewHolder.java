package com.codepath.virtualrobinhood.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Trade;

import java.text.DecimalFormat;

/**
 * Created by GANESH on 10/13/17.
 */

public class PortfolioViewHolder  extends RecyclerView.ViewHolder {

    public TextView tvSymbolPortfolio;
    public TextView tvPricePortfolio;
    DecimalFormat df = new DecimalFormat("##.##");

    public PortfolioViewHolder(View itemView) {
        super(itemView);

        tvSymbolPortfolio = itemView.findViewById(R.id.tvSymbolPortfolio);
        tvPricePortfolio = itemView.findViewById(R.id.tvPricePortfolio);
    }

    public void bindToPost(Trade trade) {
        tvSymbolPortfolio.setText(trade.symbol);
        tvPricePortfolio.setText(df.format(trade.price));
    }
}

