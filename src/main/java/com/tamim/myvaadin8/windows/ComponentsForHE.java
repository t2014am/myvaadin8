package com.tamim.myvaadin8.windows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.tamim.myvaadin8.model.HierarchicalEmployeePostion;
import com.tamim.myvaadin8.model.HierarchicalEmployeeSpeciality;
import com.tamim.myvaadin8.service.EmployeePostionService;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.RadioButtonGroup;

public class ComponentsForHE {
	public CheckBoxGroup<HierarchicalEmployeeSpeciality> checkBoxGroupSpecialitites() {
		CheckBoxGroup<HierarchicalEmployeeSpeciality> checkBoxGroup = new CheckBoxGroup<>("Speciality");
		checkBoxGroup.setDescription("CheckBox Group");
		HierarchicalEmployeeSpeciality hSpeciality1 = new HierarchicalEmployeeSpeciality(1l, "Java");
		HierarchicalEmployeeSpeciality hSpeciality2 = new HierarchicalEmployeeSpeciality(2l, "C#");
		HierarchicalEmployeeSpeciality hSpeciality3 = new HierarchicalEmployeeSpeciality(3l, "Javascript");
		HierarchicalEmployeeSpeciality hSpeciality4 = new HierarchicalEmployeeSpeciality(4l, "PHP");

		List<HierarchicalEmployeeSpeciality> hSpecialities = new ArrayList<>();
		hSpecialities.add(hSpeciality1);
		hSpecialities.add(hSpeciality2);
		hSpecialities.add(hSpeciality3);
		hSpecialities.add(hSpeciality4);

		checkBoxGroup.setItems(hSpecialities);
		checkBoxGroup.setItemCaptionGenerator(HierarchicalEmployeeSpeciality::getTitle);
//		checkBoxGroup.addValueChangeListener(l -> {
//			Notification.show("Caption: radioButtonGroup", "Description: " + l.getValue().toString(),
//					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
//		});

//		binder.bind(checkBoxGroup, HierarchicalEmployee::getSpecialities, HierarchicalEmployee::setSpecialities);

//		binder.forField(checkBoxGroup).bind(HierarchicalEmployee::getSpecialities, HierarchicalEmployee::setSpecialities)

		return checkBoxGroup;
	}

	public RadioButtonGroup<String> radioButtonGroupGender() {
		RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Gender");
		radioButtonGroup.setDescription("Radio Button Group");
		radioButtonGroup.setItems("Male", "Female", "Other");
		radioButtonGroup.addStyleNames("radioButtonGroup");
		// radioButtonGroup.addValueChangeListener(l -> {
		// Notification.show("Caption: radioButtonGroup", "Description: " +
		// l.getValue().toString(),
		// Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
		// });

		// binder.bind(radioButtonGroup, HierarchicalEmployee::getGender,
		// HierarchicalEmployee::setGender);

		return radioButtonGroup;
	}

	public ComboBox<HierarchicalEmployeePostion> comboBoxPosition() {
		// Create a selection component with some items
		ComboBox<HierarchicalEmployeePostion> comboBox = new ComboBox<>("Position");
		comboBox.setDescription("ComboBox");
		comboBox.setItemCaptionGenerator(HierarchicalEmployeePostion::getTitle);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setEmptySelectionCaption("Select...");
		EmployeePostionService employeePostionService = new EmployeePostionService();
		comboBox.setItems(employeePostionService.findAllAsSet());

		// Handle selection event
//		comboBox.addSelectionListener(l -> {
//			Notification.show("Caption: comboBox", "Description: " + l.getValue().toString(),
//					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
//		});

//		binder.bind(comboBox, HierarchicalEmployee::getPosition, HierarchicalEmployee::setPosition);

		return comboBox;
	}

	public ComboBox<HierarchicalEmployee> comboBoxSupervisor(Set<HierarchicalEmployee> flatListOfEmployees,
			HierarchicalEmployee selectedHE) {
		// Create a selection component with some items
		ComboBox<HierarchicalEmployee> comboBox = new ComboBox<>("Supervisor");
		comboBox.setDescription("ComboBox");
		comboBox.setItemCaptionGenerator(HierarchicalEmployee::getFullName);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setEmptySelectionCaption("Select supervisor...");

		Set<HierarchicalEmployee> temp = new HashSet<>(flatListOfEmployees);
		temp.remove(selectedHE);
		comboBox.setItems(temp);

		// Handle selection event
//		comboBox.addSelectionListener(l -> {
//			Notification.show("Caption: comboBox", "Description: " + l.getValue().toString(),
//					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
//		});

//		DO NOT FORGET TO BIND THE RETURNED COMBOBOX
//		binder.bind(comboBox, HierarchicalEmployee::getSupervisor, HierarchicalEmployee::setSupervisor);

		return comboBox;
	}
}
