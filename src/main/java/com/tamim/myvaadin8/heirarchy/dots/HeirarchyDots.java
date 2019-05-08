package com.tamim.myvaadin8.heirarchy.dots;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeirarchyDots {

	private final Logger logger = LogManager.getLogger(this.getClass());

	private List<String> theList;
	private Set<ItemNode> rootItems;
//	private Set<ItemNode> heirarchyItems = new HashSet<>();
	private ItemNode itemNode;
	private Integer maxDepth = 0;

	public HeirarchyDots() {
		DummyData dummyData = new DummyData();
		theList = dummyData.getList();

//        logger.debug(theList.toString());

		fixRootItems();

//        getChildren();
		for (ItemNode itemNode : rootItems) {
//            getChildren(itemNode);
			buildHierarchyTree(0, itemNode, 1);
		}

//        logger.warn(maxDepth + " ");
//        logger.warn(rootItems.toString());

//        resultListThatIWant();
	}

	private void fixRootItems() {
		rootItems = new HashSet<>();
		for (String str : theList) {
			if (!str.contains(".")) {
				rootItems.add(new ItemNode(str, new HashSet<>()));
			} else {
//				String[] strArray = str.split("\\.");
//				logger.info(Arrays.asList(strArray).toString());
//                rootItems.add(new ItemNode(strArray[0], new HashSet<>()));
			}
		}
//        logger.debug(rootItems.toString());
	}

	private Set<ItemNode> getChildren(ItemNode itemNode) {
//        logger.info("getChildren");
		Set<ItemNode> children = new HashSet<>();

		for (String str : theList) {
			String[] strArray = str.split("\\.");
			Integer level = 0;
			ItemNode child = returnChild(level + 1, strArray, itemNode.getItem());
			if (child.getItem() != null) {
				children.add(child);
			}
		}
		itemNode.setChildren(children);
		return children;
	}

	public void buildHierarchyTree(int depth, ItemNode localRoot, Integer level) {
		maxDepth = (maxDepth > depth) ? maxDepth : depth;

		Set<ItemNode> subordinates = getChildren(localRoot);
//        logger.info("subordinates: {}", subordinates.toString());
		localRoot.setChildren(subordinates);
//        if (subordinates.size() == 0) {
//            return;
//        }

		for (ItemNode e : subordinates) {
//            logger.warn(level + " level");
			buildHierarchyTree(depth + 1, e, level + 1);
		}
	}

	private ItemNode returnChild(Integer level, String[] strArray, String parentStr) {
		ItemNode child = new ItemNode();
		int x = level - 1;
//        logger.warn(strArray[x] + " " + parentStr + " " + x);
//        logger.info(level + " " + strArray.length + " ");
		if (level < strArray.length && strArray[x].equals(parentStr)) {
//        if (level < strArray.length) {
//            children.add(new ItemNode(strArray[level], null));
			child = new ItemNode(strArray[level], new HashSet<>());
		}
//        logger.info("return children size: {}", children.toString());
		return child;
	}

	public List<ItemNode> resultListThatIWant() {
		List<ItemNode> itemNodes = new ArrayList<>();
		Set<ItemNode> itemNodesChildren = new HashSet<>();
		itemNodesChildren.add(new ItemNode("generic.button", null));
		itemNodesChildren.add(new ItemNode("generic.field", null));

		itemNodes.add(new ItemNode("generic", itemNodesChildren));

		logger.info(itemNodes.toString());

		return itemNodes;
	}

	public ItemNode getItemNode() {
		return itemNode;
	}

	public Set<ItemNode> getRootItems() {
		return rootItems;
	}
}
