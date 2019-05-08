package com.tamim.myvaadin8.frames;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BrowserFramesView extends VerticalLayout implements View {

	/**
	 * This is used to check if enter was called view a refresh or via a browser
	 * back and again clicking. If refresh is called, it is gonna be empty. If the
	 * back is clicked and then you return back to this view, it will not be empty.
	 */
	private String checkIfEnterWasAlreadyCalled = "";

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		setSizeFull();

		if (("").equals(checkIfEnterWasAlreadyCalled)) {
			checkIfEnterWasAlreadyCalled = "Second call";

			addComponent(browserFrame());
		}
	}

	private BrowserFrame browserFrame() {
		BrowserFrame browser = new BrowserFrame("Browser",
				new ExternalResource("https://tamim-springboot.herokuapp.com/"));
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
