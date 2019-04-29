package com.tamim.myvaadin8.grid_renderers_collection;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.grid.cellrenderers.editable.BooleanSwitchRenderer;
import org.vaadin.grid.cellrenderers.editoraware.CheckboxRenderer;

import com.vaadin.jsclipboard.JSClipboard;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class GridRenderersHere extends VerticalLayout implements View {
	private final Logger logger = LogManager.getLogger(this.getClass());

	Set<ModelForCheckbox> items = getItemsFromDBDummy();

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);

		Button b = new Button("Open Grid Renderers");
		b.setDescription("Open checkbox grid renderer test!");
		b.addClickListener(l -> this.getViewComponent().getUI().addWindow(window()));

		addComponent(b);
		addComponent(clipboard());
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
			Set<ModelForCheckbox> itemsBeforeSave = new HashSet<>(getItemsFromDBDummy());
			items.clear();
			items = new HashSet<>(itemsBeforeSave);
//			logger.warn("itemsBeforeSave " + itemsBeforeSave.toString());
//			logger.warn("items           " + items.toString());
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

		g.setItems(items);
		g.addColumn(ModelForCheckbox::getName).setCaption("Name").setId("name");
		g.sort("name");
		g.addColumn(ModelForCheckbox::getVisible, new CheckboxRenderer<>(ModelForCheckbox::setVisible))
				.setCaption("Visible").setEditorComponent(new CheckBox(), ModelForCheckbox::setVisible)
				.setEditable(true).setId("visible");

//		logger.warn(g.getColumn("visible").getEditorBinding().getGetter().toString());

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

	private VerticalLayout clipboard() {
		final JSClipboard clipboard = new JSClipboard();

		final TextArea textArea = new TextArea();
		textArea.setSizeFull();
		textArea.setPlaceholder("Write here to copy!");
		textArea.setDescription(
				"If u wanna hide this, only possible solution is to use absolute positioning and give left:-100,"
						+ " visbility:hidden and display:none don't work!");
		textArea.setId("tocopie-textfield");

		Button b = new Button("Copy to clipboard");
		clipboard.apply(b, textArea);
		clipboard.addSuccessListener(new JSClipboard.SuccessListener() {

			@Override
			public void onSuccess() {
				if (textArea.getValue().equals("")) {
					Notification.show("TextArea empty. Nothing copied!");
				} else {
					Notification.show("Copy to clipboard successful: " + textArea.getValue());
				}
			}
		});

		clipboard.addErrorListener(new JSClipboard.ErrorListener() {

			@Override
			public void onError() {
				Notification.show("Copy to clipboard unsuccessful", Notification.Type.ERROR_MESSAGE);
			}
		});

		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(false);
		vl.setHeight("500px");
		vl.setWidth("500px");

		vl.addComponent(textArea);
		vl.addComponent(b);

		return vl;
	}

}
