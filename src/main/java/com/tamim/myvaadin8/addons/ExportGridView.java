package com.tamim.myvaadin8.addons;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.haijian.Exporter;

import com.tamim.myvaadin8.heirarchy.BuildHierarchyFromDb;
import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ExportGridView extends VerticalLayout implements View {
	private final Logger logger = LogManager.getLogger(this.getClass());

	private Grid<HierarchicalEmployee> mGridEmployees = new Grid<>(HierarchicalEmployee.class);

	@Override
	public void enter(ViewChangeEvent event) {
		logger.info("enter called! ");
		View.super.enter(event);

		initTheMainLayout();
	}

	private HorizontalLayout exportButtons() {
		FileDownloader exportToExcelDownloader = new FileDownloader(createXlsxResource());
		Button exportToExcel = new Button("Export to excel");
		exportToExcel.setIcon(VaadinIcons.CHART_GRID);
		exportToExcelDownloader.extend(exportToExcel);

		FileDownloader exportToCsvDownloader = new FileDownloader(createCsvResource());
		Button exportToCsv = new Button("Export to csv");
		exportToCsvDownloader.extend(exportToCsv);
		exportToCsv.setIcon(VaadinIcons.GRID_BEVEL);

		return new HorizontalLayout(exportToExcel, exportToCsv);
	}

	private StreamResource createXlsxResource() {
		return new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {
				return Exporter.exportAsExcel(mGridEmployees);
			}
		}, "xlsx.xlsx");
	}

	private StreamResource createCsvResource() {
		return new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {
				return Exporter.exportAsCSV(mGridEmployees);
			}
		}, "csv.csv");
	}

	private void initTheMainLayout() {
		setSizeFull();

		BuildHierarchyFromDb b = new BuildHierarchyFromDb();
		ArrayList<HierarchicalEmployee> listOfAllEmployees = new ArrayList<>(b.getEmployeesFlatWithSupervisors());

		mGridEmployees.setColumns("firstName", "lastName", "supervisorId", "gender");
		mGridEmployees.setItems(listOfAllEmployees);
		mGridEmployees.setSizeFull();

		addComponent(exportButtons());
		addComponent(mGridEmployees);
		setExpandRatio(mGridEmployees, 1f);
	}

	public ExportGridView getView() {
		logger.warn("getView called ");
		initTheMainLayout();
		return this;
	}

}
