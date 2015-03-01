/*
 *  Copyright 2015 Thorsten Frank (accounting@tfsw.de).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.tfsw.accounting.elster.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;

import de.tfsw.accounting.elster.ElsterDTO;
import de.tfsw.accounting.elster.ui.ElsterUI;

/**
 * @author Thorsten Frank
 *
 */
abstract class AbstractElsterWizardPage extends WizardPage {
	
	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	AbstractElsterWizardPage(String pageName, String title, String description) {
		super(pageName, title, ElsterUI.getImageDescriptor(Messages.AbstractElsterWizardPage_icon));
		setDescription(description);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.WizardPage#getWizard()
	 */
	@Override
	public ElsterExportWizard getWizard() {
		return (ElsterExportWizard)super.getWizard();
	}
	
	/**
	 * Shorthand for <code>getWizard().getElsterDTO()</code>.
	 * 
	 * @return
	 */
	protected ElsterDTO getDTO() {
		return getWizard().getElsterDTO();
	}
	
	/**
	 * @see org.eclipse.jface.wizard.WizardPage#setErrorMessage(java.lang.String)
	 */
	@Override
	public void setErrorMessage(String newMessage) {
		super.setErrorMessage(newMessage);
	}
}
