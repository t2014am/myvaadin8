package com.tamim.myvaadin8.grid_renderers_collection;

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
public class GridRenderersHere extends VerticalLayout implements View {
	private final Logger logger = LogManager.getLogger(this.getClass());

	Set<ModelForCheckbox> items = getItemsFromDBDummy();
	Set<ModelForCheckbox> itemsTemp = getItemsFromDBDummy();
	Set<ModelForCheckbox> itemsBeforeSave = getItemsFromDBDummy();
	Set<ModelForCheckbox> itemsFromDBDummyFromSession;
	private VaadinSession session;

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		logger.info("entered! ");
		session = getUI().getSession();
		session.setAttribute("items", items);
		session.setAttribute("itemsBeforeSave", itemsBeforeSave);
//		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("items", items);
		itemsFromDBDummyFromSession = getItemsFromDBDummyFromSession();

		Button b = new Button("Open Grid Renderers");
		b.setDescription("Open checkbox grid renderer test!");
		b.addClickListener(l -> this.getViewComponent().getUI().addWindow(window()));

		addComponent(b);
		JSClipboardExample jsClipboardExample = new JSClipboardExample();
		addComponent(jsClipboardExample.clipboard());
	}

	private Window window() {
		Window w = new Window("Please select");
		w.setWindowMode(WindowMode.NORMAL);
		w.setWidth("50%");

		Button save = new Button("Save");
		save.addClickListener(l -> {

//			logger.warn("itemsBeforeSave " + itemsBeforeSave.toString());
//			logger.warn("items           " + items.toString());
//			saveItemsToDB(items);
			w.close();
		});

		Button cancel = new Button("Cancel");
		cancel.addClickListener(l -> {
			logger.trace("Cancel");
//			items.clear();
//		    TODO: SO ITEMS.CLEAR() IS GOING TO CLEAR OUT THE DATA FROM SESSION TOO! 
			Set<ModelForCheckbox> itemsTempp = new HashSet<>(itemsTemp);
			items = new HashSet<>(itemsTempp);
			logger.warn("items           " + items.toString());
//			logger.warn("items session   " + getItemsFromDBDummyFromSession().toString());
//			logger.warn("items getShit   " + getShit().toString());
//			logger.warn("getItemsBSion   " + getItemsBeforeSaveFromSession().toString());
			logger.warn("itemsTemp       " + itemsTemp.toString());
//			logger.warn("items session   " + VaadinService.getCurrentRequest().getWrappedSession().getAttribute("items"));
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
		g.setItems(items);
		g.addColumn(ModelForCheckbox::getName).setCaption("Name").setId("name");
		g.sort("name");

//		CheckBox visibile = new CheckBox();
//		g.addColumn(ModelForCheckbox::getVisible, new CheckboxRenderer<>(ModelForCheckbox::setVisible))
//				.setCaption("Visible").setEditorComponent(visibile, ModelForCheckbox::setVisible).setEditable(true)
//				.setId("visible");

//		visibile.addValueChangeListener(l -> {
////			NOT WORKING! 
//			logger.warn("visible checkbox clicked! ");
//			logger.warn(l.getValue() + " ");
//		});

//		logger.warn(g.getColumn("visible").getEditorBinding().getGetter().toString());

		BooleanSwitchRenderer<ModelForCheckbox> booleanRenderer = new BooleanSwitchRenderer<>((obj, args) -> {
			logger.warn("Clicked: {} {}", itemsTemp.toString());
//			logger.warn("Clicked: {} {}", itemsTemp.size(), itemsTemp.toString());
//			logger.warn("Clicked: {} {}", obj.toString());
//			set.removeIf(s -> s.length() % 2 == 0) // better way! 
			for (Iterator<ModelForCheckbox> iterator = itemsTemp.iterator(); iterator.hasNext();) {
				ModelForCheckbox s = iterator.next();
				if ((Long) s.getId() == (Long) obj.getId()) {
//					logger.info("removing {}", obj);
					iterator.remove();
				}
			}
//			logger.debug("Clicked: {} {}", itemsTemp.toString());
			itemsTemp.add(obj);
			obj.setVisible(args);
			for (Iterator<ModelForCheckbox> iterator = itemsTemp.iterator(); iterator.hasNext();) {
				ModelForCheckbox s = iterator.next();
				if ((Long) s.getId() == (Long) obj.getId()) {
//					logger.info("removing {}", obj);
					iterator.remove();
				}
			}
//			itemsTemp.add(obj);
//			logger.warn("Clicked: {} {}", itemsTemp.size());
			logger.warn("Clicked: {} {}", items.toString());
		}, "true", "false");

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

	private Set<ModelForCheckbox> getShit() {
//		logger.debug(itemsFromDBDummyFromSession.toString());
		return itemsFromDBDummyFromSession;
	}

	private Set<ModelForCheckbox> getItemsBeforeSaveFromSession() {
		return (Set<ModelForCheckbox>) session.getAttribute("itemsBeforeSave");
	}

	private Set<ModelForCheckbox> getItemsFromDBDummyFromSession() {
		return (Set<ModelForCheckbox>) session.getAttribute("items");
	}
}
