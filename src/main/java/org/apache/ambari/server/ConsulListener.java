package org.apache.ambari.server;

import java.util.Date;

import org.apache.ambari.server.events.ServiceComponentInstalledEvent;
import org.apache.ambari.server.events.publishers.AmbariEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
@EagerSingleton
public class ConsulListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConsulListener.class);
    private static final String CONSUL_ADDRESS = "localhost";
    private static final Integer CONSUL_PORT = 8500;

    @Inject
    public ConsulListener(AmbariEventPublisher ambariEventPublisher) {
        LOG.info("Initialize ConsulListener for");
        ambariEventPublisher.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onServiceComponentEvent(ServiceComponentInstalledEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }
        LOG.info("registerServiceIntoConsul -> ServiceComponentInstalledEvent: ", event.toString());
        registerServiceIntoConsul(event.getServiceName().toLowerCase(), event.getHostName(), null);
    }

    private void registerServiceIntoConsul(String serviceName, String hostName, Integer port) {
        try {
            LOG.info("registerServiceIntoConsul -> Connecting to Consul because new event arrived.");
            ConsulClient consulClient = createClient(CONSUL_ADDRESS, CONSUL_PORT);
            LOG.info("Successfully connected to Consul.");
            NewService newService = new NewService();
            if (serviceName != null) {
                newService.setName(serviceName + ".service.consul");
            } else {
                newService.setName(((int) new Date().getTime() % 65000) + ".service.consul");
            }
            newService.setAddress(hostName);
            newService.setPort(port == null ? (int) new Date().getTime() % 65000 : port);
            LOG.info("Register new service to Consul: ", newService);
            consulClient.agentServiceRegister(newService);
            LOG.info("Successfully registered ne service to Consul.");
        } catch (Throwable ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public static ConsulClient createClient(String apiAddress, int apiPort) {
        LOG.info("Creating Consul client.");
        return new ConsulClient(apiAddress, apiPort);
    }

}
