package org.apache.ambari.server.ecwid.consul.v1.coordinate;

import java.util.List;

import org.apache.ambari.server.ecwid.consul.v1.QueryParams;
import org.apache.ambari.server.ecwid.consul.v1.Response;
import org.apache.ambari.server.ecwid.consul.v1.coordinate.model.Datacenter;
import org.apache.ambari.server.ecwid.consul.v1.coordinate.model.Node;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public interface CoordinateClient {

	 Response<List<Datacenter>> getDatacenters();

	 Response<List<Node>> getNodes(QueryParams queryParams);

}
