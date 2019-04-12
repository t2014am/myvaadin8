package com.tamim.myvaadin8.heirarchy_dots;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

	public List<String> getList() {
		List<String> theItems = new ArrayList<>();
		theItems.add("generic");
		theItems.add("generic.button");
		theItems.add("generic.button.cancel");
		theItems.add("generic.button.ok");
		theItems.add("generic.button.help");
		theItems.add("generic.field");
		theItems.add("generic.field.name");
		theItems.add("generic.field.placeholder");
		theItems.add("generic.field.error");
		theItems.add("myproject");
		theItems.add("myproject.button");
		theItems.add("myproject.button.goaway");
		theItems.add("myproject.button.stay");

		return theItems;
	}
}
