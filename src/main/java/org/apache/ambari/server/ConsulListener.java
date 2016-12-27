package org.apache.ambari.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.ambari.server.events.ServiceComponentInstalledEvent;
import org.apache.ambari.server.events.ServiceInstalledEvent;
import org.apache.ambari.server.events.publishers.AmbariEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
@EagerSingleton
public class ConsulListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConsulListener.class);
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    private static final String CONSUL_ADDRESS = "192.168.59.103";
    private static final Integer CONSUL_PORT = 8500;

    @Inject
    public ConsulListener(AmbariEventPublisher ambariEventPublisher) {
        LOG.info("Initialize ConsulListener for");
        ambariEventPublisher.register(this);
    }

    @Subscribe
    public void onServiceComponentEvent(ServiceComponentInstalledEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }

        registerServiceIntoConsul(event.getServiceName().toLowerCase(), event.getHostName(), null);
        try(FileWriter fw = new FileWriter("/tmp/consulevents", true);
            BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(event.toString());
        } catch (IOException e) {
            LOG.info("///////////////////////////////////");
            LOG.info("//FAILED TO REGISTER NEW SERVICE///");
            LOG.info("///////////////////////////////////");
            LOG.info(e.getMessage());
        }

    }

    @Subscribe
    public void onServiceEvent(ServiceInstalledEvent event) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(event.toString());
        }

        registerServiceIntoConsul(event.getServiceName().toLowerCase(), null, null);

        try(FileWriter fw = new FileWriter("/tmp/consulevents", true);
            BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(event.toString());
        } catch (IOException e) {
            LOG.info("///////////////////////////////////");
            LOG.info("//FAILED TO REGISTER NEW SERVICE///");
            LOG.info("///////////////////////////////////");
            LOG.info(e.getMessage());
        }
    }

    private void registerServiceIntoConsul(String serviceName, String hostName, Integer port) {
        LOG.info("Connecting to Consul because new event arrived.");
        ConsulClient consulClient = createClient(CONSUL_ADDRESS, CONSUL_PORT, DEFAULT_TIMEOUT_MS);
        LOG.info("Successfully connected to Consul.");


        NewService newService = new NewService();
        newService.setName(serviceName + ".service.consul");
        newService.setAddress(hostName);
        newService.setPort(port == null ? (int) new Date().getTime() % 65000 : port);
        LOG.info("Register new service to Consul: ", newService);
        consulClient.agentServiceRegister(newService);
        LOG.info("Successfully registered ne service to Consul.");
    }

    public static ConsulClient createClient(HttpClientConfig httpClientConfig, int timeout) {
        return new ConsulClient("https://" + httpClientConfig.getApiAddress(), httpClientConfig.getApiPort(),
                httpClientConfig.getClientCert(),
                httpClientConfig.getClientKey(),
                httpClientConfig.getServerCert(),
                timeout);
    }

    public static ConsulClient createClient(String apiAddress, int apiPort, int timeout) {
        return new ConsulClient("http://" + apiAddress, apiPort, timeout);
    }

}
