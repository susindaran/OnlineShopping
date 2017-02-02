package com.utdallas.onlineshopping.db;

import com.google.common.base.Optional;
import com.utdallas.onlineshopping.models.BaseModel;

import java.util.List;
import java.util.Map;

public interface GenericDAO<T extends BaseModel>
{
    public T create(T obj);

    public Optional<T> findById(Long id);

    public List<T> findByParams(Map<String, Object> params);

    public T update(T obj);

    public void delete(T obj);

    public long count();

    public T merge(T obj);
}
