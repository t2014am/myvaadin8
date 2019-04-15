package com.tamim.myvaadin8.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HierarchicalEmployeePostion implements Serializable {
	private Long id;
	private String title;

}
