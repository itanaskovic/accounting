/*
 *  Copyright 2011 , 2014 Thorsten Frank (accounting@tfsw.de).
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
package de.tfsw.accounting.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import de.tfsw.accounting.model.Address;
import de.tfsw.accounting.ui.util.WidgetHelper;

/**
 * A {@link WizardPage} to capture an {@link Address}.
 * 
 * @author thorsten
 *
 */
public class AddressWizardPage extends WizardPage {

	private String helpContextId;
	private Text street;
	private Text postCode;
	private Text city;
	private Text phone;
	private Text mobile;
	private Text email;
	private Text fax;
	
	/**
	 * Creates a new wizard page for an {@link Address}.
	 * 
	 * @param helpContextId the help context id - may be <code>null</code>
	 */
	public AddressWizardPage(final String helpContextId) {
		super("AddressWizardPage"); //$NON-NLS-1$
		setTitle(Messages.AddressWizardPage_title);
		setDescription(Messages.AddressWizardPage_desc);
		this.helpContextId = helpContextId;
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		if (helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, helpContextId);
		}
		
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		
		GridDataFactory grabHorizontal = GridDataFactory.fillDefaults().grab(true, false);
		
		WidgetHelper.createLabel(composite, Messages.labelStreet);
		street = WidgetHelper.createSingleBorderText(composite, null);
		grabHorizontal.applyTo(street);
		
		WidgetHelper.createLabel(composite, Messages.labelPostalCode);
		postCode = WidgetHelper.createSingleBorderText(composite, null);
		grabHorizontal.applyTo(postCode);
		
		WidgetHelper.createLabel(composite, Messages.labelCity);
		city = WidgetHelper.createSingleBorderText(composite, null);
		grabHorizontal.applyTo(city);
		
		WidgetHelper.createLabel(composite, Messages.labelPhone);
		phone = WidgetHelper.createSingleBorderText(composite, null);
		grabHorizontal.applyTo(phone);
		
		WidgetHelper.createLabel(composite, Messages.labelMobile);
		mobile = WidgetHelper.createSingleBorderText(composite, null);
		grabHorizontal.applyTo(mobile);
		
		WidgetHelper.createLabel(composite, Messages.labelFax);
		fax = WidgetHelper.createSingleBorderText(composite, null);
		grabHorizontal.applyTo(fax);
		
		WidgetHelper.createLabel(composite, Messages.labelEmail);
		email = WidgetHelper.createSingleBorderText(composite, null);
		grabHorizontal.applyTo(email);
		
		setControl(composite);
		setPageComplete(true);
	}

	/**
	 * {@inheritDoc}.
	 * @see org.eclipse.jface.dialogs.DialogPage#performHelp()
	 */
	@Override
	public void performHelp() {
		if (helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextId);
		}
	}
	
	/**
	 * Returns the {@link Address} represented by this {@link WizardPage}. If no information has been entered,
	 * <code>null</code> is returned.
	 * 
	 * @return the edited {@link Address} or <code>null</code> if no values were supplied by the user
	 */
	public Address getAddress() {
		Address address = new Address();
		boolean hasValue = false;
		
		if (!street.getText().isEmpty()) {
			address.setStreet(street.getText());
			hasValue = true;
		}
		if (!postCode.getText().isEmpty()) {
			address.setPostalCode(postCode.getText());
			hasValue = true;
		}
		if (!city.getText().isEmpty()) {
			address.setCity(city.getText());
			hasValue = true;
		}
		if (!phone.getText().isEmpty()) {
			address.setPhoneNumber(phone.getText());
			hasValue = true;
		}
		if (!mobile.getText().isEmpty()) {
			address.setMobileNumber(mobile.getText());
			hasValue = true;
		}
		if (!email.getText().isEmpty()) {
			address.setEmail(email.getText());
			hasValue = true;
		}
		if (!fax.getText().isEmpty()) {
			address.setFaxNumber(fax.getText());
			hasValue = true;
		}
		
		return hasValue ? address : null;
	}
}
