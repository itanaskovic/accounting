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
package de.tfsw.accounting.service;

import java.util.HashSet;
import java.util.Set;

import com.db4o.query.Predicate;

import de.tfsw.accounting.AccountingContext;
import de.tfsw.accounting.model.Invoice;
import de.tfsw.accounting.model.InvoiceState;
import de.tfsw.accounting.model.User;
import de.tfsw.accounting.util.TimeFrame;

/**
 * A predicate to search for invoices belonging to a user.
 * 
 * @author thorsten
 *
 */
class FindInvoicesPredicate extends Predicate<Invoice> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1990476393693145937L;

	/**
	 * The context for this predicate.
	 */
	private AccountingContext context;
	
	/**
	 * All invoice states to match - may be <code>null</code> or empty.
	 */
	private Set<InvoiceState> states;
	
	private TimeFrame timeFrame;
	
	/**
	 * Creates a new predicate.
	 * 
	 * @param user the {@link User} whose invoices are matched
	 * @param states all invoice states to match - may be <code>null</code>
	 */
	FindInvoicesPredicate(AccountingContext context, InvoiceState... states) {
		this.context = context;
		if (states != null) {
			this.states = new HashSet<InvoiceState>();
			for (InvoiceState state : states) {
				this.states.add(state);
			}
		}
	}
	
	/**
	 * @param context
	 * @param timeFrame
	 * @param states
	 */
	FindInvoicesPredicate(AccountingContext context, TimeFrame timeFrame, InvoiceState... states) {
		this.context = context;
		this.timeFrame = timeFrame;
		if (states != null) {
			this.states = new HashSet<InvoiceState>();
			for (InvoiceState state : states) {
				this.states.add(state);
			}
		}
	}



	/**
	 * {@inheritDoc}
	 * @see com.db4o.query.Predicate#match(java.lang.Object)
	 */
	@Override
	public boolean match(Invoice invoice) {
		if (invoice.getUser() == null) {
			return false;
		}
		
		if (context.getUserName().equals(invoice.getUser().getName())) {
			if (timeFrame != null && invoice.getInvoiceDate() != null && !timeFrame.isInTimeFrame(invoice.getInvoiceDate())) {
				return false;
			}
			if (states == null || states.isEmpty()) {
				return true;
			} else {
				return states.contains(invoice.getState());
			}
		}
		
		return false;
	}
}