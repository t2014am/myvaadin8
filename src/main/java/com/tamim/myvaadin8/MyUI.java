package com.tamim.myvaadin8;

import javax.servlet.annotation.WebServlet;

import com.tamim.myvaadin8.windows.WindowsAndModalsView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("mytheme")
public class MyUI extends UI {

	private Navigator navigator;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		navigator = new Navigator(this, this);
		navigator.addView("", new InfoPage());
		navigator.addView(Constants.HEIRARCHICAL, new HeirarchicalView());
		navigator.addView(Constants.WINDOWS, new WindowsAndModalsView());

//		final VerticalLayout layout = new VerticalLayout();
//
//		final TextField name = new TextField();
//		name.setCaption("Type your name here:");
//
//		Button button = new Button("Click Me");
//		button.addClickListener(e -> {
//			layout.addComponent(new Label("Thanks " + name.getValue() + ", it works!"));
//		});
//
//		layout.addComponents(name, button);
//
//		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
