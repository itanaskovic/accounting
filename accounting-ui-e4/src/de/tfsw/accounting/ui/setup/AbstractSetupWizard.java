/*
 *  Copyright 2018 Thorsten Frank (accounting@tfsw.de).
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
package de.tfsw.accounting.ui.setup;

import java.util.function.Function;

import org.eclipse.jface.wizard.Wizard;

import de.tfsw.accounting.AccountingContext;
import de.tfsw.accounting.AccountingInitService;

/**
 * Simple base class for setup wizards.
 */
abstract class AbstractSetupWizard extends Wizard {

	private Messages messages;
	private boolean canFinish;
	private AccountingContext context;
	
	@Override
	public boolean canFinish() {
		return canFinish;
	}

	void setCanFinish(boolean canFinish) {
		this.canFinish = canFinish;
		getContainer().updateButtons();
	}
	
	Function<AccountingInitService, AccountingContext> buildFunctionForWhenServiceComesOnline() {
		return service -> {
			service.init(context);
			return context;
		};
	}
	
	AccountingContext getAccountingContext() {
		return context;
	}
	
	void setAccountingContext(AccountingContext context) {
		this.context = context;
	}

	/**
	 * @return the messages
	 */
	Messages getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	void setMessages(Messages messages) {
		this.messages = messages;
		messagesAvailable();
	}
	
	void messagesAvailable() {
		// default impl does nothing
	}
}
