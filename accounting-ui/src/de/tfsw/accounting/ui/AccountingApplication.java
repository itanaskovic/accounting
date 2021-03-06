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
package de.tfsw.accounting.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class AccountingApplication implements IApplication {

	private static final Logger LOG = LogManager.getLogger(AccountingApplication.class);
	
	/**
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		LOG.debug("Starting Accounting Application");
		Display display = PlatformUI.createDisplay();
		try {
			return handlePlatformReturnCode(PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()));
		} finally {
			display.dispose();
		}
	}

	/**
	 * 
	 * @param returnCode
	 * @return
	 */
	private Integer handlePlatformReturnCode(int returnCode) {
		String returnString = null;
		Integer exitValue = IApplication.EXIT_OK;
		switch (returnCode) {
			case PlatformUI.RETURN_OK:
				returnString = "RETURN_OK";
				break;
			case PlatformUI.RETURN_RESTART:
				returnString = "RETURN_RESTART";
				exitValue = IApplication.EXIT_RESTART;
				break;
			case PlatformUI.RETURN_UNSTARTABLE:
				returnString = "RETURN_UNSTARTABLE";
				break;
			case PlatformUI.RETURN_EMERGENCY_CLOSE:
				returnString = "RETURN_EMERGENCY_CLOSE";
				break;
		}
		
		LOG.debug("ReturnCode from PlatformUI.createAndRunWorkbench() was " + returnString);
		
		return exitValue;
		
	}
	
	/**
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		LOG.debug("Stopping Accounting Application...");
		
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
