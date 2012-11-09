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
package de.togginho.accounting.ui;


/**
 * A listener that is called whenever changes to a model object are persisted using the 
 * {@link de.togginho.accounting.AccountingService} provided by the bundle activator {@link AccountingUI}. 
 * Recipients may register and unregister using {@link AccountingUI#addModelChangeListener(ModelChangeListener)} and
 * {@link AccountingUI#removeModelChangeListener(ModelChangeListener)}.
 * 
 * @author thorsten
 *
 */
public interface ModelChangeListener {

	/**
	 * Notifies of persistent changes to the model through the accounting service.
	 */
	void modelChanged();

}
