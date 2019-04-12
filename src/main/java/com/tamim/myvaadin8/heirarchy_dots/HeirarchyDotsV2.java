package com.tamim.myvaadin8.heirarchy_dots;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeirarchyDotsV2 {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private List<String> theList;

    private Set<ItemNode> rootItems = new HashSet<>();
    private Set<ItemNode> itemsWithChildren = new HashSet<>();

//    public static void main(String[] args) throws IOException {
//
//    }

    public HeirarchyDotsV2() {
        DummyData dummyData = new DummyData();
        theList = dummyData.getList();

        someMethod();
        logger.warn(rootItems);
    }

    public void someMethod() {
        for (String str : theList) {
            String[] strArray = str.split("\\.");

//            if (rootItems.con)
            rootItems.add(new ItemNode(strArray[0], new HashSet<>()));
        }
    }
}
