package com.codepath.virtualrobinhood.viewHolders;

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
    public TextView tvSymbol;
    public TextView tvPrice;
    public ImageView ivRemoveSymbol;

    public StockViewHolder(View itemView) {
        super(itemView);

        tvSymbol = itemView.findViewById(R.id.tvSymbol);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        ivRemoveSymbol = itemView.findViewById(R.id.ivRemoveSymbol);
    }

    public void bindToPost(Stock stock) {
        tvSymbol.setText(stock.symbol);
        tvPrice.setText(String.valueOf(stock.quotations.get(0).close));
    }
}
