package com.tamim.myvaadin8;

import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

public class InfoPage extends VerticalLayout implements View {
	public InfoPage() {
		Link link = new Link("heirarchical!", new ExternalResource("#!" + Constants.HEIRARCHICAL));
		Link windows = new Link("windows and modals!", new ExternalResource("#!" + Constants.WINDOWS));

		addComponent(link);
		addComponent(windows);
	}
}
