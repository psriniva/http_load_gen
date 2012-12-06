package com.sfdc.http;

import com.sfdc.http.client.handler.ResponseHandler;
import junit.framework.TestCase;

/**
 * @author psrinivasan
 *         Date: 12/5/12
 *         Time: 12:25 AM
 */
public class HttpClientTest extends TestCase {
    private HttpClient httpClient;
    private static final String STATIC_URL = "http://www.gnu.org/";

    public void setUp() throws Exception {
        httpClient = new HttpClient();
    }

    public void tearDown() throws Exception {
        httpClient.getHttpLoadGenerator().stop();
    }

    public void testStartGet() throws Exception {
        httpClient.startGet(STATIC_URL);
        Thread.sleep(5000);

    }

    public void testStartGetWithHandler() throws InterruptedException {
        ResponseHandler myResponseHandler = new ResponseHandler() {
            @Override
            public void onCompleted(int statusCode, String statusText, String responseBody, String contentType) {
                System.out.println("Status Code " + statusCode);
                System.out.println("Status Text " + statusText);
                assertEquals(200, statusCode);
                assertEquals("OK", statusText);
            }
        };
        httpClient.startGet(STATIC_URL, myResponseHandler);
        Thread.sleep(5000);
    }
}
