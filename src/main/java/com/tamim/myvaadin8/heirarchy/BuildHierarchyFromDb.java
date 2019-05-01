package com.tamim.myvaadin8.heirarchy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.tamim.myvaadin8.service.HierarchicalEmployeeService;

import lombok.Getter;

@Getter
public class BuildHierarchyFromDb {

	private Set<HierarchicalEmployee> employeesFlat;

	private Set<HierarchicalEmployee> rootItems;
	private Integer maxDepth = 0;

	public BuildHierarchyFromDb() {

		rootItems = new HashSet<>();
		employeesFlat = getTheFlatList();

		employeesFlat.forEach(e -> {
			if (e.getSupervisorId() == 0) {
				rootItems.add(e);
			}
		});

		for (HierarchicalEmployee e : rootItems) {
			buildHierarchyTree(1, e);
		}
	}

	public Set<HierarchicalEmployee> getTheFlatList() {
		HierarchicalEmployeeService hierarchicalEmployeeService = new HierarchicalEmployeeService();
		Set<HierarchicalEmployee> employeesFlat = new HashSet<>();
		hierarchicalEmployeeService.findAll().forEach(e -> {
			employeesFlat.add(e);
		});
		hierarchicalEmployeeService.findById(1L);
		return employeesFlat;
	}

	public List<HierarchicalEmployee> getSubordinatesById(HierarchicalEmployee emp) {
		List<HierarchicalEmployee> subordinates = new ArrayList<HierarchicalEmployee>();
		for (HierarchicalEmployee e : employeesFlat) {
			if (e.getSupervisorId() == emp.getEmployeeId()) {
				e.setSupervisor(emp);
				subordinates.add(e);
			}
		}
		return subordinates;
	}

	public void buildHierarchyTree(int depth, HierarchicalEmployee localRoot) {
		maxDepth = (maxDepth > depth) ? maxDepth : depth;

		List<HierarchicalEmployee> subordinates = getSubordinatesById(localRoot);
		localRoot.setSubordinates(subordinates);
		if (subordinates.size() == 0) {
			return;
		}

		for (HierarchicalEmployee e : subordinates) {
			buildHierarchyTree(depth + 1, e);
		}
	}

}
