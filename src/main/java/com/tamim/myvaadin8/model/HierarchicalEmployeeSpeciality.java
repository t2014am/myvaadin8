package com.tamim.myvaadin8.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HierarchicalEmployeeSpeciality implements Serializable {

//    @ManyToOne
//    @JoinColumn(name = "id", nullable = false)
//    private HierarchicalEmployee hierarchicalEmployee;

	private Long specialityId;
	private String title;

	private List<HierarchicalEmployee> hierarchicalEmployees;
}
