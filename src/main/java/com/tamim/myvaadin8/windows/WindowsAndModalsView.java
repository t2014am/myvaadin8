package com.tamim.myvaadin8.windows;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class WindowsAndModalsView extends VerticalLayout implements View {
	private final Logger logger = LogManager.getLogger(this.getClass());
	private Window w = w();

	private Set<String> positions = new HashSet<>();

	@Override
	public void enter(ViewChangeEvent event) {
		logger.info("enter called!");
		View.super.enter(event);

		someInPageStyling();

		this.getViewComponent().getUI().addWindow(w);
	}

	public Window w() {
		positions = getPositions();
		// Create a sub-window and set the content
		Window mainWindow = new Window("Employee Details");
		mainWindow.setDescription("Window");
		mainWindow.addStyleNames("mainWindow");
//		mainWindow.setWidth("400px");
//		mainWindow.setHeight("500px");
		mainWindow.setWindowMode(WindowMode.NORMAL);

		Button save = new Button("Save it!");
		save.setDescription("Button");
		save.addClickListener(l -> {
			this.getViewComponent().getUI().addWindow(confirmationWindow());
		});

		// Put some components in it
		VerticalLayout subContent = new VerticalLayout();
		subContent.addComponent(new Label("Please fill-in your information."));
		subContent.addComponent(textFields());
		subContent.addComponent(checkBoxGroup());
		subContent.addComponent(radioButtonGroup());
		subContent.addComponent(comboBox());
		subContent.addComponent(save);
		subContent.setComponentAlignment(save, Alignment.BOTTOM_RIGHT);

		// Center it in the browser window
		mainWindow.center();

		// Open it in the UI
//        addWindow(subWindow);

		mainWindow.setContent(subContent);
		return mainWindow;
	}

	private VerticalLayout textFields() {
		TextField firstName = new TextField("Name: ");
		firstName.setPlaceholder("Write your name here");

		TextField lastName = new TextField("Last Name: ");
		lastName.setPlaceholder("Write your lastname here");
		VerticalLayout textFields = new VerticalLayout(firstName, lastName);
		textFields.setDescription("VerticalLayout with TextFields");
		textFields.addStyleName("textFields");
		textFields.setMargin(false);
		return textFields;
	}

	private ComboBox<String> comboBox() {
		// Create a selection component with some items
		ComboBox<String> comboBox = new ComboBox<>("Position");
		comboBox.setDescription("ComboBox");
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setEmptySelectionCaption("Select...");
		logger.warn(positions.toString());
		comboBox.setItems(positions);

		// Handle selection event
		comboBox.addSelectionListener(l -> {
			Notification.show("Caption: comboBox", "Description: " + l.getValue().toString(),
					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
		});
		return comboBox;
	}

	private RadioButtonGroup<String> radioButtonGroup() {
		RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Gender");
		radioButtonGroup.setDescription("Radio Button Group");
		radioButtonGroup.setItems("Male", "Female", "Other");
		radioButtonGroup.addStyleNames("radioButtonGroup");
		radioButtonGroup.addValueChangeListener(l -> {
			Notification.show("Caption: radioButtonGroup", "Description: " + l.getValue().toString(),
					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
		});
		return radioButtonGroup;
	}

	private CheckBoxGroup<String> checkBoxGroup() {
		CheckBoxGroup<String> checkBoxGroup = new CheckBoxGroup<>("Speciality");
		checkBoxGroup.setDescription("CheckBox Group");
		checkBoxGroup.setItems("Java", "JavaScript", "C#", "PHP");
		checkBoxGroup.addValueChangeListener(l -> {
			Notification.show("Caption: radioButtonGroup", "Description: " + l.getValue().toString(),
					Notification.Type.TRAY_NOTIFICATION).setDelayMsec(2000);
		});
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

	private Set<String> getPositions() {
		Set<String> positions = new HashSet<>();

		positions.add("Frontend developer");
		positions.add("Backend developer");
		positions.add("Fullstack developer");

		return positions;
	}

}
