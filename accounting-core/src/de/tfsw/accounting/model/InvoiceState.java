/*
 *  Copyright 2011, 2014 Thorsten Frank (accounting@tfsw.de).
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
package de.tfsw.accounting.model;

import java.io.Serializable;

import de.tfsw.accounting.Messages;

/**
 * The state of an invoice denoting that invoices current point in its life cycle.
 * 
 * <p>The state of an invoice is never set directly, but rather computed through a series of rules regarding its
 * various dates, such as the creation date, sent date, due date, cancelled date, and so on.</p>
 * 
 * @author Thorsten Frank
 * @see    Invoice#getState()
 */
public enum InvoiceState implements Serializable {

	/**
	 * An invoice that is fresh, i.e. has just been instantiated but never saved to persistence.
	 */
	UNSAVED,
	
	/**
	 * An invoice that has been saved to persistence, but not been sent.
	 */
	CREATED,
	
	/**
	 * An invoice that has been sent to its client.
	 */
	SENT,
	
	/**
	 * An invoice that was sent but has a due date in the past.
	 */
	OVERDUE,
	
	/**
	 * An invoice that has been sent and then cancelled. This is a terminal state.
	 */
	CANCELLED,
	
	/**
	 * An invoice that was sent and then paid, regardless of whether the payment was made within the legal time frame
	 * or after it has already become overdue. This is a terminal state.
	 */
	PAID;

	/**
	 * Returns a translated String describing this state. The locale used is the default locale derived from the
	 * framework.
	 * 
	 * @return a locale-sensitive string representation of this invoice state
	 */
	public String getTranslatedString() {
		return Messages.translate(this);
	}
}
