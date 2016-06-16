package com.rainyalley.architecture.core.notification;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * DefaultNotificationManager Tester.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class DefaultNotificationManagerTest {

    private NotificationManager notificationManager = new DefaultNotificationManager();
    private InternalObserver internalObserver = new InternalObserver();
    private ExtendInternalObserver extendInternalObserver = new ExtendInternalObserver();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: subscribe(Class<T> subjectType, Observer<T> internalObserver)
     *
     */
    @Test
    public void testSubscribe() throws Exception {
        notificationManager.subscribe(InternalEvent.class, internalObserver);
        notificationManager.subscribe(ExtendInternalEvent.class, extendInternalObserver);
    }

    /**
     *
     * Method: notify(Event event, Object subject)
     *
     */
    @Test
    public void testNotify() throws Exception {
        testSubscribe();
        notificationManager.notify(ExtendInternalEvent.TEST);


        Assert.assertNotNull(extendInternalObserver.event);
    }

    private static class InternalObserver implements Observer<InternalEvent>{

        private Event event;

        @Override
        public void focus(InternalEvent event) {
            this.event = event;
        }
    }

    private static class ExtendInternalObserver implements Observer<ExtendInternalEvent>{

        private Event event;

        @Override
        public void focus(ExtendInternalEvent event) {
            this.event = event;
        }

    }

    private static class InternalEvent implements Event<String>{


        private String context;

        private InternalEvent(String context) {
            this.context = context;
        }

        @Override
        public String context() {
            return context;
        }
    }

    private static class ExtendInternalEvent extends InternalEvent{
        private static final ExtendInternalEvent TEST = new ExtendInternalEvent("test");

        public ExtendInternalEvent(String context) {
            super(context);
        }
    }

} 
