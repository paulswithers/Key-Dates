package uk.co.intec.keyDatesApp.pages;

/*

<!--
Copyright 2015 Paul Withers
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License
-->

*/

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         View shown when trying to navigate to a view that does not exist
 *         using {@link com.vaadin.navigator.Navigator}.
 *
 */
public class ErrorView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	private Label explanation;

	/**
	 * Constructor loading error contents
	 */
	public ErrorView() {
		setMargin(true);
		setSpacing(true);

		final Label header = new Label("The view could not be found");
		header.addStyleName(Reindeer.LABEL_H1);
		addComponent(header);
		addComponent(explanation = new Label());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.
	 * ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		explanation.setValue(String.format("You tried to navigate to a view ('%s') that does not exist.", event.getViewName()));
	}
}
