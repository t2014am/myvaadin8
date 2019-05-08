package com.tamim.myvaadin8.viritin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.tamim.myvaadin8.heirarchy.BuildHierarchyFromDb;
import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.tamim.myvaadin8.service.HierarchicalEmployeeService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

public class ViritinCrudView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	private final Logger logger = LogManager.getLogger(this.getClass());

	final int PAGESIZE = 45;
	HierarchicalEmployeeService repo = new HierarchicalEmployeeService();
	ViritinHEForm viritinHEForm = new ViritinHEForm();
//	EventBus.UIEventBus eventBus;
	private MGrid<HierarchicalEmployee> mGridEmployees = new MGrid<>(HierarchicalEmployee.class)
			.withProperties("firstName", "lastName", "supervisorId", "gender")
			.withColumnHeaders("First Name", "Last Name", "Supervisor Id", "Gender")
			// not yet supported by V8
			// .setSortableProperties("name", "email")

			.withFullWidth();
	private MTextField filterByName = new MTextField().withPlaceholder("Filter by first or last name");
	private Button addNew = new MButton(VaadinIcons.PLUS, this::add);
	private Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
	private Button delete = new ConfirmButton(VaadinIcons.TRASH, "Are you sure you want to delete the entry?",
			this::remove);

	private List<HierarchicalEmployee> listOfAllEmployees = null;

	/**
	 * This is used to check if enter was called view a refresh or via a browser
	 * back and again clicking. If refresh is called, it is gonna be empty. If the
	 * back is clicked and then you return back to this view, it will not be empty.
	 */
	private String checkIfEnterWasAlreadyCalled = "";

	public ViritinCrudView() {
		logger.info("CrudView called! ");
//		this.eventBus = b;
	}

	@PostConstruct
	void init() {
		setSizeFull();
		setMargin(false);
		filterByName.setWidth("300px");
		addComponent(new MVerticalLayout(new MHorizontalLayout(filterByName, addNew, edit, delete), mGridEmployees)
				.expand(mGridEmployees));

		listEntities();
		mGridEmployees.setSizeFull();
		mGridEmployees.addColumn(e -> e.getPosition().getTitle()).setCaption("Postion");
		mGridEmployees.addColumn(HierarchicalEmployee::getSpecialityString).setCaption("Specialities");

		mGridEmployees.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
		filterByName.addValueChangeListener(e -> {
			listEntities(e.getValue());
		});

		// Listen to change events emitted by PersonForm see onEvent method
//		eventBus.subscribe(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
////      DisclosurePanel aboutBox = new DisclosurePanel("Spring Boot JPA CRUD example with Vaadin UI", new RichText().withMarkDownResource("/welcome.md"));
////		setContent(new MVerticalLayout(new MHorizontalLayout(filterByName, addNew, edit, delete), list).expand(list));
		if (("").equals(checkIfEnterWasAlreadyCalled)) {
			checkIfEnterWasAlreadyCalled = "Second call";

			init();
		}
	}

	protected void adjustActionButtonState() {
		boolean hasSelection = !mGridEmployees.getSelectedItems().isEmpty();
		edit.setEnabled(hasSelection);
		delete.setEnabled(hasSelection);
	}

	private void listEntities() {
		listEntities(filterByName.getValue());
	}

	private List<HierarchicalEmployee> filter(List<HierarchicalEmployee> allEmployees, String nameFilter) {
//		filtering data in memory instead of getting filtered list from db! 
//		may require updating the in memory list when creating, updating and deleting! 
		List<HierarchicalEmployee> filteredList = new ArrayList<>();
		allEmployees.forEach(item -> {
			if (nameFilter != "") {
				if (item.getFirstName().toLowerCase().contains(nameFilter)
						|| item.getLastName().toLowerCase().contains(nameFilter)) {
					filteredList.add(item);
				}
			} else {
				filteredList.add(item);
			}
		});

		return filteredList;
	}

	private void listEntities(String nameFilter) {
		// But we want to support filtering, first add the % marks for SQL name query
//		String likeFilter = "%" + nameFilter + "%";
//		list.setRows(repo.findByNameLikeIgnoreCase(likeFilter));

		if (listOfAllEmployees == null) {
			BuildHierarchyFromDb b = new BuildHierarchyFromDb();
			listOfAllEmployees = new ArrayList<>(b.getEmployeesFlatWithSupervisors());
			mGridEmployees.setRows(listOfAllEmployees);
		} else {
			mGridEmployees.setRows(filter(listOfAllEmployees, nameFilter));
		}

		// Lazy binding for better optimized connection from the Vaadin Table to
		// Spring Repository. This approach uses less memory and database
		// resources. Use this approach if you expect you'll have lots of data
		// in your table. There are simpler APIs if you don't need sorting.
		// list.setDataProvider(
		// // entity fetching strategy
		// (sortOrder, offset, limit) -> {
		// final List<Person> page = repo.findByNameLikeIgnoreCase(likeFilter,
		// new PageRequest(
		// offset / limit,
		// limit,
		// sortOrder.isEmpty() || sortOrder.get(0).getDirection() ==
		// SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC,
		// // fall back to id as "natural order"
		// sortOrder.isEmpty() ? "id" : sortOrder.get(0).getSorted()
		// )
		// );
		// return page.subList(offset % limit, page.size()).stream();
		// },
		// // count fetching strategy
		// () -> (int) repo.countByNameLike(likeFilter)
		// );
		adjustActionButtonState();

	}

	public void add(ClickEvent clickEvent) {
		edit(new HierarchicalEmployee());
	}

	public void edit(ClickEvent e) {
		edit(mGridEmployees.asSingleSelect().getValue());
	}

	public void remove() {
//		repo.delete(list.asSingleSelect().getValue());
		logger.warn(mGridEmployees.asSingleSelect().getValue().getEmployeeId() + " "
				+ mGridEmployees.asSingleSelect().getValue().getFirstName());
		mGridEmployees.deselectAll();
		listEntities();
	}

	protected void edit(final HierarchicalEmployee phoneBookEntry) {
		viritinHEForm.setEntity(phoneBookEntry);
		viritinHEForm.openInModalPopup();
	}

//	@EventBusListenerMethod(scope = EventScope.UI)
//	public void onPersonModified(PersonModifiedEvent event) {
//		listEntities();
//		personForm.closePopup();
//	}
}
