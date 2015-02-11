package org.sllx.site.user.service;


import org.sllx.site.core.base.BaseService;
import org.sllx.site.user.entity.User;

import javax.ws.rs.Path;

@Path("/")
public interface UserService extends BaseService<User> {
}
