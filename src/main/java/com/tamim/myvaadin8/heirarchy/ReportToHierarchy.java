package com.tamim.myvaadin8.heirarchy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * SOURCE:
 * https://www.lavivienpost.com/build-hierarchy-tree/
 * */

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tamim.myvaadin8.HeirarchicalView;

public class ReportToHierarchy {
	private static final Logger logger = LogManager.getLogger(ReportToHierarchy.class);
	static Map<Integer, EmployeeNode> employees;
	static List<EmployeeNode> rootItems;
	static List<EmployeeNode> deepestChildren = new ArrayList<>();

	static Integer maxDepth = 0;

	public static void main(String[] args) throws IOException {
		readDataAndCreateMap();
		// for (EmployeeNode e : rootItems) {
		// buildHierarchyTree(0, e);
		// }

		for (EmployeeNode e : rootItems) {
			printHierarchyTree(e, 0);
		}
	}

	public static void readDataAndCreateMap() throws IOException {
		employees = new HashMap<Integer, EmployeeNode>();
		// final String dir = System.getProperty("user.dir");
		// String path = dir.replace("/", "\\\\") + "\\data\\";
		// String path = "vaadin8/heirarchy/input-employee.txt";
		File currentDirFile = new File("");
		System.out.println(currentDirFile.getAbsolutePath());
		logger.warn(currentDirFile.getAbsolutePath());
		
		String pathForTesting = currentDirFile.getAbsolutePath();
		String path = "";
		//TODO: Learn about relative paths extensively! 
		if (pathForTesting.contains("home") || pathForTesting.contains("mnt")) {
			/// mnt/hgfs/share_with_vm/Ubuntu/my_projects/myvaadin8
			// path =
			// "/mnt/hgfs/share_with_vm/Ubuntu/my_projects/myvaadin8/myvaadin8/src/main/java/com/tamim/myvaadin8/heirarchy/input-employee.txt";
			path = "/mnt/hgfs/share_with_vm/Ubuntu/my_projects/myvaadin8" + "/src/main/java" + "/input-employee.txt";
			// /myvaadin8/src/main/java/com/tamim/myvaadin8/heirarchy/input-employee.txt
		} else {
//			path = "G:\\My_User_Files\\my_projects\\vaadin8dev\\com.vaadin8.dev-ui\\src\\main\\java\\vaadin8\\heirarchy\\input-employee.txt";
			path = "C:\\Portables\\share_with_vm\\Ubuntu\\my_projects\\myvaadin8\\src\\main\\java\\input-employee.txt";
		}
		System.out.println(path);
		logger.warn(path);

		try {
			FileReader fin = new FileReader(path);
			BufferedReader br = new BufferedReader(fin);

			String strLine;
			EmployeeNode employee = null;
			rootItems = new ArrayList<>();
			while ((strLine = br.readLine()) != null) {
				String[] values = strLine.split(" ");
				try {
					if (values.length > 1) {
						employee = new EmployeeNode(values[0], values[1] + " " + values[2], values[3]);
					}
				} catch (Exception e) {
					employee = new EmployeeNode(values[0], values[1] + " " + values[2], "0");
				}
				employees.put(employee.getId(), employee);

				if (employee.getReportToId() == 0) {
					// System.out.println(employee.toString());
					rootItems.add(employee);
				}
			}
			fin.close();
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e);
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		}
	}

	// scan whole employee hashMap to form a list of subordinates for the given id
	public static List<EmployeeNode> getSubordinatesById(int rid) {
		List<EmployeeNode> subordinates = new ArrayList<EmployeeNode>();
		for (EmployeeNode e : employees.values()) {
			if (e.getReportToId() == rid) {
				subordinates.add(e);
			}
		}
		return subordinates;
	}

	// build tree recursively
	public static void buildHierarchyTree(int depth, EmployeeNode localRoot) {
		maxDepth = (maxDepth > depth) ? maxDepth : depth;

		// if (maxDepth == depth) {
		// deepestChildren.add(localRoot);
		// } else if (maxDepth > depth) {
		// deepestChildren.clear();
		// deepestChild = localRoot;
		// maxDepth = depth;
		// }

		// if (maxDepth > depth) {
		// deepestChildren.clear();
		// deepestChildren.add(localRoot);
		// } else {
		// maxDepth = depth;
		//
		// }

		List<EmployeeNode> subordinates = getSubordinatesById(localRoot.getId());
		localRoot.setSubordinates(subordinates);
		if (subordinates.size() == 0) {
			return;
		}

		for (EmployeeNode e : subordinates) {
			buildHierarchyTree(depth + 1, e);
		}
	}

	// print tree recursively
	private static void printHierarchyTree(EmployeeNode localRoot, int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("\t");
		}
		System.out.println(localRoot.getName());

		List<EmployeeNode> subordinates = localRoot.getSubordinates();
		System.out.print(" ");
		for (EmployeeNode e : subordinates) {
			printHierarchyTree(e, level + 1);
		}
	}

	public static List<EmployeeNode> getDeepestChildren() {
		return deepestChildren;
	}

	public static Integer getMaxDepth() {
		return maxDepth;
	}

	// public static Map<Integer, EmployeeNode> getEmployees() {
	// return employees;
	// }

	public static List<EmployeeNode> getRootItems() {
		return rootItems;
	}
}
