package com.betadevels.onlineshopping.db;

import com.google.common.base.Optional;
import com.betadevels.onlineshopping.models.BaseModel;

import java.util.List;
import java.util.Map;

public interface GenericDAO<T extends BaseModel>
{
    T create(T obj);

    Optional<T> findById(Long id);

    List<T> findByParams(Map<String, Object> params);

    T update(T obj);

    void delete(T obj);

    Long count();
}
