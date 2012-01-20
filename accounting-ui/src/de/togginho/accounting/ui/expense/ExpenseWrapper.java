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
package de.togginho.accounting.ui.expense;

import java.math.BigDecimal;
import java.util.Date;

import de.togginho.accounting.Constants;
import de.togginho.accounting.model.Expense;
import de.togginho.accounting.model.Price;
import de.togginho.accounting.util.CalculationUtil;
import de.togginho.accounting.util.FormatUtil;

/**
 * @author thorsten
 *
 */
class ExpenseWrapper {

	private Expense expense;

	private Price price;
	
	ExpenseWrapper(Expense expense) {
		this.expense = expense;
		this.price = CalculationUtil.calculatePrice(expense);
	}
	
	/**
	 * 
	 * @return
	 */
	protected Expense getExpense() {
		return expense;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getPaymentDateFormatted() {
		return FormatUtil.formatDate(expense.getPaymentDate());
	}
	
	/**
	 * @return
	 * @see de.togginho.accounting.model.Expense#getPaymentDate()
	 */
	protected Date getPaymentDate() {
		return expense.getPaymentDate();
	}
	
	/**
	 * @return
	 * @see de.togginho.accounting.model.Expense#getNetAmount()
	 */
	protected BigDecimal getNetAmount() {
		return expense.getNetAmount();
	}
	
	/**
	 * @return
	 * @see de.togginho.accounting.model.Expense#getDescription()
	 */
	protected String getDescription() {
		return expense.getDescription();
	}

	/**
	 * 
	 * @return
	 */
	protected String getNetAmountFormatted() {
		return FormatUtil.formatCurrency(price.getNet());
	}
	
	protected String getTaxRateFormatted() {
		return expense.getTaxRate() != null ? expense.getTaxRate().toShortString() : Constants.HYPHEN;
	}
	
	protected BigDecimal getTaxAmount() {
		return price.getTax();
	}
	
	protected String getTaxAmountFormatted() {
		return price.getTax() != null ? FormatUtil.formatCurrency(price.getTax()) : Constants.HYPHEN;
	}
	
	protected BigDecimal getGrossAmount() {
		return price.getGross();
	}
	
	protected String getGrossAmountFormatted() {
		return FormatUtil.formatCurrency(price.getGross());
	}
}
