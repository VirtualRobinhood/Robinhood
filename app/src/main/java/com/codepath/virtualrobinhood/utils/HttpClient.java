package com.codepath.virtualrobinhood.utils;

import okhttp3.OkHttpClient;

/**
 * Created by SagarMutha on 10/11/17.
 */

public class HttpClient {

    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }
}
