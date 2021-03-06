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
package de.tfsw.accounting.io;

import java.time.LocalDate;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tfsw.accounting.io.xml.XmlAddress;
import de.tfsw.accounting.io.xml.XmlBankAccount;
import de.tfsw.accounting.io.xml.XmlClient;
import de.tfsw.accounting.io.xml.XmlClients;
import de.tfsw.accounting.io.xml.XmlDepreciationMethod;
import de.tfsw.accounting.io.xml.XmlExpense;
import de.tfsw.accounting.io.xml.XmlExpenseTemplate;
import de.tfsw.accounting.io.xml.XmlExpenseTemplates;
import de.tfsw.accounting.io.xml.XmlExpenseType;
import de.tfsw.accounting.io.xml.XmlExpenses;
import de.tfsw.accounting.io.xml.XmlFrequency;
import de.tfsw.accounting.io.xml.XmlInvoice;
import de.tfsw.accounting.io.xml.XmlInvoicePosition;
import de.tfsw.accounting.io.xml.XmlInvoicePositions;
import de.tfsw.accounting.io.xml.XmlInvoices;
import de.tfsw.accounting.io.xml.XmlPaymentTerms;
import de.tfsw.accounting.io.xml.XmlPaymentType;
import de.tfsw.accounting.io.xml.XmlRecurrenceRule;
import de.tfsw.accounting.io.xml.XmlTaxRate;
import de.tfsw.accounting.io.xml.XmlTaxRates;
import de.tfsw.accounting.io.xml.XmlUser;
import de.tfsw.accounting.model.Address;
import de.tfsw.accounting.model.BankAccount;
import de.tfsw.accounting.model.Client;
import de.tfsw.accounting.model.Expense;
import de.tfsw.accounting.model.ExpenseTemplate;
import de.tfsw.accounting.model.Invoice;
import de.tfsw.accounting.model.InvoicePosition;
import de.tfsw.accounting.model.PaymentTerms;
import de.tfsw.accounting.model.RecurrenceRule;
import de.tfsw.accounting.model.TaxRate;
import de.tfsw.accounting.model.User;

/**
 * @author thorsten
 *
 */
class ModelToXml {

	/**
	 * 
	 */
	private static final Logger LOG = LogManager.getLogger(AccountingXmlImportExport.class);
	
	/**
	 * 
	 */
	private XmlUser xmlUser;
	
	/**
	 * 
	 */
	protected ModelToXml() {
		// nothing to do here...
	}

	/**
	 * 
	 * @param user
	 * @param invoices
	 * @return
	 */
	protected XmlUser convertToXml(XmlModelDTO model) {
		User user = model.getUser();
		
		LOG.info("Exporting User to XML: " + user.getName()); //$NON-NLS-1$
		
		xmlUser = new XmlUser();
		xmlUser.setName(user.getName());
		xmlUser.setTaxId(user.getTaxNumber());
		xmlUser.setDescription(user.getDescription());
		
		convertBankAccount(user);
		
		if (user.getAddress() != null) {
			xmlUser.setAddress(convertAddress(user.getAddress()));
		}
		
		convertClients(model.getClients());
		
		convertTaxRates(user);
		
		if (model.getInvoices() != null && !model.getInvoices().isEmpty()) {
			LOG.debug("Converting invoices..."); //$NON-NLS-1$
        	XmlInvoices xmlInvoices = new XmlInvoices();
        	xmlUser.setInvoices(xmlInvoices);
        	
        	for (Invoice invoice : model.getInvoices()) {
        		xmlInvoices.getInvoice().add(convertInvoice(invoice));
        	}
		} else {
			LOG.debug("No invoices to convert!"); //$NON-NLS-1$
		}
		
		convertExpenses(model.getExpenses());
		
		convertExpenseTemplates(model.getExpenseTemplates());
		
		return xmlUser;
	}

	/**
	 * 
	 * @param user
	 */
	private void convertBankAccount(User user) {
	    if (user.getBankAccount() != null) {
	    	LOG.debug("Converting bank account"); //$NON-NLS-1$
			BankAccount account = user.getBankAccount();
			XmlBankAccount xmlBankAccount = new XmlBankAccount();
			xmlUser.setBankAccount(xmlBankAccount);
			
			xmlBankAccount.setAccountNumber(account.getAccountNumber());
			xmlBankAccount.setBankCode(account.getBankCode());
			xmlBankAccount.setBankName(account.getBankName());
			xmlBankAccount.setBic(account.getBic());
			xmlBankAccount.setIban(account.getIban());
		}
    }

	/**
	 * 
	 * @param user
	 */
	private void convertTaxRates(User user) {
	    if (user.getTaxRates() != null && !user.getTaxRates().isEmpty()) {
			XmlTaxRates xmlTaxRates = new XmlTaxRates();
			xmlUser.setTaxRates(xmlTaxRates);
			
			for (TaxRate taxRate : user.getTaxRates()) {
				xmlTaxRates.getTaxRate().add(converTaxRate(taxRate));
			}
		}
    }

	/**
	 * 
	 * @param user
	 */
	private void convertClients(Set<Client> clients) {
	    if (clients != null && !clients.isEmpty()) {
	    	LOG.debug("Converting clients..."); //$NON-NLS-1$
			XmlClients xmlClients = new XmlClients();
			xmlUser.setClients(xmlClients);
			for (Client client : clients) {
				LOG.debug("Converting client " + client.getName()); //$NON-NLS-1$
				XmlClient xmlClient = new XmlClient();
				xmlClient.setName(client.getName());
				xmlClient.setClientNumber(client.getClientNumber());
				xmlClient.setAddress(convertAddress(client.getAddress()));
				xmlClient.setDefaultPaymentTerms(convertPaymentTerms(client.getDefaultPaymentTerms()));
				xmlClients.getClient().add(xmlClient);
			}
		} else {
			LOG.debug("No clients to convert..."); //$NON-NLS-1$
		}
    }
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	private XmlAddress convertAddress(Address address) {
		if (LOG.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("Converting address "); //$NON-NLS-1$
			if (address.getRecipientDetail() != null) {
				sb.append(address.getRecipientDetail()).append(" | ");
			}
			sb.append(address.getStreet());
			sb.append(" | "); //$NON-NLS-1$
			sb.append(address.getPostalCode());
			sb.append(" | "); //$NON-NLS-1$
			sb.append(address.getCity());
			LOG.debug(sb.toString());
		}
		
		XmlAddress xmlAddress = new XmlAddress();
		xmlAddress.setRecipientDetail(address.getRecipientDetail());
		xmlAddress.setCity(address.getCity());
		xmlAddress.setEmail(address.getEmail());
		xmlAddress.setFax(address.getFaxNumber());
		xmlAddress.setMobile(address.getMobileNumber());
		xmlAddress.setPhone(address.getPhoneNumber());
		xmlAddress.setPostalCode(address.getPostalCode());
		xmlAddress.setStreet(address.getStreet());
		return xmlAddress;
	}
	
	/**
	 * 
	 * @param taxRate
	 * @return
	 */
	private XmlTaxRate converTaxRate(TaxRate taxRate) {
		if (taxRate == null) {
			return null;
		}
		LOG.debug("Converting tax rate " + taxRate.toLongString()); //$NON-NLS-1$
		XmlTaxRate xmlTaxRate = new XmlTaxRate();
		xmlTaxRate.setAbbreviation(taxRate.getShortName());
		xmlTaxRate.setName(taxRate.getLongName());
		xmlTaxRate.setRate(taxRate.getRate());
		xmlTaxRate.setIsVat(taxRate.getIsVAT());
		return xmlTaxRate;
	}
	
	/**
	 * 
	 * @param invoice
	 * @return
	 */
	private XmlInvoice convertInvoice(Invoice invoice) {
		LOG.debug("Converting invoice " + invoice.getNumber()); //$NON-NLS-1$
		XmlInvoice xmlInvoice = new XmlInvoice();
		xmlInvoice.setNumber(invoice.getNumber());
		
		xmlInvoice.setCancelledDate(convertDate(invoice.getCancelledDate()));
		xmlInvoice.setClient(invoice.getClient().getName());
		xmlInvoice.setCreationDate(convertDate(invoice.getCreationDate()));
		// xmlInvoice.setDueDate(convertDate(invoice.getDueDate()));
		xmlInvoice.setInvoiceDate(convertDate(invoice.getInvoiceDate()));		
		xmlInvoice.setPaymentDate(convertDate(invoice.getPaymentDate()));
		xmlInvoice.setSentDate(convertDate(invoice.getSentDate()));
		xmlInvoice.setPaymentTerms(convertPaymentTerms(invoice.getPaymentTerms()));
		
		if (invoice.getInvoicePositions() != null) {
			XmlInvoicePositions xmlInvoicePositions = new XmlInvoicePositions();
			xmlInvoice.setInvoicePositions(xmlInvoicePositions);
			for (InvoicePosition invoicePosition : invoice.getInvoicePositions()) {
				XmlInvoicePosition xmlInvoicePosition = new XmlInvoicePosition();
				xmlInvoicePositions.getInvoicePosition().add(xmlInvoicePosition);
				
				xmlInvoicePosition.setDescription(invoicePosition.getDescription());
				xmlInvoicePosition.setPricePerUnit(invoicePosition.getPricePerUnit());
				xmlInvoicePosition.setQuantity(invoicePosition.getQuantity());
				if (invoicePosition.isTaxApplicable()) {
					xmlInvoicePosition.setTaxRate(converTaxRate(invoicePosition.getTaxRate()));
				}
				xmlInvoicePosition.setUnit(invoicePosition.getUnit());
				xmlInvoicePosition.setRevenueRelevant(invoicePosition.isRevenueRelevant());
			}
		}
		
		return xmlInvoice;
	}
	
	/**
	 * 
	 * @param expenses
	 */
	private void convertExpenses(Set<Expense> expenses) {
		if (expenses != null && expenses.size() > 0) {
			XmlExpenses xmlExpenses = new XmlExpenses();
			for (Expense expense : expenses) {
				XmlExpense xmlExpense = new XmlExpense();
				xmlExpense.setDescription(expense.getDescription());
				if (expense.getExpenseType() != null) {
					xmlExpense.setExpenseType(XmlExpenseType.fromValue(expense.getExpenseType().name()));
				}
				xmlExpense.setNetAmount(expense.getNetAmount());
				xmlExpense.setPaymentDate(convertDate(expense.getPaymentDate()));
				xmlExpense.setTaxRate(converTaxRate(expense.getTaxRate()));
				xmlExpense.setCategory(expense.getCategory());
				if (expense.getDepreciationMethod() != null) {
					xmlExpense.setDepreciationMethod(XmlDepreciationMethod.fromValue(expense.getDepreciationMethod().name()));
				}
				
				xmlExpense.setDepreciationPeriodInYears(expense.getDepreciationPeriodInYears());
				xmlExpense.setSalvageValue(expense.getSalvageValue());
				xmlExpenses.getExpense().add(xmlExpense);
			}
			
			xmlUser.setExpenses(xmlExpenses);
		}
	}
	
	/**
	 * 
	 * @param templates
	 */
	private void convertExpenseTemplates(Set<ExpenseTemplate> templates) {
		if (templates != null && templates.size() > 0) {
			XmlExpenseTemplates xmlTemplates = new XmlExpenseTemplates();
			for (ExpenseTemplate template : templates) {
				XmlExpenseTemplate xmlTemplate = new XmlExpenseTemplate();
				xmlTemplates.getTemplate().add(xmlTemplate);
				xmlTemplate.setActive(template.isActive());
				xmlTemplate.setCategory(template.getCategory());
				xmlTemplate.setDescription(template.getDescription());
				if (template.getExpenseType() != null) {
					xmlTemplate.setExpenseType(XmlExpenseType.fromValue(template.getExpenseType().name()));
				}
				xmlTemplate.setFirstApplication(convertDate(template.getFirstApplication()));
				xmlTemplate.setLastApplication(convertDate(template.getLastApplication()));
				xmlTemplate.setNetAmount(template.getNetAmount());
				xmlTemplate.setNumberOfApplications(template.getNumberOfApplications());
				xmlTemplate.setRule(convertRecurrenceRule(template.getRule()));
				xmlTemplate.setTaxRate(converTaxRate(template.getTaxRate()));
			}
			xmlUser.setExpenseTemplates(xmlTemplates);
		}
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	private XmlRecurrenceRule convertRecurrenceRule(RecurrenceRule rule) {
		XmlRecurrenceRule xmlRule = null;
		
		if (rule != null) {
			xmlRule = new XmlRecurrenceRule();
			if (rule.getCount() != null) {
				xmlRule.setCount(rule.getCount());
			}
			xmlRule.setFrequency(XmlFrequency.fromValue(rule.getFrequency().name()));
			xmlRule.setInterval(rule.getInterval());
			if (rule.getUntil() != null) {
				xmlRule.setUntil(convertDate(rule.getUntil()));
			}
		}
		
		return xmlRule;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private XMLGregorianCalendar convertDate(LocalDate date) {
		if (date == null) {
			return null;
		}

		try {
	        XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
	        xmlCal.setDay(date.getDayOfMonth());
	        xmlCal.setMonth(date.getMonthValue());
	        xmlCal.setYear(date.getYear());
	        return xmlCal;
        } catch (DatatypeConfigurationException e) {
        	LOG.error("Could not convert date to XML", e); //$NON-NLS-1$
        }
		
		return null;
	}
	
	/**
	 * 
	 * @param paymentTerms
	 * @return
	 */
	private XmlPaymentTerms convertPaymentTerms(PaymentTerms paymentTerms) {
		if (paymentTerms == null) {
			paymentTerms = PaymentTerms.getDefault();
		}
		XmlPaymentTerms xmlTerms = new XmlPaymentTerms();
		xmlTerms.setFullPaymentTargetInDays(paymentTerms.getFullPaymentTargetInDays());
		xmlTerms.setType(XmlPaymentType.NET);
		return xmlTerms;
	}
}
