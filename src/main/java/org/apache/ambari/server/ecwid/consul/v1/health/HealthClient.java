package org.apache.ambari.server.ecwid.consul.v1.health;

import org.apache.ambari.server.ecwid.consul.v1.QueryParams;
import org.apache.ambari.server.ecwid.consul.v1.Response;
import org.apache.ambari.server.ecwid.consul.v1.health.model.Check;

import java.util.List;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public interface HealthClient {

	public Response<List<Check>> getHealthChecksForNode(String nodeName, QueryParams queryParams);

	public Response<List<Check>> getHealthChecksForService(String serviceName, QueryParams queryParams);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.health.model.HealthService>> getHealthServices(String serviceName, boolean onlyPassing, QueryParams queryParams);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.health.model.HealthService>> getHealthServices(String serviceName, String tag, boolean onlyPassing, QueryParams queryParams);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.health.model.HealthService>> getHealthServices(String serviceName, boolean onlyPassing, QueryParams queryParams, String token);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.health.model.HealthService>> getHealthServices(String serviceName, String tag, boolean onlyPassing, QueryParams queryParams, String token);

	public Response<List<Check>> getHealthChecksState(QueryParams queryParams);

	public Response<List<Check>> getHealthChecksState(Check.CheckStatus checkStatus, QueryParams queryParams);
}
