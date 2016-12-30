package org.apache.ambari.server;

import java.util.Date;

import org.apache.ambari.server.ecwid.consul.transport.TLSConfig;
import org.apache.ambari.server.ecwid.consul.v1.ConsulClient;
import org.apache.ambari.server.ecwid.consul.v1.agent.model.NewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsulFakeListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConsulFakeListener.class);
    private static final String CONSUL_ADDRESS = "localhost";
    private static final Integer CONSUL_PORT = 8500;

    public void renameServiceEvent() {
        registerServiceIntoConsul(null, "ip-10-0-120-236.eu-west-1.compute.internal", null);
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
        } catch (Exception ex) {
            LOG.info("///////////////////////////////////");
            LOG.info("//FAILED TO REGISTER NEW SERVICE///");
            LOG.info("///////////////////////////////////");
            LOG.info(ex.getMessage());
        }
    }

    public static ConsulClient createClient(String host, TLSConfig tlsConfig) {
        return new ConsulClient(host, tlsConfig);
    }

    public static ConsulClient createClient(String apiAddress, int apiPort) {
        LOG.info("Creating Consul client.");
        return new ConsulClient(apiAddress, apiPort);
    }

}
