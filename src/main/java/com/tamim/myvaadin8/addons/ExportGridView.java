package com.tamim.myvaadin8.addons;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.haijian.Exporter;

import com.tamim.myvaadin8.heirarchy.BuildHierarchyFromDb;
import com.tamim.myvaadin8.model.HierarchicalEmployee;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ExportGridView extends VerticalLayout implements View {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Grid<HierarchicalEmployee> mGridEmployees = new Grid<>(HierarchicalEmployee.class);
	private HeaderRow filteringHeader;
	private ListDataProvider<HierarchicalEmployee> dataProvider;

	private Boolean firstVisit = true;

	@Override
	public void enter(ViewChangeEvent event) {
		logger.info("enter called! ");
		View.super.enter(event);

		if (firstVisit) {
			logger.warn("firstVisit {}", firstVisit);
			firstVisit = false;
			initTheMainLayout();
		}
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

//		mGridEmployees.setItems(listOfAllEmployees);
		dataProvider = new ListDataProvider<>(listOfAllEmployees);
		mGridEmployees.setDataProvider(dataProvider);
		mGridEmployees.setColumns("firstName", "lastName", "supervisorId", "gender");
		mGridEmployees.setSizeFull();

		addComponent(exportButtons());
		addComponent(mGridEmployees);
		setExpandRatio(mGridEmployees, 1f);

		setColumnFiltering(true);
	}

	public ExportGridView getView() {
		logger.warn("getView called ");
		initTheMainLayout();
		return this;
	}

	// This option is toggleable in the settings menu
	private void setColumnFiltering(boolean filtered) {
		if (filtered && filteringHeader == null) {
			filteringHeader = mGridEmployees.appendHeaderRow();

			// Add new TextFields to each column which filters the data from
			// that column
			TextField filteringFirstNameField = getColumnFilterField();
			filteringFirstNameField
					.addValueChangeListener(event -> dataProvider.setFilter(filterYourObjectGrid(event.getValue())));
			filteringHeader.getCell("firstName").setComponent(filteringFirstNameField);

			TextField filteringLastNameField = getColumnFilterField();
			filteringLastNameField
					.addValueChangeListener(event -> dataProvider.setFilter(filterLastName(event.getValue())));
			filteringHeader.getCell("lastName").setComponent(filteringLastNameField);

			ComboBox<String> filteringGender = getColumnFilterComboBox();
			filteringGender.addValueChangeListener(event -> {
				String theValue = (event.getValue() == null ? "" : event.getValue());
				dataProvider.setFilter(filterGender(theValue));
			});
			filteringHeader.getCell("gender").setComponent(filteringGender);
		} else if (!filtered && filteringHeader != null) {
			dataProvider.clearFilters();
			mGridEmployees.removeHeaderRow(filteringHeader);
			filteringHeader = null;
		}
	}

	private ComboBox<String> getColumnFilterComboBox() {
		ComboBox<String> filteringGender = new ComboBox<>();
		filteringGender.setWidth("100%");
		filteringGender.addStyleName(ValoTheme.TEXTFIELD_TINY);
		filteringGender.setPlaceholder("Filterrrr");
		filteringGender.setEmptySelectionCaption("All");

		Set<String> items = new HashSet<>();
		items.add("Male");
		items.add("Female");
		items.add("Other");
		filteringGender.setItems(items);
		return filteringGender;
	}

	private TextField getColumnFilterField() {
		TextField filter = new TextField();
		filter.setWidth("100%");
		filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
		filter.setPlaceholder("Filterrrr");
		return filter;
	}

	private SerializablePredicate<HierarchicalEmployee> filterYourObjectGrid(String filterFieldText) {
		SerializablePredicate<HierarchicalEmployee> columnPredicate = null;
		columnPredicate = yourObject -> (yourObject.getFirstName().toLowerCase()
				.contains(filterFieldText.toLowerCase()));
		return columnPredicate;
	}

	private SerializablePredicate<HierarchicalEmployee> filterLastName(String filterFieldText) {
		SerializablePredicate<HierarchicalEmployee> columnPredicate = null;
		columnPredicate = yourObject -> (yourObject.getLastName().toLowerCase()
				.contains(filterFieldText.toLowerCase()));
		return columnPredicate;
	}

	private SerializablePredicate<HierarchicalEmployee> filterGender(String filterFieldText) {
		SerializablePredicate<HierarchicalEmployee> columnPredicate = null;
		columnPredicate = yourObject -> (yourObject.getGender().toLowerCase().contains(filterFieldText.toLowerCase()));
		return columnPredicate;
	}

}
