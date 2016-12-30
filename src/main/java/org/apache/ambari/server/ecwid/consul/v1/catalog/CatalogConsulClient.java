package org.apache.ambari.server.ecwid.consul.v1.catalog;

import java.util.List;
import java.util.Map;

import org.apache.ambari.server.ecwid.consul.SingleUrlParameters;
import org.apache.ambari.server.ecwid.consul.UrlParameters;
import org.apache.ambari.server.ecwid.consul.json.GsonFactory;
import org.apache.ambari.server.ecwid.consul.transport.RawResponse;
import org.apache.ambari.server.ecwid.consul.transport.TLSConfig;
import org.apache.ambari.server.ecwid.consul.v1.ConsulRawClient;
import org.apache.ambari.server.ecwid.consul.v1.OperationException;
import org.apache.ambari.server.ecwid.consul.v1.QueryParams;
import org.apache.ambari.server.ecwid.consul.v1.Response;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogDeregistration;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogNode;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogRegistration;
import org.apache.ambari.server.ecwid.consul.v1.catalog.model.Node;
import com.google.gson.reflect.TypeToken;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public final class CatalogConsulClient implements CatalogClient {

	private final ConsulRawClient rawClient;

	public CatalogConsulClient(ConsulRawClient rawClient) {
		this.rawClient = rawClient;
	}

	public CatalogConsulClient() {
		this(new ConsulRawClient());
	}

	public CatalogConsulClient(TLSConfig tlsConfig) {
		this(new ConsulRawClient(tlsConfig));
	}

	public CatalogConsulClient(String agentHost) {
		this(new ConsulRawClient(agentHost));
	}

	public CatalogConsulClient(String agentHost, TLSConfig tlsConfig) {
		this(new ConsulRawClient(agentHost, tlsConfig));
	}

	public CatalogConsulClient(String agentHost, int agentPort) {
		this(new ConsulRawClient(agentHost, agentPort));
	}

	public CatalogConsulClient(String agentHost, int agentPort, TLSConfig tlsConfig) {
		this(new ConsulRawClient(agentHost, agentPort, tlsConfig));
	}

	@Override
	public Response<Void> catalogRegister(CatalogRegistration catalogRegistration) {
		String json = GsonFactory.getGson().toJson(catalogRegistration);

		RawResponse rawResponse = rawClient.makePutRequest("/v1/catalog/register", json);
		if (rawResponse.getStatusCode() == 200) {
			return new Response<Void>(null, rawResponse);
		} else {
			throw new OperationException(rawResponse);
		}
	}

	@Override
	public Response<Void> catalogDeregister(CatalogDeregistration catalogDeregistration) {
		String json = GsonFactory.getGson().toJson(catalogDeregistration);

		RawResponse rawResponse = rawClient.makePutRequest("/v1/catalog/deregister", json);
		if (rawResponse.getStatusCode() == 200) {
			return new Response<Void>(null, rawResponse);
		} else {
			throw new OperationException(rawResponse);
		}
	}

	@Override
	public Response<List<String>> getCatalogDatacenters() {
		RawResponse rawResponse = rawClient.makeGetRequest("/v1/catalog/datacenters");

		if (rawResponse.getStatusCode() == 200) {
			List<String> value = GsonFactory.getGson().fromJson(rawResponse.getContent(), new TypeToken<List<String>>() {
			}.getType());
			return new Response<List<String>>(value, rawResponse);
		} else {
			throw new OperationException(rawResponse);
		}
	}

	@Override
	public Response<List<Node>> getCatalogNodes(QueryParams queryParams) {
		RawResponse rawResponse = rawClient.makeGetRequest("/v1/catalog/nodes", queryParams);

		if (rawResponse.getStatusCode() == 200) {
			List<Node> value = GsonFactory.getGson().fromJson(rawResponse.getContent(), new TypeToken<List<Node>>() {
			}.getType());
			return new Response<List<Node>>(value, rawResponse);
		} else {
			throw new OperationException(rawResponse);
		}
	}

	@Override
	public Response<Map<String, List<String>>> getCatalogServices(QueryParams queryParams) {
		return getCatalogServices(queryParams, null);
	}

	@Override
	public Response<Map<String, List<String>>> getCatalogServices(QueryParams queryParams, String token) {
		UrlParameters tokenParam = token != null ? new SingleUrlParameters("token", token) : null;
		RawResponse rawResponse = rawClient.makeGetRequest("/v1/catalog/services", queryParams, tokenParam);

		if (rawResponse.getStatusCode() == 200) {
			Map<String, List<String>> value = GsonFactory.getGson().fromJson(rawResponse.getContent(),
					new TypeToken<Map<String, List<String>>>() {
					}.getType());
			return new Response<Map<String, List<String>>>(value, rawResponse);
		} else {
			throw new OperationException(rawResponse);
		}
	}

	@Override
	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, QueryParams queryParams) {
		return getCatalogService(serviceName, null, queryParams, null);
	}

	@Override
	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, QueryParams queryParams, String token) {
		return getCatalogService(serviceName, null, queryParams, token);
	}

	@Override
	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, String tag,
																							  QueryParams queryParams) {
		return getCatalogService(serviceName, tag, queryParams, null);
	}

	@Override
	public Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>> getCatalogService(String serviceName, String tag,
																							  QueryParams queryParams, String token) {
		UrlParameters tokenParam = token != null ? new SingleUrlParameters("token", token) : null;
		UrlParameters tagParam = tag != null ? new SingleUrlParameters("tag", tag) : null;
		RawResponse rawResponse = rawClient.makeGetRequest("/v1/catalog/service/" + serviceName, tagParam, queryParams, tokenParam);

		if (rawResponse.getStatusCode() == 200) {
			List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService> value = GsonFactory.getGson().fromJson(rawResponse.getContent(),
					new TypeToken<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>>() {
					}.getType());
			return new Response<List<org.apache.ambari.server.ecwid.consul.v1.catalog.model.CatalogService>>(value, rawResponse);
		} else {
			throw new OperationException(rawResponse);
		}
	}

	@Override
	public Response<CatalogNode> getCatalogNode(String nodeName, QueryParams queryParams) {
		RawResponse rawResponse = rawClient.makeGetRequest("/v1/catalog/node/" + nodeName, queryParams);

		if (rawResponse.getStatusCode() == 200) {
			CatalogNode catalogNode = GsonFactory.getGson().fromJson(rawResponse.getContent(), CatalogNode.class);
			return new Response<CatalogNode>(catalogNode, rawResponse);
		} else {
			throw new OperationException(rawResponse);
		}
	}

}
