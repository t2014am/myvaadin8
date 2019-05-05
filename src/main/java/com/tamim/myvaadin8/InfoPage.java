package com.tamim.myvaadin8;

import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class InfoPage extends VerticalLayout implements View {
	public InfoPage() {
		Link link = new Link("heirarchical!", new ExternalResource("#!" + Constants.HEIRARCHICAL));
		Link windows = new Link("windows and modals!", new ExternalResource("#!" + Constants.WINDOWS));
		Link gridRenderersCollection = new Link("grid renderers collection!",
				new ExternalResource("#!" + Constants.GRID_RENDERERS_COLLECTION));
		Link crudView = new Link("crud view!", new ExternalResource("#!" + Constants.CRUD_VIEW));

		addComponent(link);
		addComponent(windows);
		addComponent(gridRenderersCollection);
		addComponent(crudView);
	}
}
