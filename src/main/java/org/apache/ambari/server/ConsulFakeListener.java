package org.apache.ambari.server;

import java.util.Date;

import com.ecwid.consul.transport.TLSConfig;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;


public class ConsulFakeListener {

    private static final String CONSUL_ADDRESS = "localhost";
    private static final Integer CONSUL_PORT = 8500;

    public void renameServiceEvent() {
        registerServiceIntoConsul(null, "ip-10-0-120-236.eu-west-1.compute.internal", null);
    }

    private void registerServiceIntoConsul(String serviceName, String hostName, Integer port) {
        try {
            System.out.println("registerServiceIntoConsul: Connecting to Consul because new event arrived.");
            ConsulClient consulClient = createClient(CONSUL_ADDRESS, CONSUL_PORT);
            System.out.println("Successfully connected to Consul.");
            NewService newService = new NewService();
            if (serviceName != null) {
                newService.setName(serviceName + ".service.consul");
            } else {
                newService.setName(((int) new Date().getTime() % 65000) + ".service.consul");
            }
            newService.setAddress(hostName);
            newService.setPort(port == null ? (int) new Date().getTime() % 65000 : port);
            System.out.println("Register new service to Consul: " +  newService);
            consulClient.agentServiceRegister(newService);
            System.out.println("Successfully registered ne service to Consul.");
        } catch (Exception ex) {
            System.out.println("///////////////////////////////////");
            System.out.println("//FAILED TO REGISTER NEW SERVICE///");
            System.out.println("///////////////////////////////////");
            System.out.println(ex.getMessage());
        }
    }

    public static ConsulClient createClient(String host, TLSConfig tlsConfig) {
        return new ConsulClient(host, tlsConfig);
    }

    public static ConsulClient createClient(String apiAddress, int apiPort) {
        System.out.println("Creating Consul client.");
        return new ConsulClient(apiAddress, apiPort);
    }

}
