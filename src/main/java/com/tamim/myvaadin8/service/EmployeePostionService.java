package com.tamim.myvaadin8.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.tamim.myvaadin8.model.HierarchicalEmployeePostion;

public class EmployeePostionService implements ServiceInterface<HierarchicalEmployeePostion> {

	@Override
	public Iterable<HierarchicalEmployeePostion> findAll() {

		return getAllDummy();
	}

	@Override
	public Optional<HierarchicalEmployeePostion> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HierarchicalEmployeePostion save(HierarchicalEmployeePostion e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	private Iterable<HierarchicalEmployeePostion> getAllDummy() {
		Set<HierarchicalEmployeePostion> employeePostions = new HashSet<>();
		employeePostions.add(new HierarchicalEmployeePostion(1L, "Fullstack developer"));
		employeePostions.add(new HierarchicalEmployeePostion(2L, "Backend developer"));
		employeePostions.add(new HierarchicalEmployeePostion(3L, "Frontend developer"));

		return employeePostions;

	}

}
