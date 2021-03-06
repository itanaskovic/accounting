/*
 *  Copyright 2012 , 2014 Thorsten Frank (accounting@tfsw.de).
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
package de.tfsw.accounting.ui.reports;

import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.tfsw.accounting.Constants;
import de.tfsw.accounting.model.Expense;
import de.tfsw.accounting.model.ExpenseCollection;
import de.tfsw.accounting.model.ExpenseType;
import de.tfsw.accounting.model.IncomeStatement;
import de.tfsw.accounting.model.Invoice;
import de.tfsw.accounting.model.Price;
import de.tfsw.accounting.model.Revenue;
import de.tfsw.accounting.ui.AccountingUI;
import de.tfsw.accounting.ui.Messages;
import de.tfsw.accounting.ui.util.WidgetHelper;
import de.tfsw.accounting.util.CalculationUtil;
import de.tfsw.accounting.util.FormatUtil;
import de.tfsw.accounting.util.TimeFrame;

/**
 * @author thorsten
 *
 */
public class IncomeStatementDialog extends AbstractReportDialog {

	private static final String HELP_CONTEXT_ID = AccountingUI.PLUGIN_ID + ".IncomeStatementDetailsDialog"; //$NON-NLS-1$
	
	private IncomeStatement incomeStatement;
	
	private FormToolkit toolkit;
	
	private Tree revenueTree;
	private Tree opexTree;
	private Tree capexTree;
	private Tree otherExpensesTree;
	
	private Text grossProfitNet;
	private Text grossProfitTax;
	private Text grossProfitGross;
	
	/**
     * @param shell
     */
    IncomeStatementDialog(Shell shell) {
	    super(shell, TimeFrame.lastYear());
    }

    /**
     * 
     * {@inheritDoc}.
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
    	getShell().setText(Messages.IncomeStatementDialog_title);
    	getShell().setImage(AccountingUI.getImageDescriptor(Messages.iconsIncomeStatement).createImage());
    	
    	PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_CONTEXT_ID);
    	
    	toolkit = new FormToolkit(parent.getDisplay());
    	
    	Composite container = new Composite(parent, SWT.NONE);
    	container.setLayout(new GridLayout(1, true));
    	WidgetHelper.grabBoth(container);
    	
    	createQuerySection(container);
    	
    	//createUniqueQuerySection(container);
    	
    	createRevenueSection(container);
    	
    	createOpexSection(container);
    	
    	createGrossProfitSection(container);

    	createCapexSection(container);
    	
    	createOtherExpensesSection(container);
    	
    	updateModel();
    	
    	return container;
    }
    
    /**
     * 
     * @param parent
     */
    private void createRevenueSection(Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.CLIENT_INDENT);
		WidgetHelper.grabBoth(section);
		section.setText(Messages.labelRevenue);
		
		Composite sectionClient = toolkit.createComposite(section);
		section.setClient(sectionClient);
		sectionClient.setLayout(new GridLayout(1, false));
		
		revenueTree = toolkit.createTree(sectionClient, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).hint(600, 100).applyTo(revenueTree);
		revenueTree.setHeaderVisible(true);
		revenueTree.setLinesVisible(true);
		
		createTreeColumn(revenueTree, SWT.LEFT, Messages.InvoiceView_number, 150);
		createTreeColumn(revenueTree, SWT.LEFT, Messages.labelInvoiceDate, 150);
		createTreeColumn(revenueTree, SWT.RIGHT, Messages.labelNet, 100);
		createTreeColumn(revenueTree, SWT.RIGHT, Messages.labelTaxes, 100);
		createTreeColumn(revenueTree, SWT.RIGHT, Messages.labelGross, 100);
    }
    

    
    /**
     * 
     * @param parent
     */
    private void createOpexSection(Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		WidgetHelper.grabBoth(section);
		toolkit.paintBordersFor(section);
		section.setText(ExpenseType.OPEX.getTranslatedString());
		
		Composite sectionClient = toolkit.createComposite(section);
		section.setClient(sectionClient);
		sectionClient.setLayout(new GridLayout(1, false));
		
		opexTree = toolkit.createTree(sectionClient, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		initExpensesTree(opexTree, sectionClient);
    }

    /**
     * 
     * @param parent
     */
    private void createGrossProfitSection(Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		WidgetHelper.grabBoth(section);
		toolkit.paintBordersFor(section);
		section.setText("Gross Profit");
		
		Composite sectionClient = toolkit.createComposite(section);
		section.setClient(sectionClient);
		sectionClient.setLayout(new GridLayout(6, false));
				
		toolkit.createLabel(sectionClient, Messages.labelNet);
		grossProfitNet = toolkit.createText(sectionClient, Constants.EMPTY_STRING);
		grossProfitNet.setOrientation(SWT.RIGHT_TO_LEFT);
		grossProfitNet.setEnabled(false);
		grossProfitNet.setEditable(false);
		WidgetHelper.grabHorizontal(grossProfitNet);
		
		toolkit.createLabel(sectionClient, Messages.labelTaxes);
		grossProfitTax = toolkit.createText(sectionClient, Constants.EMPTY_STRING);
		grossProfitTax.setOrientation(SWT.RIGHT_TO_LEFT);
		grossProfitTax.setEnabled(false);
		grossProfitTax.setEditable(false);
		WidgetHelper.grabHorizontal(grossProfitTax);
		
		toolkit.createLabel(sectionClient, Messages.labelGross);
		grossProfitGross = toolkit.createText(sectionClient, Constants.EMPTY_STRING);
		grossProfitGross.setOrientation(SWT.RIGHT_TO_LEFT);
		grossProfitGross.setEnabled(false);
		grossProfitGross.setEditable(false);
		WidgetHelper.grabHorizontal(grossProfitGross);
    }
    
    /**
     * 
     * @param parent
     */
    private void createCapexSection(Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		WidgetHelper.grabBoth(section);
		section.setText(ExpenseType.CAPEX.getTranslatedString());
		
		Composite sectionClient = toolkit.createComposite(section);
		toolkit.paintBordersFor(sectionClient);
		
		section.setClient(sectionClient);
		sectionClient.setLayout(new GridLayout(1, false));
		
		capexTree = toolkit.createTree(sectionClient, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		initExpensesTree(capexTree, sectionClient);
    }
    
    /**
     * 
     * @param parent
     */
    private void createOtherExpensesSection(Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		WidgetHelper.grabBoth(section);
		section.setText(ExpenseType.OTHER.getTranslatedString());
		
		Composite sectionClient = toolkit.createComposite(section);
		section.setClient(sectionClient);
		sectionClient.setLayout(new GridLayout(1, false));
		
		otherExpensesTree = toolkit.createTree(sectionClient, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		initExpensesTree(otherExpensesTree, sectionClient);
    }
    
    /**
     * 
     * @param tree
     * @param parent
     */
    private void initExpensesTree(Tree tree, Composite parent) {
		GridDataFactory.fillDefaults().grab(true, true).hint(600, 100).applyTo(tree);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		createTreeColumn(tree, SWT.LEFT, Messages.labelCategory, 150);
		createTreeColumn(tree, SWT.LEFT, Messages.labelDescription, 150);
		createTreeColumn(tree, SWT.RIGHT, Messages.labelNet, 100);
		createTreeColumn(tree, SWT.RIGHT, Messages.labelTaxes, 100);
		createTreeColumn(tree, SWT.RIGHT, Messages.labelGross, 100);    	
    }
    
	/**
	 * {@inheritDoc}.
	 * @see de.tfsw.accounting.ui.reports.AbstractReportDialog#getToolkit()
	 */
	@Override
	protected FormToolkit getToolkit() {
		return toolkit;
	}
	
	/**
	 * 
	 * @param revenue
	 */
	private void updateRevenueTree(Revenue revenue) {
		revenueTree.removeAll();
		TreeItem headerItem = new TreeItem(revenueTree, SWT.NONE);
		headerItem.setText(
				new String[]{
						Messages.labelTotals, 
						Constants.EMPTY_STRING, 
						FormatUtil.formatCurrency(revenue.getRevenueNet()),
						FormatUtil.formatCurrency(revenue.getRevenueTax()),
						FormatUtil.formatCurrency(revenue.getRevenueGross())
						}
		);
		
		for (Invoice invoice : revenue.getInvoices()) {
			TreeItem invoiceItem = new TreeItem(headerItem, SWT.NONE);
			Price invoiceRevenue = revenue.getInvoiceRevenues().get(invoice);
			invoiceItem.setText(
					new String[]{
							invoice.getNumber(), 
							FormatUtil.formatDate(invoice.getInvoiceDate()), 
							FormatUtil.formatCurrency(invoiceRevenue.getNet()),
							FormatUtil.formatCurrency(invoiceRevenue.getTax()),
							FormatUtil.formatCurrency(invoiceRevenue.getGross())
							}
			);
		}
		
		headerItem.setExpanded(true);
	}
	
	/**
	 * 
	 * @param tree
	 * @param expenses
	 * @param expenseCategoryTotals
	 */
	private void updateExpenseTree(Tree tree, ExpenseCollection expenses, Map<String, Price> expenseCategoryTotals) {
		tree.removeAll();
		
		if (expenses == null) {
			return;
		}
		
		TreeItem headerItem = new TreeItem(tree, SWT.NONE);
		headerItem.setText(
				new String[]{
						Messages.labelTotals, 
						Constants.EMPTY_STRING, 
						FormatUtil.formatCurrency(expenses.getTotalCost().getNet()),
						FormatUtil.formatCurrency(expenses.getTotalCost().getTax()),
						FormatUtil.formatCurrency(expenses.getTotalCost().getGross())
						}
		);
		
		for (String category : expenseCategoryTotals.keySet()) {
			Price total = expenseCategoryTotals.get(category);
			TreeItem catHeader = new TreeItem(headerItem, SWT.NONE);
			catHeader.setText(new String[]{
					category != null ? category : Constants.HYPHEN, 
					Constants.EMPTY_STRING, 
					FormatUtil.formatCurrency(total.getNet()),
					FormatUtil.formatCurrency(total.getTax()),
					FormatUtil.formatCurrency(total.getGross())}
			);
			catHeader.setExpanded(true);
			
			for (Expense expense : expenses.getExpenses()) {
				if (isSameCategory(category, expense)) {
					Price price = CalculationUtil.calculatePrice(expense);
					TreeItem expenseItem = new TreeItem(catHeader, SWT.NONE);
					expenseItem.setText(new String[]{
							Constants.EMPTY_STRING, 
							expense.getDescription(), 
							FormatUtil.formatCurrency(expense.getNetAmount()),
							FormatUtil.formatCurrency(price.getTax()),
							FormatUtil.formatCurrency(price.getGross())}
					);
				}
			}
		}
		
		headerItem.setExpanded(true);
	}
	
	/**
	 * 
	 * @param category
	 * @param expense
	 * @return
	 */
	private boolean isSameCategory(String category, Expense expense) {
		return ((category == null && expense.getCategory() == null) || category != null && category.equals(expense.getCategory()));
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 * @see de.tfsw.accounting.ui.reports.AbstractReportDialog#updateModel()
	 */
	@Override
	protected void updateModel() {
		incomeStatement = AccountingUI.getAccountingService().getIncomeStatement(getTimeFrame());
		updateRevenueTree(incomeStatement.getRevenue());
		
		updateExpenseTree(opexTree, incomeStatement.getOperatingExpenses(), incomeStatement.getOperatingExpenseCategories());
		
		final Price ebitda = incomeStatement.getGrossProfit();
		setCurrencyValue(grossProfitNet, ebitda.getNet());
		setCurrencyValue(grossProfitTax, ebitda.getTax());
		setCurrencyValue(grossProfitGross, ebitda.getGross());
		
		updateExpenseTree(capexTree, incomeStatement.getCapitalExpenses(), incomeStatement.getCapitalExpenseCategories());
		updateExpenseTree(otherExpensesTree, incomeStatement.getOtherExpenses(), incomeStatement.getOtherExpenseCategories());
	}

    /**
     * 
     * @param parent
     * @param style
     * @param label
     * @param width
     * @return
     */
    private TreeColumn createTreeColumn(Tree parent, int style, String label, int width) {
    	TreeColumn col = new TreeColumn(parent, style);
    	col.setText(label);
    	col.setWidth(width);
    	return col;
    }
	
	/**
	 * {@inheritDoc}.
	 * @see de.tfsw.accounting.ui.reports.AbstractReportDialog#handleExport()
	 */
	@Override
	protected void handleExport() {
		ReportGenerationUtil.executeReportGeneration(new IncomeStatementGenerationHandler(), getShell());
	}

	/**
	 * 
	 * @author thorsten
	 *
	 */
	private class IncomeStatementGenerationHandler implements ReportGenerationHandler {

		/**
		 * 
		 * {@inheritDoc}
		 * @see de.tfsw.accounting.ui.reports.ReportGenerationHandler#getTargetFileNameSuggestion()
		 */
		@Override
		public String getTargetFileNameSuggestion() {
			return ReportGenerationUtil.appendTimeFrameToFileNameSuggestion(
					Messages.IncomeStatementDialog_fileNameSuggestion, incomeStatement.getTimeFrame());
		}

		/**
         * {@inheritDoc}.
         * @see de.tfsw.accounting.ui.reports.ReportGenerationHandler#getModelObject()
         */
        @Override
        public Object getModelObject() {
	        return incomeStatement;
        }

		/**
         * {@inheritDoc}.
         * @see de.tfsw.accounting.ui.reports.ReportGenerationHandler#getReportId()
         */
        @Override
        public String getReportId() {
	        return "de.tfsw.accounting.reporting.IncomeStatement";
        }		
	}
}
