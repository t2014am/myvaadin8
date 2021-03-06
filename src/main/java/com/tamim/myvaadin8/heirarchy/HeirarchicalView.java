package com.tamim.myvaadin8.heirarchy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tamim.myvaadin8.heirarchy.dots.HeirarchyDots;
import com.tamim.myvaadin8.heirarchy.dots.HeirarchyDotsV2;
import com.tamim.myvaadin8.heirarchy.dots.ItemNode;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class HeirarchicalView extends VerticalLayout implements View {
	private static Logger logger = LoggerFactory.getLogger(HeirarchicalView.class);

	private List<EmployeeNode> rootItems = new ArrayList<>();
	private List<EmployeeNode> children = new ArrayList<>();

	/**
	 * This is used to check if enter was called view a refresh or via a browser
	 * back and again clicking. If refresh is called, it is gonna be empty. If the
	 * back is clicked and then you return back to this view, it will not be empty.
	 */
	private String checkIfEnterWasAlreadyCalled = "";

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		setSizeFull();
		setMargin(true);
		if (("").equals(checkIfEnterWasAlreadyCalled)) {
			checkIfEnterWasAlreadyCalled = "Second call";
			try {
				String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
				String path = "";
				if (basepath.contains("\\")) {
//				check if it is a windows or a linux system
					path = basepath + "\\input-employee.txt";
				} else {
					path = basepath + "/input-employee.txt";
				}
				logger.info(path);
				ReportToHierarchy.setPath(path);
				ReportToHierarchy.readDataAndCreateMap();
				children.clear();
				rootItems = ReportToHierarchy.getRootItems();
				for (EmployeeNode e : rootItems) {
					ReportToHierarchy.buildHierarchyTree(1, e);
				}
				for (EmployeeNode e : rootItems) {
//                children.addAll(ReportToHierarchy.buildHierarchyTree(e));
					children.addAll(ReportToHierarchy.getSubordinatesById(e.getId()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			HorizontalLayout h = new HorizontalLayout();
			h.addComponent(firstTreeEmployees());
			h.addComponent(secondTree());
			h.addComponent(thirdTree());
			h.setSizeFull();

			Label l = new Label("Hierarchical data processinggggggggggggg");
			l.addStyleNames("v-margin-bottom", "v-button-huge");
			l.setDescription("just a heading, what else do you want? ");
			addComponent(l);
			setComponentAlignment(l, Alignment.TOP_CENTER);
			addComponent(h);
			setExpandRatio(h, 1F);
		}
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);
//        Notification.show("beforeClientResponse",
//                "The depth of the heirarchical tree is: " + ReportToHierarchy.getMaxDepth(),
//                Notification.Type.HUMANIZED_MESSAGE).setDelayMsec(4000);
	}

	public Component firstTreeEmployees() {
		Tree<EmployeeNode> employeesTree = new Tree<>();
		employeesTree.setItemCaptionGenerator(EmployeeNode::getName);
		TreeData<EmployeeNode> employeeNodeTreeData = new TreeData<>();
		employeeNodeTreeData.addItems(rootItems, EmployeeNode::getSubordinates);

		TreeDataProvider<EmployeeNode> employeeNodeTreeDataProvider = new TreeDataProvider<>(employeeNodeTreeData);

		employeesTree.setDataProvider(employeeNodeTreeDataProvider);
//        employeesTree.expand(rootItems);
//        logger.warn("max depth of the herarchical tree is {} ", ReportToHierarchy.getMaxDepth());
//        logger.warn("getDeepest Children size is {} ", ReportToHierarchy.getDeepestChildren().size());
//        logger.warn("getDeepest Children are {} ", ReportToHierarchy.getDeepestChildren().toString());
		employeesTree.expandRecursively(rootItems, 5);

		employeesTree.addItemClickListener(l -> {
//            logger.warn(l.getItem().toString());
			Notification.show("This is the caption",
					"You clicked: " + l.getItem().getName() + " with id: " + l.getItem().getId(),
					Notification.Type.HUMANIZED_MESSAGE).setDelayMsec(2000);
		});

		VerticalLayout v = new VerticalLayout();
		v.addComponent(new Label("Heirarcy with a column with parent/supervisor"));
		v.addComponent(employeesTree);
		v.setMargin(false);
		return v;
	}

	public Component secondTree() {
		HeirarchyDots heirarchyDots = new HeirarchyDots();
		Set<ItemNode> itemNodeSet = heirarchyDots.getRootItems();

		Tree<ItemNode> employeesTree = new Tree<>();
		employeesTree.setItemCaptionGenerator(ItemNode::getItem);

		TreeData<ItemNode> employeeNodeTreeData = new TreeData<>();
		employeeNodeTreeData.addItems(itemNodeSet, ItemNode::getChildren);
//        employeeNodeTreeData.addRootItems(rootItems);

		TreeDataProvider<ItemNode> employeeNodeTreeDataProvider = new TreeDataProvider<>(employeeNodeTreeData);

		employeesTree.setDataProvider(employeeNodeTreeDataProvider);
//        employeesTree.expand(rootItems);
//        logger.warn("max depth of the herarchical tree is {} ", ReportToHierarchy.getMaxDepth());
//        logger.warn("getDeepest Children size is {} ", ReportToHierarchy.getDeepestChildren().size());
//        logger.warn("getDeepest Children are {} ", ReportToHierarchy.getDeepestChildren().toString());
		employeesTree.expandRecursively(itemNodeSet, 5);

		employeesTree.addItemClickListener(l -> {
//            logger.warn(l.getItem().toString());
			Notification.show("This is the caption",
					"You clicked: " + l.getItem().getItem() + " with id: " + l.getItem().getId(),
					Notification.Type.HUMANIZED_MESSAGE).setDelayMsec(2000);
		});

		VerticalLayout v = new VerticalLayout();
//		.addStyleNames("v-align-bottom", "v-botton-primary")
		v.addComponent(new Label("Heirarcy with dots, FAILED TRY! "));
		v.addComponent(employeesTree);
		v.setMargin(false);
		return v;
	}

	public Component thirdTree() {
		HeirarchyDotsV2 heirarchyDotsV2 = new HeirarchyDotsV2();

		Tree<ItemNode> theTree = new Tree<>();
		theTree.setItemCaptionGenerator(ItemNode::getItem);
		TreeData<ItemNode> employeeNodeTreeData = new TreeData<>();
		Set<ItemNode> itemNodeSet = new HashSet<>();
		itemNodeSet.addAll(heirarchyDotsV2.getItemsWithChildren());
		employeeNodeTreeData.addItems(itemNodeSet, ItemNode::getChildren);

		TreeDataProvider<ItemNode> theTreeDataProvider = new TreeDataProvider<>(employeeNodeTreeData);

		theTree.setDataProvider(theTreeDataProvider);
		theTree.expandRecursively(itemNodeSet, 5);
		theTree.addItemClickListener(l -> {
			Notification.show("This is the caption",
					"You clicked: " + l.getItem().getItem() + " with id: " + l.getItem().getId(),
					Notification.Type.HUMANIZED_MESSAGE).setDelayMsec(2000);
		});

		VerticalLayout v = new VerticalLayout();
		v.addComponent(new Label("Heirarcy with dots"));
		v.addComponent(theTree);
		v.setMargin(false);
		return v;
	}
}
