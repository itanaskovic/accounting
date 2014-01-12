/*
 *  Copyright 2012 thorsten frank (thorsten.frank@gmx.de).
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
package de.togginho.accounting.ui.reports;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import de.togginho.accounting.ui.AbstractAccountingHandler;
import de.togginho.accounting.ui.AbstractModalDialog;
import de.togginho.accounting.ui.AccountingUI;
import de.togginho.accounting.ui.IDs;
import de.togginho.accounting.ui.Messages;
import de.togginho.accounting.ui.util.WidgetHelper;

/**
 * @author thorsten
 *
 */
public class ChooseReportCommand extends AbstractAccountingHandler {

	/**
	 * 
	 */
	private static final Logger LOG = Logger.getLogger(ChooseReportCommand.class);
	
	private static final String[][] REPORT_CONFIG = {
		{Messages.IncomeStatementDialog_title, Messages.iconsIncomeStatement, IDs.CMD_OPEN_INCOME_STATEMENT},
		{Messages.RevenueDialog_title, Messages.iconsRevenue, IDs.CMD_OPEN_REVENUE_DIALOG},
		{Messages.RevenueByYearDialog_title, Messages.iconsRevenue, IDs.CMD_OPEN_REVENUE_BY_YEAR_DIALOG},
		{Messages.labelExpenses, Messages.iconsExpenses, IDs.CMD_OPEN_EXPENSES_DIALOG},
		{Messages.IncomeStatementDialog_title, Messages.iconsIncomeStatementDetails, IDs.CMD_OPEN_INCOME_STATEMENT_DETAILS}
	};
	
	/**
	 * The dialog opening command to run - default is the revenue dialog.
	 */
	private String commandIdToRun = IDs.CMD_OPEN_REVENUE_DIALOG;
	
	/**
	 * {@inheritDoc}
	 * @see de.togginho.accounting.ui.AbstractAccountingHandler#doExecute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	protected void doExecute(ExecutionEvent event) throws ExecutionException {
		
		AbstractModalDialog dialog = new AbstractModalDialog(
				getShell(event), 
				Messages.ChooseReportCommand_title, 
				Messages.ChooseReportCommand_message, 
				Messages.iconsReports) {
			
			@Override
			protected void createMainContents(Composite parent) {
				Composite composite = new Composite(parent, SWT.NONE);
				composite.setLayout(new GridLayout(2, false));
				WidgetHelper.grabHorizontal(composite);
        		
				for(int x = 0; x < REPORT_CONFIG.length; x++) {
        			createDialogSelectorButton(composite, REPORT_CONFIG[x][0], REPORT_CONFIG[x][1], REPORT_CONFIG[x][2]);
        		}
			}
		};
		
		if (dialog.show()) {
			LOG.debug("Opening dialog: " + commandIdToRun); //$NON-NLS-1$
			IHandlerService handlerService = 
					(IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
			try {
				handlerService.executeCommand(commandIdToRun, new Event());
			} catch (Exception e) {
				LOG.error("Error opening command " + commandIdToRun, e); //$NON-NLS-1$
			}
		} else {
			LOG.debug("Command was cancelled by user"); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 * @param imgDesc
	 * @param commandToRun
	 */
	private void createDialogSelectorButton(Composite parent, String title, String imgDesc, final String commandToRun) {
		final Button button = new Button(parent, SWT.RADIO);
		button.setImage(AccountingUI.getImageDescriptor(imgDesc).createImage());
		button.setText(title);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, true).indent(5, 0).applyTo(button);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (button.getSelection()) {
					commandIdToRun = commandToRun;
				}
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 * @see de.togginho.accounting.ui.AbstractAccountingHandler#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return LOG;
	}
}