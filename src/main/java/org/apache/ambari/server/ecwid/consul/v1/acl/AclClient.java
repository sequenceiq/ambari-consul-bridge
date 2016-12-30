package org.apache.ambari.server.ecwid.consul.v1.acl;

import org.apache.ambari.server.ecwid.consul.v1.Response;
import org.apache.ambari.server.ecwid.consul.v1.acl.model.Acl;
import org.apache.ambari.server.ecwid.consul.v1.acl.model.NewAcl;
import org.apache.ambari.server.ecwid.consul.v1.acl.model.UpdateAcl;

import java.util.List;

/**
 * @author Vasily Vasilkov (vgv@ecwid.com)
 */
public interface AclClient {

	Response<String> aclCreate(NewAcl newAcl, String token);

	Response<Void> aclUpdate(UpdateAcl updateAcl, String token);

	Response<Void> aclDestroy(String aclId, String token);

	Response<Acl> getAcl(String id);

	Response<String> aclClone(String aclId, String token);

	Response<List<Acl>> getAclList(String token);

}
