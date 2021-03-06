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
package de.tfsw.accounting.ui.invoice;

import java.time.LocalDate;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

import de.tfsw.accounting.model.Invoice;
import de.tfsw.accounting.ui.AbstractModalDialog;
import de.tfsw.accounting.ui.AccountingUI;
import de.tfsw.accounting.ui.Messages;
import de.tfsw.accounting.ui.util.WidgetHelper;
import de.tfsw.accounting.util.FormatUtil;

/**
 * Opens a dialog for selecting a payment date and if succesfull, marks this invoice as paid on the selected date.
 * This handler is active when the active view is {@link InvoiceView} and an invoice is selected, and is enabled only
 * when {@link Invoice#canBePaid()} returns <code>true</code> for that invoice.
 * 
 * @author tfrank1
 * @see de.tfsw.accounting.AccountingService#markAsPaid(Invoice, Date)
 */
public class PayInvoiceFromSelectionHandler extends AbstractInvoiceHandler {

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(PayInvoiceFromSelectionHandler.class);
	
	private LocalDate paymentDate;
	
	/**
	 * {@inheritDoc}.
	 * @see de.tfsw.accounting.ui.invoice.AbstractInvoiceHandler#doExecute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
    protected void doExecute(ExecutionEvent event) throws ExecutionException {
		final Invoice invoice = doGetInvoice(event);
		
		AbstractModalDialog dialog = new AbstractModalDialog(
				getShell(event), Messages.MarkInvoiceAsPaidCommand_title, Messages.MarkInvoiceAsPaidCommand_message) {
			
			@Override
			protected void createMainContents(Composite parent) {
				Composite composite = new Composite(parent, SWT.NONE);
				composite.setLayout(new GridLayout(1, false));

				WidgetHelper.createLabel(composite, Messages.MarkInvoiceAsPaidCommand_paymentDateLabel);
				
				final DateTime paymentDateWidget = new DateTime(composite, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
				paymentDate = WidgetHelper.widgetToDate(paymentDateWidget);
				paymentDateWidget.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						paymentDate = WidgetHelper.widgetToDate(paymentDateWidget);
						boolean okEnabled = false;
						if (paymentDate.isAfter(LocalDate.now())) {
							setErrorMessage(Messages.MarkInvoiceAsPaidCommand_errorPaymentDateInTheFuture);
						} else if (paymentDate.isBefore(invoice.getSentDate())) {
							setErrorMessage(Messages.MarkInvoiceAsPaidCommand_errorPaymentDateBeforeSentDate);
						} else {
							okEnabled = true;
						}
						
						getButton(IDialogConstants.OK_ID).setEnabled(okEnabled);
					}
				});
				
			}
			
		};
		
		if (dialog.show()) {
			LOG.info(String.format("Now marking invoice [%s] as paid on [%s]", //$NON-NLS-1$
					invoice.getNumber(), FormatUtil.formatDate(paymentDate)));
			
			AccountingUI.getAccountingService().markAsPaid(invoice, paymentDate);
		} else {
			LOG.debug("Payment was cancelled..."); //$NON-NLS-1$
		}
    }

	/**
	 * {@inheritDoc}.
	 * @see de.tfsw.accounting.ui.AbstractAccountingHandler#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	protected Invoice doGetInvoice(ExecutionEvent event) {
		return getInvoiceFromSelection(event);
	}
}
