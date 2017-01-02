package org.apache.ambari.server;

import org.apache.ambari.server.events.ServiceComponentInstalledEvent;
import org.apache.ambari.server.events.ServiceComponentUninstalledEvent;
import org.apache.ambari.server.events.publishers.AmbariEventPublisher;
import org.junit.Ignore;
import org.junit.Test;

public class ConsulListenerTest {

    ConsulListener consulListener = new ConsulListener(new AmbariEventPublisher());

    @Ignore
    @Test
    public void testServiceDeregister() {
        ServiceComponentUninstalledEvent serviceComponentInstalledEvent =
                new ServiceComponentUninstalledEvent(1l, "ricsitest", "2.5", "HDFS_CLIENT123", "HDFS", "alma123-123-123.alma.com", false);

        consulListener.onServiceComponentUnInstalledEvent(serviceComponentInstalledEvent);
    }

    @Ignore
    @Test
    public void testServiceRegister() {
        ServiceComponentInstalledEvent serviceComponentInstalledEvent =
                new ServiceComponentInstalledEvent(1l, "ricsitest", "2.5", "HDFS_CLIENT123", "HDFS", "alma123-123-123.alma.com", false);

        consulListener.onServiceComponentInstalledEvent(serviceComponentInstalledEvent);
    }
}