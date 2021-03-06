package com.tamim.myvaadin8.addons.grid_renderers_collection_and_jsclipboard;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ModelForCheckbox implements Serializable {

	private Long id;
	private String name;
	private Boolean visible;

	public ModelForCheckbox(ModelForCheckbox m) {
		this.id = m.getId();
		this.name = m.getName();
		this.visible = m.getVisible();
	}
}
