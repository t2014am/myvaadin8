package com.tamim.myvaadin8.service;

import java.io.Serializable;
import java.util.Optional;

public interface ServiceInterface<T> extends Serializable {

	Iterable<T> findAll();

	Optional<T> findById(Long id);

	T save(T e);

	void deleteById(Long id);

	long count();
}
