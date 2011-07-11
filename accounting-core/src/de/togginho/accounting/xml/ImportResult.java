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
package de.togginho.accounting.xml;

import java.util.Set;

import de.togginho.accounting.model.Invoice;
import de.togginho.accounting.model.User;

/**
 * @author tfrank1
 *
 */
public class ImportResult {

	private User importedUser;
	
	private Set<Invoice> importedInvoices;

	/**
     * @return the importedUser
     */
    public User getImportedUser() {
    	return importedUser;
    }

	/**
     * @param importedUser the importedUser to set
     */
    void setImportedUser(User importedUser) {
    	this.importedUser = importedUser;
    }

	/**
     * @return the importedInvoices
     */
    public Set<Invoice> getImportedInvoices() {
    	return importedInvoices;
    }

	/**
     * @param importedInvoices the importedInvoices to set
     */
    void setImportedInvoices(Set<Invoice> importedInvoices) {
    	this.importedInvoices = importedInvoices;
    }
	
	
}
