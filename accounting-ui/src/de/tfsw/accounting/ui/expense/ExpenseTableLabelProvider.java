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

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import de.tfsw.accounting.Constants;

/**
 * @author thorsten
 *
 */
class ExpenseTableLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	/**
	 * {@inheritDoc}
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		ExpenseWrapper wrapper = (ExpenseWrapper) element;
		switch (columnIndex) {
		case ExpenseTableSorter.COL_INDEX_DATE:
			return wrapper.getPaymentDateFormatted();
		case ExpenseTableSorter.COL_INDEX_DESC:
			return wrapper.getDescription();
		case ExpenseTableSorter.COL_INDEX_CATEGORY:
			return wrapper.getCategory() != null ? wrapper.getCategory() : Constants.BLANK_STRING;
		case ExpenseTableSorter.COL_INDEX_NET:
			return wrapper.getNetAmountFormatted();
		case ExpenseTableSorter.COL_INDEX_TAX:
			return wrapper.getTaxAmountFormatted();
		case ExpenseTableSorter.COL_INDEX_GROSS:
			return wrapper.getGrossAmountFormatted();
		default:
			return Constants.HYPHEN;
		}
	}
}
