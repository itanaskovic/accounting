/*
 *  Copyright 2015 Thorsten Frank (accounting@tfsw.de).
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
package de.tfsw.accounting.elster;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.YearMonth;


/**
 * Transfer object that acts as a bridge between the core accounting model and the XML interface model used for
 * generating messages to the German electronic VAT registration system.
 * 
 * <p>
 * There are several differences between the application and the interface model, such as a more fine-grained
 * address format used by the interface, and also the fact that some of the revenue and tax amounts need to be
 * rounded and/or scaled down. In addition, the German tax numbering scheme differs slightly in regards to what is
 * used in human and machine communication.
 * </p>
 * 
 * <p>
 * Clients aquire an instance of this class via {@link ElsterService#getElsterDTO(java.time.YearMonth)}, which will
 * be pre-filled with data based on the application model for the supplied period. Clients may change data before
 * generating an XML document, but need to consider a few things:
 * 
 * <ul>
 * <li>Changes made within this object do not reflect back onto the main application model.</li>
 * <li>Monetary amounts have been calculated based on the supplied time period and adapted to the specification of
 * the German Revenue Service, so any changes are at your own risk!</li>
 * <li>the time frame cannot be changed - when a new period is needed, clients must either retrieve a new DTO instance 
 * ({@link ElsterService#getElsterDTO(java.time.YearMonth)}) or adapt an existing one to a new period
 * ({@link ElsterService#adaptToPeriod(ElsterDTO, java.time.YearMonth)})</li>
 * </ul>
 * </p>
 * 
 * @author Thorsten Frank
 */
public abstract class ElsterDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Property change listener utility.
	 */
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	private Bundesland finanzAmtBL;
	private String creationDate;
	private String timeFrameYear;
	private String timeFrameMonth;
	private String companyName;
	private String userFirstName;
	private String userLastName;
	private String companyTaxNumberOrig;
	private String companyTaxNumberGenerated;
	private String companyPhone;
	private String companyEmail;
	private String companyStreetName;
	private String companyStreetNumber;
	private String companyStreetAddendum;
	private String companyPostCode;
	private String companyCity;
	private String companyCountry;
	private BigDecimal revenue19;
	private BigDecimal revenue19tax;
	private BigDecimal revenue7;
	private BigDecimal revenue7tax;
	private BigDecimal inputTax;
	private BigDecimal taxSum;

	/**
	 * Returns the filing period this DTO was built for.
	 * 
	 * <p>
	 * This class does intentionally not define a setter method for this property, because clients must not change
	 * the period directly, and subclasses may need to react to period changes.
	 * </p>
	 * 
	 * @return	the filing period
	 */
	public abstract YearMonth getFilingPeriod();
	
	/**
	 * Subclasses are notified whenever {@link #setFinanzAmtBL(Bundesland)} or {@link #setCompanyTaxNumberOrig(String)}
	 * is called.
	 *  
	 * @return
	 */
	protected abstract String generateTaxNumber();
	
	/**
	 * @return the finanzAmtBL
	 */
	public Bundesland getFinanzAmtBL() {
		return finanzAmtBL;
	}
	
	/**
	 * @param finanzAmtBL the finanzAmtBL to set
	 */
	public void setFinanzAmtBL(Bundesland finanzAmtBL) {
		final Bundesland oldValue = this.finanzAmtBL;
		this.finanzAmtBL = finanzAmtBL;
		firePropertyChange("finanzAmtBL", oldValue, finanzAmtBL);
		setCompanyTaxNumberGenerated();
	}
	
	/**
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		final String oldValue = this.creationDate;
		this.creationDate = creationDate;
		firePropertyChange("creationDate", oldValue, creationDate);
	}

	/**
	 * @return the timeFrameYear
	 */
	public String getTimeFrameYear() {
		return timeFrameYear;
	}

	/**
	 * @param timeFrameYear the timeFrameYear to set
	 */
	public void setTimeFrameYear(String timeFrameYear) {
		final String oldValue = this.timeFrameYear;
		this.timeFrameYear = timeFrameYear;
		firePropertyChange("timeFrameYear", oldValue, timeFrameYear);
	}

	/**
	 * @return the timeFrameMonth
	 */
	public String getTimeFrameMonth() {
		return timeFrameMonth;
	}

	/**
	 * @param timeFrameMonth the timeFrameMonth to set
	 */
	public void setTimeFrameMonth(String timeFrameMonth) {
		final String oldValue = this.timeFrameMonth;
		this.timeFrameMonth = timeFrameMonth;
		firePropertyChange("timeFrameMonth", oldValue, timeFrameMonth);
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		final String oldValue = this.companyName;
		this.companyName = companyName;
		firePropertyChange("companyName", oldValue, companyName);
	}

	/**
	 * @return the userFirstName
	 */
	public String getUserFirstName() {
		return userFirstName;
	}

	/**
	 * @param userFirstName the userFirstName to set
	 */
	public void setUserFirstName(String userFirstName) {
		final String oldValue = this.userFirstName;
		this.userFirstName = userFirstName;
		firePropertyChange("userFirstName", oldValue, userFirstName);
	}

	/**
	 * @return the userLastName
	 */
	public String getUserLastName() {
		return userLastName;
	}

	/**
	 * @param userLastName the userLastName to set
	 */
	public void setUserLastName(String userLastName) {
		final String oldValue = this.userLastName;
		this.userLastName = userLastName;
		firePropertyChange("userLastName", oldValue, userLastName);
	}

	/**
	 * @return the companyTaxNumberOrig
	 */
	public String getCompanyTaxNumberOrig() {
		return companyTaxNumberOrig;
	}

	/**
	 * @param companyTaxNumberOrig the companyTaxNumberOrig to set
	 */
	public void setCompanyTaxNumberOrig(String companyTaxNumberOrig) {
		final String oldValue = this.companyTaxNumberOrig;
		this.companyTaxNumberOrig = companyTaxNumberOrig;
		firePropertyChange("companyTaxNumberOrig", oldValue, companyTaxNumberOrig);
		setCompanyTaxNumberGenerated();
	}

	/**
	 * @return the companyTaxNumberGenerated
	 */
	public String getCompanyTaxNumberGenerated() {
		return companyTaxNumberGenerated;
	}
	
	/**
	 * 
	 * @param companyTaxNumberGenerated
	 */
	protected void setCompanyTaxNumberGenerated() {
		final String oldValue = this.companyTaxNumberGenerated;
		this.companyTaxNumberGenerated = generateTaxNumber();
		firePropertyChange("companyTaxNumberGenerated", oldValue, companyTaxNumberGenerated);
	}
	
	/**
	 * @return the companyPhone
	 */
	public String getCompanyPhone() {
		return companyPhone;
	}

	/**
	 * @param companyPhone the companyPhone to set
	 */
	public void setCompanyPhone(String companyPhone) {
		final String oldValue = this.companyPhone;
		this.companyPhone = companyPhone;
		firePropertyChange("companyPhone", oldValue, companyPhone);
	}

	/**
	 * @return the companyEmail
	 */
	public String getCompanyEmail() {
		return companyEmail;
	}

	/**
	 * @param companyEmail the companyEmail to set
	 */
	public void setCompanyEmail(String companyEmail) {
		final String oldValue = this.companyEmail;
		this.companyEmail = companyEmail;
		firePropertyChange("companyEmail", oldValue, companyEmail);
	}

	/**
	 * @return the companyStreetName
	 */
	public String getCompanyStreetName() {
		return companyStreetName;
	}

	/**
	 * @param companyStreetName the companyStreetName to set
	 */
	public void setCompanyStreetName(String companyStreetName) {
		final String oldValue = this.companyStreetName;
		this.companyStreetName = companyStreetName;
		firePropertyChange("companyStreetName", oldValue, companyStreetName);
	}

	/**
	 * @return the companyStreetNumber
	 */
	public String getCompanyStreetNumber() {
		return companyStreetNumber;
	}

	/**
	 * @param companyStreetNumber the companyStreetNumber to set
	 */
	public void setCompanyStreetNumber(String companyStreetNumber) {
		final String oldValue = this.companyStreetNumber;
		this.companyStreetNumber = companyStreetNumber;
		firePropertyChange("companyStreetNumber", oldValue, companyStreetNumber);
	}

	/**
	 * @return the companyStreetAddendum
	 */
	public String getCompanyStreetAddendum() {
		return companyStreetAddendum;
	}

	/**
	 * @param companyStreetAddendum the companyStreetAddendum to set
	 */
	public void setCompanyStreetAddendum(String companyStreetAddendum) {
		final String oldValue = this.companyStreetAddendum;
		this.companyStreetAddendum = companyStreetAddendum;
		firePropertyChange("companyStreetAddendum", oldValue, companyStreetAddendum);
	}

	/**
	 * @return the companyPostCode
	 */
	public String getCompanyPostCode() {
		return companyPostCode;
	}

	/**
	 * @param companyPostCode the companyPostCode to set
	 */
	public void setCompanyPostCode(String companyPostCode) {
		final String oldValue = this.companyPostCode;
		this.companyPostCode = companyPostCode;
		firePropertyChange("companyPostCode", oldValue, companyPostCode);
	}

	/**
	 * @return the companyCity
	 */
	public String getCompanyCity() {
		return companyCity;
	}

	/**
	 * @param companyCity the companyCity to set
	 */
	public void setCompanyCity(String companyCity) {
		final String oldValue = this.companyCity;
		this.companyCity = companyCity;
		firePropertyChange("companyCity", oldValue, companyCity);
	}

	/**
	 * @return the companyCountry
	 */
	public String getCompanyCountry() {
		return companyCountry;
	}

	/**
	 * @param companyCountry the companyCountry to set
	 */
	public void setCompanyCountry(String companyCountry) {
		final String oldValue = this.companyCountry;
		this.companyCountry = companyCountry;
		firePropertyChange("companyCountry", oldValue, companyCountry);
	}

	/**
	 * @return the revenue19
	 */
	public BigDecimal getRevenue19() {
		return revenue19;
	}

	/**
	 * @param revenue19 the revenue19 to set
	 */
	public void setRevenue19(BigDecimal revenue19) {
		final BigDecimal oldValue = this.revenue19;
		this.revenue19 = revenue19;
		firePropertyChange("revenue19", oldValue, revenue19);
	}

	/**
	 * @return the revenue19tax
	 */
	public BigDecimal getRevenue19tax() {
		return revenue19tax;
	}

	/**
	 * @param revenue19tax the revenue19tax to set
	 */
	public void setRevenue19tax(BigDecimal revenue19tax) {
		final BigDecimal oldValue = this.revenue19tax;
		this.revenue19tax = revenue19tax;
		firePropertyChange("revenue19tax", oldValue, revenue19tax);
	}

	/**
	 * @return the revenue7
	 */
	public BigDecimal getRevenue7() {
		return revenue7;
	}

	/**
	 * @param revenue7 the revenue7 to set
	 */
	public void setRevenue7(BigDecimal revenue7) {
		final BigDecimal oldValue = this.revenue7;
		this.revenue7 = revenue7;
		firePropertyChange("revenue7", oldValue, revenue7);
	}

	/**
	 * @return the revenue7tax
	 */
	public BigDecimal getRevenue7tax() {
		return revenue7tax;
	}

	/**
	 * @param revenue7tax the revenue7tax to set
	 */
	public void setRevenue7tax(BigDecimal revenue7tax) {
		final BigDecimal oldValue = this.revenue7tax;
		this.revenue7tax = revenue7tax;
		firePropertyChange("revenue7tax", oldValue, revenue7tax);
	}

	/**
	 * @return the inputTax
	 */
	public BigDecimal getInputTax() {
		return inputTax;
	}

	/**
	 * @param inputTax the inputTax to set
	 */
	public void setInputTax(BigDecimal inputTax) {
		final BigDecimal oldValue = this.inputTax;
		this.inputTax = inputTax;
		firePropertyChange("inputTax", oldValue, inputTax);
	}

	/**
	 * @return the taxSum
	 */
	public BigDecimal getTaxSum() {
		return taxSum;
	}

	/**
	 * @param taxSum the taxSum to set
	 */
	public void setTaxSum(BigDecimal taxSum) {
		final BigDecimal oldValue = this.taxSum;
		this.taxSum = taxSum;
		firePropertyChange("taxSum", oldValue, taxSum);
	}
	
	/**
	 * Delegate method.
	 * 
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Delegate method.
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	/**
	 * Delegate method.
	 * 
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	/**
	 * Delegate method.
	 * 
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
}
