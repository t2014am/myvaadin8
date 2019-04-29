package com.tamim.myvaadin8.grid_renderers_collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ModelForCheckbox {

	private Long id;
	private String name;
	private Boolean visible;
}
