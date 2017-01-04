package org.apache.ambari.server;

import java.util.Date;
import java.util.Map;

import org.apache.ambari.server.events.ServiceComponentInstalledEvent;
import org.apache.ambari.server.events.ServiceComponentUninstalledEvent;
import org.apache.ambari.server.events.publishers.AmbariEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;
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
    public void onServiceComponentInstalledEvent(ServiceComponentInstalledEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }
        LOG.info("registerServiceIntoConsul -> ServiceComponentInstalledEvent: ", event.toString());

        registerServiceIntoConsul(event.getComponentName().toLowerCase(), event.getHostName());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onServiceComponentUnInstalledEvent(ServiceComponentUninstalledEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }
        LOG.info("deregisterServiceIntoConsul -> ServiceComponentUninstalledEvent: ", event.toString());

        deregisterServiceIntoConsul(event.getComponentName().toLowerCase(), event.getHostName());
    }

    private void registerServiceIntoConsul(String componentName, String hostName) {
        try {
            LOG.info("registerServiceIntoConsul -> Connecting to Consul because new event arrived.");
            ConsulClient consulClient = createClient(CONSUL_ADDRESS, CONSUL_PORT);
            LOG.info("Successfully connected to Consul.");
            NewService newService = new NewService();
            if (componentName != null) {
                newService.setName(componentName(componentName, hostName));
            } else {
                newService.setName(((int) new Date().getTime() % 65000) + "");
            }
            newService.setAddress(hostName);
            newService.setPort(443);
            LOG.info("Register new service to Consul: ", newService);
            consulClient.agentServiceRegister(newService);
            LOG.info("Successfully registered new service to Consul.");
        } catch (Throwable ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private void deregisterServiceIntoConsul(String componentName, String hostName) {
        try {
            LOG.info("registerServiceIntoConsul -> Connecting to Consul because new event arrived.");
            ConsulClient consulClient = createClient(CONSUL_ADDRESS, CONSUL_PORT);
            LOG.info("Successfully connected to Consul.");
            Response<Map<String, Service>> agentServices = consulClient.getAgentServices();
            for (Map.Entry<String, Service> stringServiceEntry : agentServices.getValue().entrySet()) {
                Service value = stringServiceEntry.getValue();
                String validComponentName = componentName(componentName, hostName);
                if (value.getAddress().equals(hostName) && value.getService().equals(validComponentName)) {
                    LOG.info("Deregistered service with id: ", value.getId());
                    consulClient.agentServiceDeregister(value.getId());
                }
            }
            LOG.info("Deregistered is currently not implemented.");
        } catch (Throwable ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public String componentName(String componentName, String hostname) {
        return componentName.toLowerCase().replaceAll("_", "-") + "." + hostname.split("\\.")[0].toLowerCase().replaceAll("_", "-");
    }

    public static ConsulClient createClient(String apiAddress, int apiPort) {
        LOG.info("Creating Consul client.");
        return new ConsulClient(apiAddress, apiPort);
    }

}
