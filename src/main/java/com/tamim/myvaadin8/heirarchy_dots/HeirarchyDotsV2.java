package com.tamim.myvaadin8.heirarchy_dots;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeirarchyDotsV2 {
	private final Logger logger = LogManager.getLogger(this.getClass());
	private List<String> theList;

	private Set<ItemNode> rootItems = new HashSet<>();

	public HeirarchyDotsV2() {
		DummyData dummyData = new DummyData();
		theList = dummyData.getList();

//		logger.warn(rootItems);
	}

	public Set<ItemNode> someMethod() {
		logger.info("someMethod called");
		ParserTree parserTree = new ParserTree();
		for (String str : theList) {
			if (!str.contains(".")) {
				logger.warn(str);
				rootItems.add(new ItemNode(str, new HashSet<>()));
			}
		}

		return parserTree.getItemsWithChildren(rootItems, theList);
	}
}
