package com.tamim.myvaadin8.frames;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BrowserFrames extends VerticalLayout implements View {

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		setSizeFull();

		addComponent(browserFrame());
	}

	private BrowserFrame browserFrame() {
		BrowserFrame browser = new BrowserFrame("Browser", new ExternalResource("https://tamim-springboot.herokuapp.com/"));
//		browser.setWidth("600px");
//		browser.setHeight("400px");

		browser.setCaption("BrowserFrame");
		browser.setSizeFull();
		browser.addStyleName("browserFrame");
		browser.setAlternateText("Alternate Text");
		browser.setDescription("Description " + VaadinIcons.PLUG.getHtml(), ContentMode.HTML);
		browser.setIcon(VaadinIcons.PLUG);
		browser.setResponsive(true);

		return browser;
	}
}
