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

    private TextView tvSymbol;
    private TextView tvCompanyName;
    private TextView tvPrice;
    private TextView tvQuantity;

    DecimalFormat df = new DecimalFormat("##.##");

    public PortfolioViewHolder(View itemView) {
        super(itemView);

        tvSymbol = itemView.findViewById(R.id.tvSymbol);
        tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvQuantity = itemView.findViewById(R.id.tvQuantity);
    }

    public void bindToPost(Trade trade) {
        tvSymbol.setText(trade.symbol);
        tvCompanyName.setText(trade.companyName);
        tvPrice.setText("$" + df.format(trade.price));
        tvQuantity.setText(String.valueOf(trade.quantity));
    }
}

