INSERT INTO sllxsite.user (id, name, password, enabled) VALUES (2, 'success', '123456', 1);

INSERT INTO sllxsite.role (id, name, description) VALUES (1, 'admin', 'administrator');

INSERT INTO sllxsite.web_resource (id, pattern, sequence) VALUES (1, '/user/**', 0);

INSERT INTO sllxsite.security_metadata (id, webResource_id, role_id) VALUES (1, 1, 1);

INSERT INTO sllxsite.user_role (id, user_id, role_id) VALUES (1, 2, 1);
