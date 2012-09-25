package com.sfdc.http.smc;

import com.sfdc.http.queue.Consumer;
import com.sfdc.http.queue.Producer;
import com.sfdc.http.queue.WorkItem;
import com.sfdc.http.util.SoapLoginUtil;
import junit.framework.TestCase;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

/**
 * @author psrinivasan
 *         Date: 9/20/12
 *         Time: 11:29 AM
 */
public class QueueingStreamingClientImplTest extends TestCase {
    private QueueingStreamingClientImpl streamingClient;
    private String sessionId;
    private String instance;
    private Producer producer;
    private Consumer consumer;
    private Thread consumerThread;

    public void setUp() throws Exception {
        String[] credentials = SoapLoginUtil.login("dpham@180.private.streaming.20.org8", "123456", "https://ist6.soma.salesforce.com/");
        sessionId = credentials[0];
        instance = credentials[1];
        LinkedBlockingDeque<WorkItem> queue = new LinkedBlockingDeque<WorkItem>();
        producer = new Producer(queue);
        Semaphore numConcurrentClients = new Semaphore(2);
        consumer = new Consumer(queue, numConcurrentClients);
        String[] channels = {"/topic/accountTopic", "/topic/c1Topic"};
        streamingClient = new QueueingStreamingClientImpl(sessionId, instance, producer, producer, channels);
        consumerThread = new Thread(consumer);
        consumerThread.start();

    }

    public void tearDown() throws Exception {
        //consumerThread.interrupt();

    }

    public void testStart() throws Exception {
        streamingClient.start();
        Thread.sleep(115000); // 110 seconds for connect to expire, and 5 seconds for everything else.
        if (streamingClient.getState() != "FSM.Done") {
            System.out.println("End state not reached yet, sleeping for 10 more seconds.");
            //sleep for a 10 more seconds hoping we'll be done.
            Thread.sleep(10000);
        }
        assertEquals("FSM.Done", streamingClient.getState());
    }
}