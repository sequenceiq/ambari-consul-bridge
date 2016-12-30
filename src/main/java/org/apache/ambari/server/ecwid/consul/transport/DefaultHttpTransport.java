package org.apache.ambari.server.ecwid.consul.transport;

import org.apache.http.client.HttpClient;

public final class DefaultHttpTransport extends AbstractHttpTransport {

    public DefaultHttpTransport() {
        super();
    }

    public DefaultHttpTransport(HttpClient httpClient) {
        super(httpClient);
    }

}
