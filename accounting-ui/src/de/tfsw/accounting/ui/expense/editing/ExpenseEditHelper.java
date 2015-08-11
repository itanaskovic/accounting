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
package de.tfsw.accounting.ui.expense.editing;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.tfsw.accounting.Constants;
import de.tfsw.accounting.model.DepreciationMethod;
import de.tfsw.accounting.model.Expense;
import de.tfsw.accounting.ui.Messages;
import de.tfsw.accounting.ui.util.GenericLabelProvider;
import de.tfsw.accounting.ui.util.WidgetHelper;

/**
 * @author Thorsten Frank
 *
 */
public class ExpenseEditHelper extends BaseExpenseEditHelper {

	private static final Logger LOG = Logger.getLogger(ExpenseEditHelper.class);
	
	// model object
	private Expense expense;
	
	// Depreciation section
	private ComboViewer depreciationMethod;
	private Spinner depreciationPeriod;
	private Text scrapValue;
	
	/**
	 * @param expense
	 * @param client
	 */
	public ExpenseEditHelper(Expense expense, ExpenseEditingHelperClient client) {
		super(expense, client);
		this.expense = expense;
	}

	/**
	 * @see de.tfsw.accounting.ui.expense.editing.BaseExpenseEditHelper#createBasicSection(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createBasicSection(Composite container) {
		// DATE
		getClient().createLabel(container, Messages.labelDate);
		final DateTime paymentDate = new DateTime(container, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
		WidgetHelper.dateToWidget(expense.getPaymentDate(), paymentDate);
		paymentDate.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				expense.setPaymentDate(WidgetHelper.widgetToDate(paymentDate));
				getClient().modelHasChanged();
			}
		});
		
		// build the rest of the section
		super.createBasicSection(container);
	}
	
	/**
	 * 
	 */
	public void createDepreciationSection(Composite parent) {
		boolean enabled = isDepreciationEnabled();
		
		depreciationMethod = createComboViewer(parent, SWT.READ_ONLY, Messages.labelDepreciationMethod, 
				Expense.FIELD_DEPRECIATION_METHOD, DepreciationMethod.class, DepreciationMethod.values(), 
				new GenericLabelProvider(DepreciationMethod.class, "getTranslatedString"));
		depreciationMethod.getCombo().setEnabled(enabled);
		
		// PERIOD
		getClient().createLabel(parent, Messages.labelDepreciationPeriodInYears);
		depreciationPeriod = new Spinner(parent, SWT.BORDER);
		depreciationPeriod.setMinimum(0);
		depreciationPeriod.setMaximum(100);
		depreciationPeriod.setIncrement(1);
		WidgetHelper.grabHorizontal(depreciationPeriod);
		depreciationPeriod.setEnabled(enabled);
		getBindingCtx().bindValue(
				WidgetProperties.selection().observe(depreciationPeriod),
				PojoProperties.value(Expense.FIELD_DEPRECIATION_PERIOD).observe(expense));
		depreciationPeriod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				notifyModelChange(Expense.FIELD_DEPRECIATION_PERIOD);
			}
		});
		
		scrapValue = createAndBindText(parent, Messages.labelScrapValue, expense, Expense.FIELD_SALVAGE_VALUE, false);
		scrapValue.setEnabled(enabled);
	}
	
	/**
	 * @see de.tfsw.accounting.ui.expense.editing.BaseExpenseEditHelper#notifiyModelChangeInternal(java.lang.String)
	 */
	@Override
	protected void notifiyModelChangeInternal(String origin) {
		if (Expense.FIELD_TYPE.equals(origin)) {
			LOG.debug("Expense_Type changed, now enabling/disabling depreciation widgets");
			enableOrDisableDepreciation();
		}
	}

	/**
	 * 
	 */
	private void enableOrDisableDepreciation() {
		boolean enable = isDepreciationEnabled();
		
		// if depreciation isn't possible for the expense type, make sure that the depreciation values are set to null/empty
		if (!enable) {
			depreciationPeriod.setSelection(0);
			scrapValue.setText(Constants.EMPTY_STRING);
			depreciationMethod.setSelection(StructuredSelection.EMPTY);
		}
		
		depreciationMethod.getCombo().setEnabled(enable);
		depreciationPeriod.setEnabled(enable);
		scrapValue.setEnabled(enable);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isDepreciationEnabled() {
		return expense.getExpenseType() != null && expense.getExpenseType().isDepreciationPossible();
	}
}
