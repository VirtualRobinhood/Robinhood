package com.codepath.virtualrobinhood.viewHolders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.virtualrobinhood.R;
import com.codepath.virtualrobinhood.models.Stock;

/**
 * Created by gkurghin on 10/12/17.
 */

public class StockViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivRemoveSymbol;
    public TextView tvSymbol;
    public TextView tvPrice;
    public TextView tvPriceDifference;

    private Context context;

    public StockViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;

        tvSymbol = itemView.findViewById(R.id.tvSymbol);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvPriceDifference = itemView.findViewById(R.id.tvPriceDifference);
        ivRemoveSymbol = itemView.findViewById(R.id.ivRemoveSymbol);
    }

    public void bindToPost(Stock stock) {
        tvSymbol.setText(stock.symbol);
        if (stock.quotations.size() > 0) {
            float currentClose = stock.quotations.get(0).close;
            tvPrice.setText(String.valueOf(currentClose).toUpperCase());

            float prevClose = stock.quotations.size() > 1
                    ? stock.quotations.get(1).close
                    : stock.quotations.get(0).close;


            if (currentClose > prevClose) {
                tvPriceDifference.setText("+" + String.valueOf(currentClose - prevClose));
                tvPriceDifference.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            } else if (currentClose < prevClose) {
                tvPriceDifference.setText("-" + String.valueOf(currentClose - prevClose));
                tvPriceDifference.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            } else {
                tvPriceDifference.setText("0");
            }
        }
    }
}
