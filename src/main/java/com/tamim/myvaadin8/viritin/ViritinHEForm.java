package com.tamim.myvaadin8.viritin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.tamim.myvaadin8.heirarchy.BuildHierarchyFromDb;
import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.tamim.myvaadin8.model.HierarchicalEmployeePostion;
import com.tamim.myvaadin8.model.HierarchicalEmployeeSpeciality;
import com.tamim.myvaadin8.service.HierarchicalEmployeeService;
import com.tamim.myvaadin8.windows.ComponentsForHE;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class ViritinHEForm extends AbstractForm<HierarchicalEmployee> {

	private static final long serialVersionUID = 1L;

	private final Logger logger = LogManager.getLogger(this.getClass());

//	EventBus.UIEventBus eventBus;
	HierarchicalEmployeeService repo = new HierarchicalEmployeeService();

	TextField firstName = new MTextField("First Name");
	TextField lastName = new MTextField("Last Name");
//	TextField email = new MTextField("Email");
//	TextField phoneNumber = new MTextField("Phone");
//	DateField birthDay = new DateField("Birthday");
//	Switch colleague = new Switch("Colleague");
	ComboBox<HierarchicalEmployeePostion> position = new ComboBox<>();
	ComboBox<HierarchicalEmployee> supervisor = new ComboBox<>();
	CheckBoxGroup<HierarchicalEmployeeSpeciality> specialities = new CheckBoxGroup<>();
	RadioButtonGroup<String> gender = new RadioButtonGroup<>();

	public ViritinHEForm() {
		super(HierarchicalEmployee.class);
//		this.eventBus = b;
		someInPageStyling();
		ComponentsForHE componentsForHE = new ComponentsForHE();
		BuildHierarchyFromDb b = new BuildHierarchyFromDb();

		position = componentsForHE.comboBoxPosition();

		supervisor = componentsForHE.comboBoxSupervisor(b.getTheFlatList(), new HierarchicalEmployee());
		specialities = componentsForHE.checkBoxGroupSpecialitites();
		gender = componentsForHE.radioButtonGroupGender();

		// On save & cancel, publish events that other parts of the UI can listen
		setSavedHandler(person -> {
			// persist changes
//			repo.save(person);
			logger.warn("save: {}", person);
			// send the event for other parts of the application
//			eventBus.publish(this, new PersonModifiedEvent(person));
		});
//		setResetHandler(p -> eventBus.publish(this, new PersonModifiedEvent(p)));

		setDescription("description");
		addStyleName("ViritinHEForm");

		setSizeUndefined();

	}

	@Override
	protected void bind() {
		// DateField in Vaadin 8 uses LocalDate by default, the backend
		// uses plain old java.util.Date, thus we need a converter, using
		// built in helper here
//		getBinder().forMemberField(birthDay).withConverter(new LocalDateToDateConverter());
		super.bind();
	}

	@Override
	protected Component createContent() {
		return new MVerticalLayout(
				new MFormLayout(firstName, lastName, specialities, gender, position, supervisor).withWidth(""),
				getToolbar()).withWidth("");
//		return new MVerticalLayout(new MFormLayout(name, email, phoneNumber, birthDay, colleague).withWidth(""),
//				getToolbar()).withWidth("");
	}

	private void someInPageStyling() {
		String myThemeName = "." + UI.getCurrent().getTheme();
		Styles styles = Page.getCurrent().getStyles();
		StringBuilder cssStrBuilder = new StringBuilder();
		cssStrBuilder.append(myThemeName + " .ViritinHEForm .v-margin-top {padding-top: 0;}");

//		cssStrBuilder.append(".mainWindow {top: 20px !important;}");

		styles.add(cssStrBuilder.toString());
	}

}
