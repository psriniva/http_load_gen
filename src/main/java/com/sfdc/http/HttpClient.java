package com.sfdc.http;

import com.ning.http.client.Cookie;
import com.sfdc.http.client.handler.ThrottlingGenericAsyncHandler;
import com.sfdc.stats.StatsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * @author psrinivasan
 *         Date: 11/20/12
 *         Time: 10:50 AM
 *
 *         One instance of this class per user.  We use that model to account for usage patterns
 *         that use a finite state machine per user, or otherwise want to persist client state.
 *
 *         This class is the HttpClient (and the only HttpClient) that should be used to create a
 *         workload.  All the details of the async processing (ie., the queues and the actual
 *         http client) are hidden from the end user.
 *
 */
public class HttpClient {
    /* Absolute maximum connections on a Linux machine
     * given the port range.
    */
    public static final int MAX_CONNECTIONS_PER_HOST = 65536;
    public static final Semaphore MAX_CONNECTIONS_PERMIT = new Semaphore(MAX_CONNECTIONS_PER_HOST);

    /*
     * These internal state variables can be set by the caller.
     */

    private ArrayList<Cookie> cookies;
    private HashMap<String, String> requestHeaders;
    private HashMap<String, String> parameters;

    public void start() {

    }

    public void startGet(String url,
                         HashMap<String, String> headers,
                         HashMap<String, String> parameters,
                         ArrayList<Cookie> cookies,
                         ThrottlingGenericAsyncHandler throttlingGenericAsyncHandler) {
    }

    public void startGet(String url,
                         HashMap<String, String> headers,
                         HashMap<String, String> parameters,
                         ArrayList<Cookie> cookies) {
        startGet(url, headers, parameters, cookies, new ThrottlingGenericAsyncHandler(MAX_CONNECTIONS_PERMIT, StatsManager.getInstance()));
    }

    public void startGet(String url) {
        startGet(url, this.requestHeaders, this.parameters, this.cookies);
    }




}
