Accounting Product
(c) 2015 Thorsten Frank (accounting@tfsw.de)

Contains the Eclipse RCP product definition.

# Notes

1) Since switching to a feature-based product, the following plugins are missing from the previous config:

<plugin id="org.eclipse.ui.forms"/>

Manually added the plugin to the accounting-feature

Also missing, but that hasn't caused any problems so far:

<plugin id="org.eclipse.core.net.linux.x86" fragment="true"/>
<plugin id="org.eclipse.core.net.linux.x86_64" fragment="true"/>
<plugin id="org.eclipse.core.net.win32.x86" fragment="true"/>
<plugin id="org.eclipse.core.net.win32.x86_64" fragment="true"/>

<plugin id="org.eclipse.core.runtime.compatibility.registry" fragment="true"/>

<plugin id="org.eclipse.equinox.security.macosx" fragment="true"/>
<plugin id="org.eclipse.equinox.security.win32.x86" fragment="true"/>
<plugin id="org.eclipse.equinox.security.win32.x86_64" fragment="true"/>

<plugin id="org.eclipse.osgi.util"/>

2) Since the introduction of the accounting-api project, and placing the service interfaces there, the product config 
needs to include an auto-start configuration for the accounting-core bundle, otherwise the service impl never gets
created and cannot be injected into the UI parts.