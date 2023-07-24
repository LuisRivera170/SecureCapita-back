package com.lara.securecapita.repository;

import com.lara.securecapita.domain.Role;

import java.util.Collection;

public interface RoleRepository<T extends Role> {

    /* CRUD operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More complex operations */
    void addRoleToUser(Long userid, String roleName);
    Role getRoleByUserId(Long userId);
    Role getRoleByUserEmail(String email);
    void updateUserRole(Long userId, String roleName);

}
