package com.tamim.myvaadin8.heirarchy_dots;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
public class ItemNode implements Serializable {

	private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

	private Integer id = ID_GENERATOR.getAndIncrement();

	private String item;

	private Set<ItemNode> children;

	public void addChild(ItemNode child) {
		children.add(child);
	}

	public ItemNode(String item, Set<ItemNode> children) {
		this.item = item;
		this.children = children;
	}
}
