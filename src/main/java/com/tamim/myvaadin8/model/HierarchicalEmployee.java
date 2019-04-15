package com.tamim.myvaadin8.model;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(exclude = "speciality")
public class HierarchicalEmployee implements Serializable {
	private final Logger logger = LogManager.getLogger(this.getClass());

	private Long employeeId;
	private String firstName;
	private String lastName;
	private Long supervisorId;
	private List<HierarchicalEmployeeSpeciality> specialities;
	private String gender;
	// @OneToOne(mappedBy = "hierarchical_employee_position")
	private HierarchicalEmployeePostion position;
	private List<HierarchicalEmployee> subordinates;

	public String getSpecialityString() {
		List<HierarchicalEmployeeSpeciality> s = getSpecialities();
		StringBuilder sb = new StringBuilder();

		for (HierarchicalEmployeeSpeciality hierarchicalEmployeeSpeciality : s) {
			sb.append(hierarchicalEmployeeSpeciality.getTitle() + ", ");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 2);
		}
		if (s.size() >= 2) {
			String str = sb.toString().replaceFirst(",", " and");
			return str;
		}

		return sb.toString();
	}

}
