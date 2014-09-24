/*
 *  Copyright 2011 thorsten frank (thorsten.frank@gmx.de).
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
package de.togginho.accounting.ui.invoice;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.togginho.accounting.model.Invoice;
import de.togginho.accounting.ui.AbstractAccountingHandler;
import de.togginho.accounting.ui.AccountingUI;
import de.togginho.accounting.ui.Messages;

/**
 * Cancels the currently selected invoice.
 * 
 * @author thorsten
 * @see de.togginho.accounting.AccountingService#cancelInvoice(de.togginho.accounting.model.Invoice)
 */
public class CancelInvoiceFromSelectionHandler extends AbstractInvoiceHandler {

	/** Logger. */
	private static final Logger LOG = Logger.getLogger(CancelInvoiceFromSelectionHandler.class);
	
	/**
	 * 
	 * {@inheritDoc}.
	 * @see AbstractAccountingHandler#doExecute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	protected void doExecute(ExecutionEvent event) throws ExecutionException {
		Invoice invoice = getInvoiceFromSelection(event);
		if (showWarningMessage(
				event, 
				Messages.bind(Messages.CancelInvoiceCommand_confirmMessage, invoice.getNumber()), 
				Messages.CancelInvoiceCommand_confirmText,
				true)) {
			getLogger().info("Cancelling invoice " + invoice.getNumber()); //$NON-NLS-1$
			// do the actual work
			AccountingUI.getAccountingService().cancelInvoice(invoice);
		} else {
			getLogger().info("CancelInvoice was cancelled by user"); //$NON-NLS-1$
		};
	}
	
	/**
	 * {@inheritDoc}
	 * @see AbstractInvoiceHandler.invoice.AbstractInvoiceCommand#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return LOG;
	}	
}