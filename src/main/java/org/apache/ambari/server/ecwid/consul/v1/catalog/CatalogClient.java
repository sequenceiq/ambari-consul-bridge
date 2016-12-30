package org.apache.ambari.server.ecwid.consul.v1.catalog;

import org.apache.ambari.server.ecwid.consul.v1.QueryParams;
import org.apache.ambari.server.ecwid.consul.v1.Response;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogDeregistration;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogNode;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogRegistration;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.Node;

import java.util.List;
import java.util.Map;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public interface CatalogClient {

	public Response<Void> catalogRegister(CatalogRegistration catalogRegistration);

	public Response<Void> catalogDeregister(CatalogDeregistration catalogDeregistration);

	public Response<List<String>> getCatalogDatacenters();

	public Response<List<Node>> getCatalogNodes(QueryParams queryParams);

	public Response<Map<String, List<String>>> getCatalogServices(QueryParams queryParams);

	public Response<Map<String, List<String>>> getCatalogServices(QueryParams queryParams, String token);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, QueryParams queryParams);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, String tag, QueryParams queryParams);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, QueryParams queryParams, String token);

	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, String tag, QueryParams queryParams, String token);

	public Response<CatalogNode> getCatalogNode(String nodeName, QueryParams queryParams);
}
