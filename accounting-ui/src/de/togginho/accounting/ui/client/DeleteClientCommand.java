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
package de.togginho.accounting.ui.client;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import de.togginho.accounting.model.Client;
import de.togginho.accounting.model.User;
import de.togginho.accounting.ui.IDs;
import de.togginho.accounting.ui.Messages;
import de.togginho.accounting.ui.ModelHelper;

/**
 * Deletes a client provided by the active {@link IWorkbenchPart}, or more precisely it's {@link ISelectionProvider}.
 * 
 * @author tfrank1
 */
public class DeleteClientCommand extends AbstractClientHandler {

	/** Logger. */
	private static final Logger LOG = Logger.getLogger(DeleteClientCommand.class);
	
	/**
	 * 
	 * {@inheritDoc}.
	 * @see AbstractClientHandler#handleClient(Client, ExecutionEvent)
	 */
	@Override
	protected void handleClient(Client client, ExecutionEvent event) throws ExecutionException {
		MessageBox messageBox = new MessageBox(getShell(event), SWT.ICON_WARNING | SWT.YES | SWT.NO);
		
		LOG.debug("Confirming deletion of client " + client.getName()); //$NON-NLS-1$
		
		messageBox.setMessage(Messages.bind(Messages.DeleteClientCommand_confirmMessage, client.getName()));
		messageBox.setText(Messages.DeleteClientCommand_confirmText);
		
		if (messageBox.open() == SWT.YES) {
			User currentUser = ModelHelper.getCurrentUser();
			
			LOG.debug(String.format("Removing client [%s] from user [%s]",  //$NON-NLS-1$
					client.getName(), currentUser.getName()));
			
			Iterator<Client> iter = currentUser.getClients().iterator();
			while (iter.hasNext()) {
				Client toBeDeleted = iter.next();
				if (toBeDeleted.equals(client)) {
					LOG.debug("Found client in user, now removing"); //$NON-NLS-1$
					iter.remove();
					break;
				}
			}
			
			// save the current user
			ModelHelper.saveCurrentUser();
			
			// close open editors for the deleted client, if open
			removeOpenEditorForClient(client, event);
		} else {
			LOG.debug(String.format("Deleting client [%s] was cancelled.", client.getName())); //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 * {@inheritDoc}.
	 * @see de.togginho.accounting.ui.AbstractAccountingHandler#getLogger()
	 */
	@Override
    protected Logger getLogger() {
	    return LOG;
    }
	
	/**
	 * @param deletedClient
	 */
	private void removeOpenEditorForClient(Client deletedClient, ExecutionEvent event) {
		LOG.debug("Checking for open editors for client " + deletedClient.getName()); //$NON-NLS-1$
		IWorkbenchPage page = getActivePage(event);
		
		for (IEditorReference editorRef : page.findEditors(null, IDs.EDIT_CLIENT_ID, IWorkbenchPage.MATCH_ID)) {
			if (editorRef.getName().equals(deletedClient.getName())) {
				LOG.debug("Closing editor for deleted client: " + editorRef.getName()); //$NON-NLS-1$
				page.closeEditor(editorRef.getEditor(false), false);
			}
		}		
	}
	
	
}