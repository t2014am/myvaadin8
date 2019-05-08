package com.tamim.myvaadin8.heirarchy.dots;
//package com.tamim.myvaadin8.heirarchy_dots;
//
//import java.util.*;
//
//import lombok.Data;
//
//public class ParserTreeGeneric<T extends something> {
//
//	// public PackageInfo getRoot(List<String> packages) throws
//	// JsonProcessingException {
//	public <T extends something> T getRoot(List<String> packages, String topRoot) {
//		Map<String, T> map = new HashMap<>();
//
//		String root = null;
////		String root1 = "be";
//		for (String packageName : packages) {
//			String[] split = packageName.split("\\.");
////            System.out.println(Arrays.asList(split).toString());
//			for (int i = 0; i < split.length; i++) {
//				String singlePackage = split[i];
//				if (topRoot.equals(split[0])) {
//					if (root == null) {
//						root = singlePackage;
//					}
//					map.computeIfAbsent(singlePackage, t -> (T) createContents((T)t.getClass()));
//					if (i - 1 >= 0) {
//						T currentPackage = map.get(singlePackage);
//						map.get(split[i - 1]).getChildren().add(currentPackage);
//					}
//				}
//			}
//		}
//
//		return map.get(root);
//	}
//	
//	T create() {
//		return createContents(null);
//	}
//
//	T createContents(Class<T> clazz) {
//		try {
//			return clazz.newInstance();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	// public static void main(String[] args) throws JsonProcessingException {
//	public static void main(String[] args) {
//		List<String> packages = Arrays.asList("com.project.server", "com.project.client", "com.project.client.util",
//				"com.project.client.util.some", "be.proj", "be.proj.util", "be.proj.test", "be.dirty.test");
//
//		ParserTreeGeneric<PackageInfo> parseTree = new ParserTreeGeneric<>();
//		PackageInfo root = parseTree.getRoot(packages, "be");
//
//		System.out.println(root.toString());
//	}
//}
//
//@Datas
//class PackageInfoo<T extends something> implements something {
//
//	T createContents(Class<T> clazz) {
//		try {
//			return clazz.newInstance();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private String name;
//	private Set<T> children;
//
//	public PackageInfoo(String name) {
//		this.name = name;
//		children = new HashSet<>();
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (this == o) {
//			return true;
//		}
//		if (o == null || getClass() != o.getClass()) {
//			return false;
//		}
//		PackageInfoo<T> that = (PackageInfoo<T>) o;
//		return Objects.equals(name, that.name);
//	}
//
//	@Override
//	public int hashCode() {
//
//		return Objects.hash(name);
//	}
//
//	@Override
//	public <T> Set<T> getChildren() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <T> T createContents() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
