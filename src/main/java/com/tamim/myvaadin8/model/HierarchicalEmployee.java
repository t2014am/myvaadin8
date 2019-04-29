package com.tamim.myvaadin8.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "employeeId")
//@EqualsAndHashCode(exclude = { "speciality", "subordinates" })
public class HierarchicalEmployee implements Serializable {
	private final Logger logger = LogManager.getLogger(this.getClass());

	private Long employeeId;
	private String firstName;
	private String lastName;
	private Long supervisorId;
	private Set<HierarchicalEmployeeSpeciality> specialities;
	private String gender;
	// @OneToOne(mappedBy = "hierarchical_employee_position")
	private HierarchicalEmployeePostion position;
	private List<HierarchicalEmployee> subordinates;

	public HierarchicalEmployee(HierarchicalEmployee e) {
		employeeId = e.getEmployeeId();
		firstName = e.getFirstName();
		lastName = e.getLastName();
		supervisorId = e.getSupervisorId();
		specialities = e.getSpecialities();
		gender = e.getGender();
		position = e.getPosition();
		subordinates = e.getSubordinates();
	}

	public String getSpecialityString() {
		Set<HierarchicalEmployeeSpeciality> s = getSpecialities();
		StringBuilder sb = new StringBuilder();

		for (HierarchicalEmployeeSpeciality hierarchicalEmployeeSpeciality : s) {
			sb.append(hierarchicalEmployeeSpeciality.getTitle() + ", ");
		}
		if (sb.length() > 0) {
			// delete the last comma!
			sb.deleteCharAt(sb.length() - 2);
		}
		if (s.size() >= 2) {
			//find the last comma(,) and replace it with an (and)
			StringBuilder newSB = new StringBuilder(sb.reverse().toString().replaceFirst(",", "dna "));
//			String str = sb.toString().replaceFirst(",", " and");
			return newSB.reverse().toString();
		}

		return sb.toString();
	}

}