package com.tamim.myvaadin8.heirarchy;

import java.io.Serializable;
import java.util.List;

public class EmployeeNode implements Serializable {
	private int empId;
	private String name;
	private int reportToId;
	private List<EmployeeNode> subordinates;

	public EmployeeNode(String id, String empName, String rid) {
		try {
			this.empId = Integer.parseInt(id);
			this.name = empName;
			this.reportToId = Integer.parseInt(rid);
		} catch (Exception e) {
			System.err.println("Exception creating employee:" + e);
		}
	}

	public List<EmployeeNode> getSubordinates() {
		return subordinates;
	}

	void setSubordinates(List<EmployeeNode> subordinates) {
		this.subordinates = subordinates;
	}

	public int getId() {
		return empId;
	}

	void setId(int id) {
		this.empId = id;
	}

	public String getName() {
		return name;
	}

	void setName(String n) {
		name = n;
	}

	int getReportToId() {
		return reportToId;
	}

	@Override
	public String toString() {
		return "EmployeeNode{" + "empId=" + empId + ", name='" + name + '\'' + ", reportToId=" + reportToId
				+ ", subordinates=" + subordinates + '}';
	}
}
