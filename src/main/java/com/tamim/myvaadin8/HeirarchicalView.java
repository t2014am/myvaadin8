package com.tamim.myvaadin8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tamim.myvaadin8.heirarchy.EmployeeNode;
import com.tamim.myvaadin8.heirarchy.ReportToHierarchy;
import com.tamim.myvaadin8.heirarchy_dots.HeirarchyDots;
import com.tamim.myvaadin8.heirarchy_dots.HeirarchyDotsV2;
import com.tamim.myvaadin8.heirarchy_dots.ItemNode;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class HeirarchicalView extends VerticalLayout implements View {
	private static final Logger logger = LogManager.getLogger(HeirarchicalView.class);
	private List<EmployeeNode> rootItems = new ArrayList<>();
	private List<EmployeeNode> children = new ArrayList<>();

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		setSizeFull();

		try {
			ReportToHierarchy.readDataAndCreateMap();
			rootItems.clear();
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

		Tree<EmployeeNode> employeesTree = new Tree<>();
		employeesTree.setItemCaptionGenerator(EmployeeNode::getName);
//        System.out.println(rootItems.toString());

		TreeData<EmployeeNode> employeeNodeTreeData = new TreeData<>();
		employeeNodeTreeData.addItems(rootItems, EmployeeNode::getSubordinates);
//        employeeNodeTreeData.addRootItems(rootItems);

		TreeDataProvider<EmployeeNode> employeeNodeTreeDataProvider = new TreeDataProvider<>(employeeNodeTreeData);

		employeesTree.setDataProvider(employeeNodeTreeDataProvider);
//        employeesTree.expand(rootItems);
//        logger.warn("max depth of the herarchical tree is {} ", ReportToHierarchy.getMaxDepth());
//        logger.warn("getDeepest Children size is {} ", ReportToHierarchy.getDeepestChildren().size());
//        logger.warn("getDeepest Children are {} ", ReportToHierarchy.getDeepestChildren().toString());
		employeesTree.expandRecursively(rootItems, 5);

		employeesTree.addItemClickListener(l -> {
//            logger.warn(l.getItem().toString());
			Notification.show("This is the caption", "You clicked: " + l.getItem().toString(),
					Notification.Type.HUMANIZED_MESSAGE).setDelayMsec(2000);
		});

		HeirarchyDots heirarchyDots = new HeirarchyDots();

		thirdTree();

//        addComponent(employeesTree);
		addComponent(secondTree());
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		super.beforeClientResponse(initial);

//        Notification.show("beforeClientResponse",
//                "The depth of the heirarchical tree is: " + ReportToHierarchy.getMaxDepth(),
//                Notification.Type.HUMANIZED_MESSAGE).setDelayMsec(4000);
	}

	public Component thirdTree() {
		HeirarchyDotsV2 heirarchyDotsV2 = new HeirarchyDotsV2();

		Tree<ItemNode> theTree = new Tree<>();

		return theTree;
	}

	public Component secondTree() {
		HeirarchyDots heirarchyDots = new HeirarchyDots();
		ItemNode itemNode = heirarchyDots.getItemNode();
		Set<ItemNode> itemNodeSet = heirarchyDots.getRootItems();

		logger.warn(itemNodeSet.toString());

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
			Notification.show("This is the caption", "You clicked: " + l.getItem().toString(),
					Notification.Type.HUMANIZED_MESSAGE).setDelayMsec(2000);
		});

		return employeesTree;
	}
}
