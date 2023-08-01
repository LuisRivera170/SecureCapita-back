package com.lara.securecapita.query;

public class RoleQuery {

    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM roles WHERE name = :name";
    public static final String SELECT_ROLE_BY_USER_ID_QUERY = "SELECT * FROM roles WHERE id = (SELECT role_id FROM user_roles WHERE user_id = :userId)";

}
