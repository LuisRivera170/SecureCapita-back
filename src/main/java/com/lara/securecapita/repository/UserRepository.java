package com.lara.securecapita.repository;

import com.lara.securecapita.domain.User;

import java.util.Collection;

public interface UserRepository<T extends User> {

    /* CRUD operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More complex operations */

}
