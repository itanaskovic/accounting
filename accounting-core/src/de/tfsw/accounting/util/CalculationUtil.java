/*
 *  Copyright 2010 , 2014 Thorsten Frank (accounting@tfsw.de).
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
package de.tfsw.accounting.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tfsw.accounting.model.AnnualDepreciation;
import de.tfsw.accounting.model.Expense;
import de.tfsw.accounting.model.Invoice;
import de.tfsw.accounting.model.InvoicePosition;
import de.tfsw.accounting.model.Price;
import de.tfsw.accounting.model.TaxRate;
import de.tfsw.accounting.model.internal.DepreciationCalculatorFactory;

/**
 * Utility class for calculating prices of {@link InvoicePosition} and and entire {@link Invoice}.
 * 
 * @author thorsten frank
 */
public final class CalculationUtil {

    /**
     * 
     */
    private CalculationUtil() {
    	
    }
    
    /**
     * Calculates the total price of an invoice.
     * 
     * @param invoice the invoice to calculate
     * @return the total {@link Price} of the invoice
     */
    public static Price calculateTotalPrice(Invoice invoice) {
    	BigDecimal totalNet = BigDecimal.ZERO;
    	BigDecimal totalGross = BigDecimal.ZERO;
    	BigDecimal totalTax = BigDecimal.ZERO;
    	
    	if (invoice != null && invoice.getInvoicePositions() != null) {
        	for (InvoicePosition position : invoice.getInvoicePositions()) {
        		final Price singlePrice = calculatePrice(position);
        		totalNet = totalNet.add(singlePrice.getNet());
        		totalGross = totalGross.add(singlePrice.getGross());
        		if (singlePrice.getTax() != null) {
        			totalTax = totalTax.add(singlePrice.getTax());
        		}
        	}    		
    	}
    	
    	return new Price(totalNet, totalTax, totalGross);
    }
    
    /**
     * 
     * @param invoice
     * @return
     */
    public static Map<TaxRate, BigDecimal> calculateSubTotalsByTaxRate(Invoice invoice) {
    	final Map<TaxRate, BigDecimal> subtotals = new HashMap<TaxRate, BigDecimal>();
    	
    	if (invoice != null && invoice.getInvoicePositions() != null) {
    		for (InvoicePosition ip : invoice.getInvoicePositions()) {
    			final Price singlePrice = calculatePrice(ip);
    			BigDecimal subtotal = BigDecimal.ZERO;
    			if (subtotals.containsKey(ip.getTaxRate())) {
    				subtotal = subtotals.get(ip.getTaxRate());
    			}
    			subtotal = subtotal.add(singlePrice.getNet());
    			subtotals.put(ip.getTaxRate(), subtotal);
    		}
    	}
    	
    	
    	return subtotals;
    }
    
    /**
     * Calculates total revenue of an invoice. The revenue amount may differ from the price of an invoice in that it
     * only considers invoice positions that are revenue relevant, i.e. where 
     * {@link InvoicePosition#isRevenueRelevant()} returns <code>true</code>.
     * 
     * @param invoice
     * @return
     * 
     * @see #calculateTotalPrice(Invoice)
     */
    public static Price calculateRevenue(Invoice invoice) {
    	BigDecimal totalNet = BigDecimal.ZERO;
    	BigDecimal totalGross = BigDecimal.ZERO;
    	BigDecimal totalTax = BigDecimal.ZERO;
    	
    	if (invoice != null && invoice.getInvoicePositions() != null) {
        	for (InvoicePosition position : invoice.getInvoicePositions()) {
        		if (position.isRevenueRelevant()) {
            		final Price singlePrice = calculatePrice(position);
            		totalNet = totalNet.add(singlePrice.getNet());
            		totalGross = totalGross.add(singlePrice.getGross());
            		if (singlePrice.getTax() != null) {
            			totalTax = totalTax.add(singlePrice.getTax());
            		}        			
        		}
        	}    		
    	}
    	
    	return new Price(totalNet, totalTax, totalGross);    	
    }
    
    /**
     * Calculates the price of a single invoice position.
     * @param invoicePosition
     * @return
     */
    public static Price calculatePrice(InvoicePosition invoicePosition) {
    	BigDecimal net = BigDecimal.ZERO;
    	BigDecimal tax = null;
    	BigDecimal gross = BigDecimal.ZERO;
    	
    	if (invoicePosition != null) {
    		net = calculateNetPrice(invoicePosition);
        	if (invoicePosition.isTaxApplicable()) {
        		tax = net.multiply(invoicePosition.getTaxRate().getRate());
        		gross = net.add(tax);
        	} else {
        		gross = net;
        	}
    	}
    	
    	return new Price(net, tax, gross);
    }
    
    /**
     * Calculates the net price of a single invoice position by multiplying its quantity by the price per unit, i.e.
     * the price excluding taxes.
     *
     * @param invoicePosition   the invoice position to calculate
     *
     * @return  the net price of the invoice position, 
     * 			or {@link BigDecimal#ZERO} if either quantity or pricePerUnit is <code>null</code>
     */
    public static BigDecimal calculateNetPrice(InvoicePosition invoicePosition) {
    	if (invoicePosition.getQuantity() == null || invoicePosition.getPricePerUnit() == null) {
    		return BigDecimal.ZERO;
    	}
    	BigDecimal netPrice = invoicePosition.getQuantity().multiply(invoicePosition.getPricePerUnit());
    	return netPrice;
    }
    
    /**
     * 
     * @param expense
     * @return
     */
    public static Price calculatePrice(Expense expense) {
    	BigDecimal net = expense.getNetAmount() != null ? expense.getNetAmount() : BigDecimal.ZERO;
    	BigDecimal tax = null;
    	BigDecimal gross = BigDecimal.ZERO;
    	
    	if (expense.getTaxRate() == null) {
    		gross = net;
    	} else {
    		tax = net.multiply(expense.getTaxRate().getRate());
    		gross = net.add(tax);
    	}
    	
    	return new Price(net, tax, gross); 
    }

    /**
     * 
     * @param expense
     * @return
     */
    public static List<AnnualDepreciation> calculateDepreciationSchedule(Expense expense) {
    	return DepreciationCalculatorFactory.getDepreciationCalculator(expense).getDepreciationSchedule();
    }
}