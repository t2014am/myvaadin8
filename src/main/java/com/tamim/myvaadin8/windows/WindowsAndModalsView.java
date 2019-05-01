package com.tamim.myvaadin8.windows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tamim.myvaadin8.heirarchy.BuildHierarchyFromDb;
import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.tamim.myvaadin8.model.HierarchicalEmployeePostion;
import com.tamim.myvaadin8.model.HierarchicalEmployeeSpeciality;
import com.tamim.myvaadin8.service.EmployeePostionService;
import com.tamim.myvaadin8.service.HierarchicalEmployeeService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.HtmlRenderer;

@SuppressWarnings("serial")
public class WindowsAndModalsView extends VerticalLayout implements View {
	private final Logger logger = LogManager.getLogger(this.getClass());
	private Window w;

	HierarchicalEmployeeService hierarchicalEmployeeService;
	private Set<HierarchicalEmployeePostion> positions;
	private Set<HierarchicalEmployeeSpeciality> specialities;

	private HierarchicalEmployee hierarchicalEmployee;

	private Set<HierarchicalEmployee> rootItems;
	private Set<HierarchicalEmployee> hierarchicalEmployeesFlat;
	private Integer maxDepth = 0;

	private Binder<HierarchicalEmployee> binder = new Binder<>();

	TextField firstName = new TextField("Name: ");

	TreeGrid<HierarchicalEmployee> theTreeGrid;
	ComboBox<HierarchicalEmployee> comboBoxSupervisor;

	@Override
	public void enter(ViewChangeEvent event) {
		logger.info("enter called!");
		View.super.enter(event);
		setSizeFull();

		hierarchicalEmployeeService = new HierarchicalEmployeeService();

		hierarchicalEmployee = new HierarchicalEmployee();
//		binder.setBean(hierarchicalEmployee);

		someInPageStyling();

		Button newItem = new Button("Add new");
		newItem.addClickListener(l -> {
			HierarchicalEmployee hierarchicalEmployee = new HierarchicalEmployee();
//			binder.setBean(hierarchicalEmployee);
			w = addOrEditItem(null);
			this.getViewComponent().getUI().addWindow(w);
			logger.warn("Add new btn clicked! ");
		});
		TreeGrid<HierarchicalEmployee> theTreeGrid = theTreeGrid();
		addComponent(newItem);
		addComponent(theTreeGrid);
		setExpandRatio(theTreeGrid, 1F);

		binder.addStatusChangeListener(l -> {
//			logger.warn(l.getBinder().hasChanges() + " " + l.getBinder().getBean());
//			logger.warn(hierarchicalEmployee.equals(l.getBinder().getBean()));
//			logger.warn(l.getBinder().getBean());
//			logger.warn(hierarchicalEmployee);

			if (binder.hasChanges()) {
//				logger.warn(binder.hasChanges() + " ");
//				logger.warn(binder.getBean().getLastName() + " ");
			}

		});
	}

	private TreeGrid<HierarchicalEmployee> theTreeGrid() {
		theTreeGrid = new TreeGrid<>();
		theTreeGrid.setSizeFull();
		BuildHierarchyFromDb b = new BuildHierarchyFromDb();
		hierarchicalEmployeesFlat = b.getEmployeesFlat();
		rootItems = b.getRootItems();
		theTreeGrid.setItems(rootItems, HierarchicalEmployee::getSubordinates);
		theTreeGrid.addColumn(c -> {
			String iconHtml;
			if (c.getGender().toLowerCase().equals("male")) {
				iconHtml = VaadinIcons.MALE.getHtml();
			} else if (c.getGender().toLowerCase().equals("female")) {
				iconHtml = VaadinIcons.FEMALE.getHtml();
			} else {
				iconHtml = VaadinIcons.CLIPBOARD_USER.getHtml();
			}

//			return iconHtml + " " + Jsoup.clean(file.getName(), Whitelist.simpleText());
			return iconHtml + " " + c.getFirstName();
		}, new HtmlRenderer()).setCaption("Firstname").setId("firstName");
		theTreeGrid.addColumn(HierarchicalEmployee::getLastName).setCaption("Lastname");
		theTreeGrid.addColumn(e -> e.getPosition().getTitle()).setCaption("Position");
		theTreeGrid.addColumn(e -> e.getSpecialityString()).setCaption("Speciality");
		theTreeGrid.sort("firstName", SortDirection.DESCENDING);
		theTreeGrid.expandRecursively(rootItems, 2);

		theTreeGrid.addSelectionListener(l -> {
			if (l.getFirstSelectedItem().isPresent()) {
//				logger.warn(l.getFirstSelectedItem().get().toString());
				hierarchicalEmployee = l.getFirstSelectedItem().get();
				w = addOrEditItem(hierarchicalEmployee);
				this.getViewComponent().getUI().addWindow(w);
			}
		});

		return theTreeGrid;
	}

	public Window addOrEditItem(HierarchicalEmployee hEmployee) {

		if (hEmployee != null) {
//			logger.warn(hEmployee.toString());
			logger.warn("SelectedItem: {}", hEmployee.getFirstName() + " " + hEmployee.getGender());
		} else {
			logger.warn(" adding mode! ");
			if (hierarchicalEmployee != null) {
				theTreeGrid.deselect(hierarchicalEmployee);
			}
			hierarchicalEmployee = new HierarchicalEmployee();
		}
		EmployeePostionService employeePostionService = new EmployeePostionService();
		positions = new HashSet<>();
		employeePostionService.findAll().forEach(e -> {
			positions.add(e);
		});
		// Create a sub-window and set the content
		Window mainWindow = new Window("Employee Details");
		mainWindow.setDescription("Window");
		mainWindow.addStyleNames("mainWindow");
//		mainWindow.setWidth("400px");
//		mainWindow.setHeight("500px");
		mainWindow.setWindowMode(WindowMode.NORMAL);
		mainWindow.setModal(true);

		Button save = new Button("Save it!");
		save.setDescription("Button");

		save.addClickListener(l -> {
			logger.info("save clicked! ");
//			logger.info(binder.getBean().getFirstName());
			if (binder.hasChanges()) {
//				binderWriteBean(hierarchicalEmployeeBeforeConfirm);
//				logger.warn("hierarchicalEmployee " + hierarchicalEmployee.getFirstName() + " ");
//				logger.warn("hierarchicalEmployee " + hierarchicalEmployee.getEmployeeId() + " ");
//				logger.warn("hierarchicalEmployeeBeforeConfirm " + hierarchicalEmployeeBeforeConfirm.getFirstName() + " ");
//				logger.warn("hierarchicalEmployeeBeforeConfirm " + hierarchicalEmployeeBeforeConfirm.getEmployeeId() + " ");
				this.getViewComponent().getUI().addWindow(confirmationWindow());
			} else {
				mainWindow.close();
			}

//			Notification.show("Item saved! ", hierarchicalEmployee.toString(), Type.TRAY_NOTIFICATION);
		});

		// Put some components in it
		VerticalLayout subContent = new VerticalLayout();
		subContent.addComponent(new Label("Please fill-in your information."));
		subContent.addComponent(textFields());
		subContent.addComponent(checkBoxGroup());
		subContent.addComponent(radioButtonGroup());
		subContent.addComponent(comboBox());

		comboBoxSupervisor = comboBoxSupervisor();

		subContent.addComponent(comboBoxSupervisor);
		subContent.addComponent(save);
		subContent.setComponentAlignment(save, Alignment.BOTTOM_RIGHT);

		binder.readBean(hierarchicalEmployee);

		// Center it in the browser window
		mainWindow.center();

		// Open it in the UI
//        addWindow(subWindow);

		mainWindow.setContent(subContent);

		return mainWindow;
	}

	private void binderWriteBean(HierarchicalEmployee e) {
		try {
			binder.writeBean(e);
		} catch (ValidationException e1) {
			logger.error("BEANWRITER ERROR: {}", e1);
		}
	}

	private VerticalLayout textFields() {
		firstName = new TextField("Name: ");
		binder.bind(firstName, HierarchicalEmployee::getFirstName, HierarchicalEmployee::setFirstName);
		firstName.setPlaceholder("Write your name here");

		TextField lastName = new TextField("Last Name: ");
		lastName.setPlaceholder("Write your lastname here");
		binder.bind(lastName, HierarchicalEmployee::getLastName, HierarchicalEmployee::setLastName);

		VerticalLayout textFields = new VerticalLayout(firstName, lastName);
		textFields.setDescription("VerticalLayout with TextFields");
		textFields.addStyleName("textFields");
		textFields.setMargin(false);

		return textFields;
	}

	private ComboBox<HierarchicalEmployeePostion> comboBox() {
		// Create a selection component with some items
		ComboBox<HierarchicalEmployeePostion> comboBox = new ComboBox<>("Position");
		comboBox.setDescription("ComboBox");
		comboBox.setItemCaptionGenerator(HierarchicalEmployeePostion::getTitle);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setEmptySelectionCaption("Select...");
		comboBox.setItems(positions);

		// Handle selection event
//		comboBox.addSelectionListener(l -> {
//			Notification.show("Caption: comboBox", "Description: " + l.getValue().toString(),
//					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
//		});

		binder.bind(comboBox, HierarchicalEmployee::getPosition, HierarchicalEmployee::setPosition);

		return comboBox;
	}

	private ComboBox<HierarchicalEmployee> comboBoxSupervisor() {
		// Create a selection component with some items
		ComboBox<HierarchicalEmployee> comboBox = new ComboBox<>("Position");
		comboBox.setDescription("Supervisor");
		comboBox.setItemCaptionGenerator(HierarchicalEmployee::getFullName);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setEmptySelectionCaption("Select supervisor...");

		Set<HierarchicalEmployee> temp = new HashSet<>(hierarchicalEmployeesFlat);
		temp.remove(hierarchicalEmployee);
		comboBox.setItems(temp);

		// Handle selection event
//		comboBox.addSelectionListener(l -> {
//			Notification.show("Caption: comboBox", "Description: " + l.getValue().toString(),
//					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
//		});

		binder.bind(comboBox, HierarchicalEmployee::getSupervisor, HierarchicalEmployee::setSupervisor);

		return comboBox;
	}

	private RadioButtonGroup<String> radioButtonGroup() {
		RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Gender");
		radioButtonGroup.setDescription("Radio Button Group");
		radioButtonGroup.setItems("Male", "Female", "Other");
		radioButtonGroup.addStyleNames("radioButtonGroup");
//		radioButtonGroup.addValueChangeListener(l -> {
//			Notification.show("Caption: radioButtonGroup", "Description: " + l.getValue().toString(),
//					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
//		});

		binder.bind(radioButtonGroup, HierarchicalEmployee::getGender, HierarchicalEmployee::setGender);

		return radioButtonGroup;
	}

	private CheckBoxGroup<HierarchicalEmployeeSpeciality> checkBoxGroup() {
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

		binder.bind(checkBoxGroup, HierarchicalEmployee::getSpecialities, HierarchicalEmployee::setSpecialities);

//		binder.forField(checkBoxGroup).bind(HierarchicalEmployee::getSpecialities, HierarchicalEmployee::setSpecialities)

		return checkBoxGroup;
	}

	private Window confirmationWindow() {
		// Create a sub-window and set the content
		Window subWindow = new Window("Confirmation");
		subWindow.setModal(true);
		subWindow.setWindowMode(WindowMode.NORMAL);
		VerticalLayout subContent = new VerticalLayout();
		subWindow.setContent(subContent);

		Button yes = new Button("Awlright");
		yes.addClickListener(l -> {
			Notification.show("Confirmed!", "You've decided not close the window!", Notification.Type.TRAY_NOTIFICATION)
					.setDelayMsec(2000);

//			logger.warn(hierarchicalEmployee.toString());
			binderWriteBean(hierarchicalEmployee);
			logger.info(hierarchicalEmployee.toString());
//			logger.warn(hierarchicalEmployee.toString());

			theTreeGrid.deselect(hierarchicalEmployee);
			theTreeGrid.getDataProvider().refreshAll();
			subWindow.close();
			w.close();
		});
		Button no = new Button("Nope");
		no.addClickListener(l -> {
			Notification
					.show("Cancelled", "You've decided not to close the window!", Notification.Type.TRAY_NOTIFICATION)
					.setDelayMsec(2000);
			subWindow.close();
		});
		HorizontalLayout buttons = new HorizontalLayout(yes, no);

		// Put some components in it
		subContent.addComponent(new Label("Are you sure u want to close the window? "));
		subContent.addComponent(buttons);
		subContent.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

		// Center it in the browser window
		subWindow.center();

		// Open it in the UI
		return subWindow;
	}

	private void someInPageStyling() {
		String myThemeName = "." + UI.getCurrent().getTheme();
		Styles styles = Page.getCurrent().getStyles();
		StringBuilder cssStrBuilder = new StringBuilder();
		cssStrBuilder.append(" .textFields .v-caption {display: inline-block; padding-top: 8px; min-width: 120px;}");
		cssStrBuilder.append(" .textFields input {min-width: 250px;}");
		cssStrBuilder.append(
				myThemeName + " .v-slot-radioButtonGroup .v-select-optiongroup .v-checkbox  {display: inline-block;}");
		cssStrBuilder.append(myThemeName
				+ " .v-slot-radioButtonGroup .v-select-optiongroup .v-radiobutton  {display: inline-block;}");

//		cssStrBuilder.append(".mainWindow {top: 20px !important;}");

		styles.add(cssStrBuilder.toString());
	}

//	private Set<String> getPositions() {
//		Set<String> positions = new HashSet<>();
//
//		positions.add("Frontend developer");
//		positions.add("Backend developer");
//		positions.add("Fullstack developer");
//
//		return positions;
//	}

}
