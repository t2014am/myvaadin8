package com.tamim.myvaadin8.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HierarchicalEmployeeSpeciality implements Serializable {

//    @ManyToOne
//    @JoinColumn(name = "id", nullable = false)
//    private HierarchicalEmployee hierarchicalEmployee;

	private Long specialityId;
	private String title;

//	private List<HierarchicalEmployee> hierarchicalEmployees;
}
