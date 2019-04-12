package com.tamim.myvaadin8.heirarchy_dots;

import java.util.*;

public class ParserTree {

	// public PackageInfo getRoot(List<String> packages) throws
	// JsonProcessingException {
	public PackageInfo getRoot(List<String> packages) {
		Map<String, PackageInfo> map = new HashMap<>();

		String root = null;
		String root1 = "be";
		for (String packageName : packages) {
			String[] split = packageName.split("\\.");
//            System.out.println(Arrays.asList(split).toString());
			for (int i = 0; i < split.length; i++) {
				String singlePackage = split[i];
				if (root1.equals(split[0])) {
					if (root == null) {
						root = singlePackage;
					}
					map.computeIfAbsent(singlePackage, PackageInfo::new);
					if (i - 1 >= 0) {
						PackageInfo currentPackage = map.get(singlePackage);
						map.get(split[i - 1]).getChildren().add(currentPackage);
					}
				}
			}
		}

		return map.get(root);
	}

	// public static void main(String[] args) throws JsonProcessingException {
	public static void main(String[] args) {
		List<String> packages = Arrays.asList("com.project.server", "com.project.client", "com.project.client.util",
				"com.project.client.util.some", "be.proj", "be.proj.util", "be.proj.test", "be.dirty.test");

		ParserTree parseTree = new ParserTree();
		PackageInfo root = parseTree.getRoot(packages);

		System.out.println(root.toString());
	}
}

class PackageInfo {

	private String name;
	private Set<PackageInfo> children;

	public PackageInfo(String name) {
		this.name = name;
		children = new HashSet<>();
	}

	public Set<PackageInfo> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PackageInfo that = (PackageInfo) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {

		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "PackageInfo{" + "name='" + name + '\'' + ", children=" + children + '}';
	}
}