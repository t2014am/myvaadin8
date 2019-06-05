package com.tamim.myvaadin8.tabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tamim.myvaadin8.addons.ExportGridView;
import com.tamim.myvaadin8.addons.grid_renderers_collection_and_jsclipboard.GridRenderersView;
import com.tamim.myvaadin8.addons.vritin.ViritinCrudView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page.Styles;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class TabsView extends VerticalLayout implements View {

	private final Logger logger = LogManager.getLogger(this.getClass());
	private String checkIfEnterWasAlreadyCalled = "";

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		setSizeFull();
		if (("").equals(checkIfEnterWasAlreadyCalled)) {
			checkIfEnterWasAlreadyCalled = "Second call";

			addComponent(theTabSheet());
		}
	}

	private TabSheet theTabSheet() {
		logger.warn("getView called ");
		TabSheet tabSheets = new TabSheet();
		tabSheets.setHeight(100.0f, Unit.PERCENTAGE);
		tabSheets.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabSheets.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

		ExportGridView exportGridView = new ExportGridView();
		ExportGridView view = exportGridView.getView();
		tabSheets.addTab(view, exportGridView.getClass().getSimpleName().toString());

		ViritinCrudView viritinCrudView = new ViritinCrudView();
		ViritinCrudView view2 = viritinCrudView.getView();
		view2.setMargin(false);
		view2.addStyleName("viritinCrudViewTab");
		inPageStyles();
		tabSheets.addTab(view2, viritinCrudView.getClass().getSimpleName().toString());

		GridRenderersView gridRenderersView = new GridRenderersView();
		GridRenderersView view3 = gridRenderersView.getView();
		tabSheets.addTab(view3, gridRenderersView.getClass().getSimpleName().toString());

		for (int i = 1; i < 3; i++) {
			final Label label = new Label(getLoremContent(), ContentMode.HTML);
			label.setWidth(100.0f, Unit.PERCENTAGE);

			final VerticalLayout layout = new VerticalLayout(label);
			layout.setMargin(true);

			tabSheets.addTab(layout, "Tab " + i);
		}

		return tabSheets;
	}

	private String getLoremContent() {
		return "Text";
	}

	private void inPageStyles() {
		Styles styles = UI.getCurrent().getPage().getStyles();
		// THIS STYLE IS NEEDED BECAUSE VIRITIN USES A LITTLE DIFFERENT LAYOUT SYSTEM!
		styles.add("." + UI.getCurrent().getTheme() + " .viritinCrudViewTab .v-verticallayout {padding:12px;}");
	}
}
