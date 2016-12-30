package org.apache.ambari.server.ecwid.consul.v1.event;

import org.apache.ambari.server.ecwid.consul.v1.QueryParams;
import org.apache.ambari.server.ecwid.consul.v1.Response;
import org.apache.ambari.server.ecwid.consul.v1.event.model.Event;
import org.apache.ambari.server.ecwid.consul.v1.event.model.EventParams;

import java.util.List;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public interface EventClient {

	public Response<Event> eventFire(String event, String payload, EventParams eventParams, QueryParams queryParams);

	public Response<List<Event>> eventList(QueryParams queryParams);

	public Response<List<Event>> eventList(String event, QueryParams queryParams);

}
