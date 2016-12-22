package org.apache.ambari;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.ambari.server.EagerSingleton;
import org.apache.ambari.server.events.ServiceEvent;
import org.apache.ambari.server.events.publishers.AmbariEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
@EagerSingleton
public class ConsulListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConsulListener.class);

    @Inject
    public ConsulListener(AmbariEventPublisher ambariEventPublisher) {
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        ambariEventPublisher.register(this);
    }


    @Subscribe
    public void onServiceComponentEvent(ServiceEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());

        }

        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        try(FileWriter fw = new FileWriter("/tmp/outfilename", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("the text");
            //more code
            out.println("more text");
            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

    @Subscribe
    public void onServiceEvent(ServiceEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }

        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        LOG.info("Consul test................................");
        try(FileWriter fw = new FileWriter("/tmp/outfilename", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("the text");
            //more code
            out.println("more text");
            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
