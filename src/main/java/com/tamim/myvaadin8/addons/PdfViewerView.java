package com.tamim.myvaadin8.addons;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.VerticalLayout;
import com.whitestein.vaadin.widgets.wtpdfviewer.WTPdfViewer;

@SuppressWarnings("serial")
public class PdfViewerView extends VerticalLayout implements View {
	private static Logger logger = LoggerFactory.getLogger(PdfViewerView.class);

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);

		addComponent(pdfViewer());
	}

	private WTPdfViewer pdfViewer() {
		WTPdfViewer pdfViewer = new WTPdfViewer();

		// get file stream
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		String path = "";
		if (basepath.contains("\\")) {
//		check if it is a windows or a linux system
			path = basepath + "\\";
		} else {
			path = basepath + "/";
		}
		logger.info(path);
		String filename = path + "cv.pdf";
		InputStream dataStream = getClass().getClassLoader().getResourceAsStream(filename);

		// show file in pdf viewer
		pdfViewer.setResource(new StreamResource(new InputStreamSource(dataStream), filename));

//		pdfViewer.setPage(5);

		return pdfViewer;
	}

	// boilerplate StreamSource implementation
	class InputStreamSource implements StreamSource {

		private final InputStream data;

		public InputStreamSource(InputStream data) {
			super();
			this.data = data;
		}

		@Override
		public InputStream getStream() {
			return data;
		}

	}
}
