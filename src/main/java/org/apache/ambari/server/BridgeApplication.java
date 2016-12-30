package org.apache.ambari.server;

public class BridgeApplication {

    private BridgeApplication() {
    }

    public static void main(String[] args) {

        ConsulFakeListener consulFakeListener = new ConsulFakeListener();
        consulFakeListener.renameServiceEvent();
        System.out.println("Ambari Consul Bridge");
    }
}
