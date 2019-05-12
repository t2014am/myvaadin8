package com.tamim.myvaadin8.addons.grid_renderers_collection_and_jsclipboard;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.grid.cellrenderers.editable.BooleanSwitchRenderer;
import org.vaadin.grid.cellrenderers.editoraware.CheckboxRenderer;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class GridRenderersView extends VerticalLayout implements View {
	private final Logger logger = LogManager.getLogger(this.getClass());

	Set<ModelForCheckbox> items = getItemsFromDBDummy();
	Set<ModelForCheckbox> itemsHardCopy = hardCopy(items);
	private VaadinSession session;

	/**
	 * This is used to check if enter was called view a refresh or via a browser
	 * back and again clicking. If refresh is called, it is gonna be empty. If the
	 * back is clicked and then you return back to this view, it will not be empty.
	 */
	private String checkIfEnterWasAlreadyCalled = "";

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		if (("").equals(checkIfEnterWasAlreadyCalled)) {
			checkIfEnterWasAlreadyCalled = "Second call";
			logger.info("entered! ");
			session = getUI().getSession();
			session.setAttribute("items", items);
//		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("items", items);

			Button b = new Button("Open Grid Renderers");
			b.setDescription("Open checkbox grid renderer test!");
			b.addClickListener(l -> this.getViewComponent().getUI().addWindow(window()));

			addComponent(b);
			JSClipboardExample jsClipboardExample = new JSClipboardExample();
			addComponent(jsClipboardExample.clipboard());
		}
	}

	private Window window() {
		Window w = new Window("Please select");
		w.setWindowMode(WindowMode.NORMAL);
		w.setWidth("50%");

		Button save = new Button("Save");
		save.addClickListener(l -> {

			logger.warn("Save");
			logger.warn("Save");
			logger.warn("Save");
			logger.warn("Save");

//			logger.warn("itemsBeforeSave " + itemsBeforeSave.toString());
//			logger.warn("items           " + items.toString());
//			saveItemsToDB(items);
			items.clear();
			items = hardCopy(itemsHardCopy);
			session.setAttribute("items", items);
//			logger.warn(session.getAttribute("items"));

			w.close();
		});

		Button cancel = new Button("Cancel");
		cancel.addClickListener(l -> {
			logger.trace("Cancel");
//			items.clear();
//		    TODO: SO ITEMS.CLEAR() IS GOING TO CLEAR OUT THE DATA FROM SESSION TOO! 

//			logger.warn(items.toString());
//			logger.warn(itemsHardCopy.toString());
//			logger.warn(itemsHardCopy == items);
//			logger.warn(session.getAttribute("items"));

			w.close();
		});
		HorizontalLayout buttons = new HorizontalLayout(save, cancel);

		Label l = new Label(
				"Note: Without database connection it is impossible to keep track of the checked/unchecked items, since no matter how many itemLists you make, once u try to copy the values from one to another, the data binding takes over and binds both, updating the values instantanously!");

		l.setWidth("100%");

		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(theGrid());
		vl.addComponent(buttons);
		vl.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		vl.addComponent(l);
		w.setContent(vl);

		return w;
	}

	private Grid<ModelForCheckbox> theGrid() {
		Grid<ModelForCheckbox> g = new Grid<>();
		g.setSizeFull();

//		logger.warn(session.getAttribute("items").toString());
//		logger.warn(VaadinService.getCurrentRequest().getWrappedSession().getAttribute("items"));
		itemsHardCopy = hardCopy(items);
		g.setItems(itemsHardCopy);
		g.addColumn(ModelForCheckbox::getName).setCaption("Name").setId("name");
		g.sort("name");

		BooleanSwitchRenderer<ModelForCheckbox> booleanRenderer = new BooleanSwitchRenderer<>(
				ModelForCheckbox::setVisible, "true", "false");

		booleanRenderer.addItemEditListener(event -> {
//			Notification.show("Column " + event.getColumn().getCaption() + " edited with value "
//					+ event.getNewValue().toString());
		});

		g.addColumn(ModelForCheckbox::getVisible, new CheckboxRenderer<>(ModelForCheckbox::setVisible))
				.setCaption("Visible BooleanSwitchRenderer").setRenderer(booleanRenderer);

		return g;
	}

	private Set<ModelForCheckbox> getItemsFromDBDummy() {
		Set<ModelForCheckbox> modelForCheckboxs = new HashSet<>();
		modelForCheckboxs.add(new ModelForCheckbox(1l, "1one", true));
		modelForCheckboxs.add(new ModelForCheckbox(2l, "2two", true));
		modelForCheckboxs.add(new ModelForCheckbox(3l, "3three", false));

		return modelForCheckboxs;
	}

	public Set<ModelForCheckbox> hardCopy(Set<ModelForCheckbox> originalSet) {
		Set<ModelForCheckbox> hardCopy = new HashSet<>();
		for (ModelForCheckbox modelForCheckbox : originalSet) {
			hardCopy.add(new ModelForCheckbox(modelForCheckbox.getId(), modelForCheckbox.getName(),
					modelForCheckbox.getVisible()));
//			hardCopy.add(new ModelForCheckbox(modelForCheckbox));
		}

//      logger.warn(items.toString());
//		logger.warn(hardCopy.toString());
//		logger.warn(hardCopy == items);

		return hardCopy;
	}

	private Set<ModelForCheckbox> getItemsBeforeSaveFromSession() {
		return (Set<ModelForCheckbox>) session.getAttribute("itemsBeforeSave");
	}

	private Set<ModelForCheckbox> getItemsFromDBDummyFromSession() {
		return (Set<ModelForCheckbox>) session.getAttribute("items");
	}
}
