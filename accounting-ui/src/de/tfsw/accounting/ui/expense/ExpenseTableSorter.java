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
package de.tfsw.accounting.ui.expense;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tfsw.accounting.ui.AbstractTableSorter;

/**
 * 
 * @author thorsten
 *
 */
class ExpenseTableSorter extends AbstractTableSorter<ExpenseWrapper> {

	private static final Logger LOG = LogManager.getLogger(ExpenseTableSorter.class);
	
	protected static final int COL_INDEX_DATE = 0;
	protected static final int COL_INDEX_DESC = 1;
	protected static final int COL_INDEX_CATEGORY = 2;
	protected static final int COL_INDEX_NET = 3;
	protected static final int COL_INDEX_TAX = 4;
	protected static final int COL_INDEX_GROSS = 5;
	
	
	/**
	 * Creates a new sorter for the expenses view.
	 */
	protected ExpenseTableSorter() {
		super(ExpenseWrapper.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @see de.tfsw.accounting.ui.AbstractTableSorter#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	/**
	 * {@inheritDoc}
	 * @see AbstractTableSorter#doCompare(Object, Object, int)
	 */
	@Override
	protected int doCompare(ExpenseWrapper e1, ExpenseWrapper e2, int columnIndex) {
		switch (columnIndex) {
		case COL_INDEX_DATE:
			return e1.getPaymentDate().compareTo(e2.getPaymentDate());
		case COL_INDEX_DESC:
			return e1.getDescription().compareTo(e2.getDescription());
		case COL_INDEX_CATEGORY:
			if (e1.getCategory() != null) {
				return e2.getCategory() != null ? e1.getCategory().compareTo(e2.getCategory()) : 1;
			} else if (e2.getCategory() != null) {
				return e1.getCategory() != null ? e2.getCategory().compareTo(e1.getCategory()) : -1;
			} else {
				return 0;
			}
		case COL_INDEX_NET:
			return e1.getNetAmount().compareTo(e2.getNetAmount());
		case COL_INDEX_TAX:
			if (e1.getTaxAmount() != null) {
				return e2.getTaxAmount() != null ? e1.getTaxAmount().compareTo(e2.getTaxAmount()) : 1;
			} else if (e2.getTaxAmount() != null) {
				return e1.getTaxAmount() != null ? e2.getTaxAmount().compareTo(e1.getTaxAmount()) : -1;
			} else {
				return 0;
			}
		case COL_INDEX_GROSS:
			return e1.getGrossAmount().compareTo(e2.getGrossAmount());
		default:
			return 0;
		}
		
	}
}