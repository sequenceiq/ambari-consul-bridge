package org.apache.ambari.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.ambari.server.events.AmbariEvent;
import org.apache.ambari.server.events.ClusterEvent;
import org.apache.ambari.server.events.ServiceComponentInstalledEvent;
import org.apache.ambari.server.events.ServiceInstalledEvent;
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
        LOG.info("///////////////////////////////////");
        LOG.info("/////INITIALIZE CONSULLISTENER/////");
        LOG.info("///////////////////////////////////");
        ambariEventPublisher.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onServiceComponentEvent(ServiceComponentInstalledEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }
        LOG.info("registerServiceIntoConsul: onServiceComponentEvent.");
        registerServiceIntoConsul(event.getServiceName().toLowerCase(), event.getHostName(), null);

        try(FileWriter fw = new FileWriter("/tmp/consulevents", true);
            BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(event.toString());
        } catch (Throwable ex) {
            LOG.info("///////////////////////////////////");
            LOG.info("//FAILED TO REGISTER NEW SERVICE///");
            LOG.info("///////////////////////////////////");
            LOG.error(ex.getMessage(), ex);
        }

    }

    @Subscribe
    @AllowConcurrentEvents
    public void onServiceEvent(ServiceInstalledEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }

        LOG.info("registerServiceIntoConsul: registerServiceIntoConsul.");
        registerServiceIntoConsul(event.getServiceName().toLowerCase(), "127.0.0.1", null);

        try(FileWriter fw = new FileWriter("/tmp/consulevents", true);
            BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(event.toString());
        } catch (Throwable ex) {
            LOG.info("///////////////////////////////////");
            LOG.info("//FAILED TO REGISTER NEW SERVICE///");
            LOG.info("///////////////////////////////////");
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void renameServiceEvent(ClusterEvent event) {
        if (event.getType().equals(AmbariEvent.AmbariEventType.CLUSTER_RENAME)) {
            registerServiceIntoConsul(null, "ip-10-0-120-236.eu-west-1.compute.internal", null);
        } else {
            LOG.info("Not a rename event: " + event.toString());
        }
    }

    private void registerServiceIntoConsul(String serviceName, String hostName, Integer port) {
        try {
            LOG.info("registerServiceIntoConsul: Connecting to Consul because new event arrived.");
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
            LOG.info("///////////////////////////////////");
            LOG.info("//FAILED TO REGISTER NEW SERVICE///");
            LOG.info("///////////////////////////////////");
            LOG.error(ex.getMessage(), ex);
        }
    }

    public static ConsulClient createClient(String apiAddress, int apiPort) {
        LOG.info("Creating Consul client.");
        return new ConsulClient(apiAddress, apiPort);
    }

}
