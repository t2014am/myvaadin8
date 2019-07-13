package com.tamim.myvaadin8.windows;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tamim.myvaadin8.heirarchy.BuildHierarchyFromDb;
import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.tamim.myvaadin8.model.HierarchicalEmployeePostion;
import com.tamim.myvaadin8.model.HierarchicalEmployeeSpeciality;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.HtmlRenderer;

@SuppressWarnings("serial")
public class WindowsAndModalsView extends VerticalLayout implements View {
	private static Logger logger = LoggerFactory.getLogger(WindowsAndModalsView.class);

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

	/**
	 * This is used to check if enter was called view a refresh or via a browser
	 * back and again clicking. If refresh is called, it is gonna be empty. If the
	 * back is clicked and then you return back to this view, it will not be empty.
	 */
	private String checkIfEnterWasAlreadyCalled = "";

	@Override
	public void enter(ViewChangeEvent event) {
		logger.info("enter called!");
		View.super.enter(event);
		setSizeFull();

		if (("").equals(checkIfEnterWasAlreadyCalled)) {
			checkIfEnterWasAlreadyCalled = "Second call";

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
	}

	private TreeGrid<HierarchicalEmployee> theTreeGrid() {
		theTreeGrid = new TreeGrid<>();
		theTreeGrid.setSizeFull();
		BuildHierarchyFromDb b = new BuildHierarchyFromDb();
		hierarchicalEmployeesFlat = b.getEmployeesFlatWithSupervisors();
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
		ComponentsForHE componentsForHE = new ComponentsForHE();
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

		CheckBoxGroup<HierarchicalEmployeeSpeciality> cbgs = componentsForHE.checkBoxGroupSpecialitites();
		subContent.addComponent(cbgs);
		binder.forField(cbgs).bind(HierarchicalEmployee::getSpecialities, HierarchicalEmployee::setSpecialities);

		RadioButtonGroup<String> radioButtonGroupGender = componentsForHE.radioButtonGroupGender();
		subContent.addComponent(radioButtonGroupGender);
		binder.bind(radioButtonGroupGender, HierarchicalEmployee::getGender, HierarchicalEmployee::setGender);

		ComboBox<HierarchicalEmployeePostion> comboBoxPosition = componentsForHE.comboBoxPosition();
		subContent.addComponent(comboBoxPosition);
		binder.bind(comboBoxPosition, HierarchicalEmployee::getPosition, HierarchicalEmployee::setPosition);

//		comboBoxSupervisor = comboBoxSupervisor();

		ComboBox<HierarchicalEmployee> comboBoxSupervisor = componentsForHE
				.comboBoxSupervisor(hierarchicalEmployeesFlat, hierarchicalEmployee);
		binder.bind(comboBoxSupervisor, HierarchicalEmployee::getSupervisor, HierarchicalEmployee::setSupervisor);
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

}
