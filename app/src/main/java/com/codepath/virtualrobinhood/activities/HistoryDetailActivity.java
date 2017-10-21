package com.codepath.virtualrobinhood.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.virtualrobinhood.R;

public class HistoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        /*
        Intent intent = getIntent();
        final History history = Parcels.unwrap(intent.getParcelableExtra("history"));
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final TextView tvTypeValue = findViewById(R.id.tvTypeValue);
        final TextView tvSubmittedDate = findViewById(R.id.tvSubmittedDateValue);

        tvTypeValue.setText(history.buySell);
        tvSubmittedDate.setText(history.date);
        */
    }
}
