package com.tamim.myvaadin8.heirarchy.dots;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "children")
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

	public ItemNode(String item) {
		this.item = item;
		this.children = new HashSet<>();
	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		ItemNode other = (ItemNode) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		if (item == null) {
//			if (other.item != null)
//				return false;
//		} else if (!item.equals(other.item))
//			return false;
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + ((item == null) ? 0 : item.hashCode());
//		return result;
//	}

}
