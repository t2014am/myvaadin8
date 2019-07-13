package com.tamim.myvaadin8.heirarchy.dots;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserTree {
	private static Logger logger = LoggerFactory.getLogger(ParserTree.class);

	public Set<ItemNode> getItemsWithChildren(Set<ItemNode> rootItems, List<String> theFlatList) {
		logger.info("getItemsWithChildren(Set<ItemNode> rootItems, List<String> theFlatList) called");
		Set<ItemNode> itemsWithChildren = new HashSet<>();
		for (ItemNode itemNode : rootItems) {
			itemsWithChildren.add(getRoot(theFlatList, itemNode.getItem()));
		}

		return itemsWithChildren;
	}

	// public PackageInfo getRoot(List<String> packages) throws
	// JsonProcessingException {
	public ItemNode getRoot(List<String> packages, String rootItem) {
		Map<String, ItemNode> map = new HashMap<>();
//		logger.warn(rootItem);
//		logger.warn(packages.toString());
		String root = null;
		for (String packageName : packages) {
			String[] split = packageName.split("\\.");
//            System.out.println(Arrays.asList(split).toString());
			for (int i = 0; i < split.length; i++) {
				String singlePackage = split[i];
				if (rootItem.equals(split[0])) {
					if (root == null) {
						root = singlePackage;
					}
//					map.computeIfAbsent(singlePackage, ItemNode::new);
					map.computeIfAbsent(singlePackage, t -> new ItemNode(t));
					if (i - 1 >= 0) {
						ItemNode currentPackage = map.get(singlePackage);
						map.get(split[i - 1]).getChildren().add(currentPackage);
					}
				}
			}
		}

		return map.get(root);
	}

	// public static void main(String[] args) throws JsonProcessingException {
	public static void main(String[] args) {
//		List<String> packages = Arrays.asList("com.project.server", "com.project.client", "com.project.client.util",
//				"com.project.client.util.some", "be.proj", "be.proj.util", "be.proj.test", "be.dirty.test");

//		ParserTree parseTree = new ParserTree();
//		ItemNode root = parseTree.getRoot(packages);

//		System.out.println(root.toString());
	}

}
