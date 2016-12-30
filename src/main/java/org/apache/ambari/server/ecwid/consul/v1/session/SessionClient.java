package org.apache.ambari.server.ecwid.consul.v1.session;

import org.apache.ambari.server.ecwid.consul.v1.QueryParams;
import org.apache.ambari.server.ecwid.consul.v1.Response;
import org.apache.ambari.server.ecwid.consul.v1.session.model.NewSession;
import org.apache.ambari.server.ecwid.consul.v1.session.model.Session;

import java.util.List;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public interface SessionClient {

	public Response<String> sessionCreate(NewSession newSession, QueryParams queryParams);

	public Response<Void> sessionDestroy(String session, QueryParams queryParams);

	Response<Session> getSessionInfo(String session, QueryParams queryParams);

	Response<List<Session>> getSessionNode(String node, QueryParams queryParams);

	Response<List<Session>> getSessionList(QueryParams queryParams);

	Response<Session> renewSession(String session, QueryParams queryParams);
}
