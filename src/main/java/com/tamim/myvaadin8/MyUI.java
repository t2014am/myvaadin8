package com.tamim.myvaadin8;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tamim.myvaadin8.frames.BrowserFramesView;
import com.tamim.myvaadin8.grid_renderers_collection.GridRenderersView;
import com.tamim.myvaadin8.heirarchy.HeirarchicalView;
import com.tamim.myvaadin8.viritin.ViritinCrudView;
import com.tamim.myvaadin8.windows.WindowsAndModalsView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
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
//@PreserveOnRefresh
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
		navigator.addView(Constants.GRID_RENDERERS_COLLECTION, new GridRenderersView());
		navigator.addView(Constants.CRUD_VIEW, new ViritinCrudView());
		navigator.addView(Constants.BROWSER_FRAMES, new BrowserFramesView());

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
	public static class MyUIServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener {
		private final Logger logger = LogManager.getLogger(this.getClass());

		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			getService().addSessionInitListener(this);
			getService().addSessionDestroyListener(this);
		}

		@Override
		public void sessionDestroy(SessionDestroyEvent event) {
			logger.info("SessionDestroy called!");

		}

		@Override
		public void sessionInit(SessionInitEvent event) throws ServiceException {
			logger.info("SessionInit called!");

		}
	}
}
