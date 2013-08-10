package mta.cats.dao;

import java.util.List;

import mta.cats.model.Cat;

public interface IDAO<T> {
	T select(Long id);
	List<T> selectAll();
	int update(T model);
	T create(T model);
	int delete(T model);
}
