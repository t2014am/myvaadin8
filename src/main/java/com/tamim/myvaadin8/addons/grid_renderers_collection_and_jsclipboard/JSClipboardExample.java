package com.tamim.myvaadin8.addons.grid_renderers_collection_and_jsclipboard;

import java.io.Serializable;

import com.vaadin.jsclipboard.JSClipboard;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class JSClipboardExample implements Serializable {

	public VerticalLayout clipboard() {
		final JSClipboard clipboard = new JSClipboard();

		final TextArea textArea = new TextArea();
		textArea.setSizeFull();
		textArea.setPlaceholder("Write here to copy!");
		textArea.setDescription(
				"If u wanna hide this, only possible solution is to use absolute positioning and give left:-100,"
						+ " visbility:hidden and display:none don't work!");
		textArea.setId("tocopie-textfield");

		Button b = new Button("Copy to clipboard");
		clipboard.apply(b, textArea);
		clipboard.addSuccessListener(new JSClipboard.SuccessListener() {

			@Override
			public void onSuccess() {
				if (textArea.getValue().equals("")) {
					Notification.show("TextArea empty. Nothing copied!");
				} else {
					Notification.show("Copy to clipboard successful: " + textArea.getValue());
				}
			}
		});

		clipboard.addErrorListener(new JSClipboard.ErrorListener() {

			@Override
			public void onError() {
				Notification.show("Copy to clipboard unsuccessful", Notification.Type.ERROR_MESSAGE);
			}
		});

		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(false);
		vl.setHeight("500px");
		vl.setWidth("500px");

		vl.addComponent(textArea);
		vl.addComponent(b);

		return vl;
	}
}
