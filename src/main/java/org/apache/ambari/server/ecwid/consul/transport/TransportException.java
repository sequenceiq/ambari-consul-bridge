package org.apache.ambari.server.ecwid.consul.transport;

import org.apache.ambari.server.ecwid.consul.ConsulException;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public class TransportException extends ConsulException {

	public TransportException(Throwable cause) {
		super(cause);
	}

}
