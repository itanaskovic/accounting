
package de.tfsw.accounting.io.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xmlInvoicePosition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xmlInvoicePosition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="pricePerUnit" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="taxRate" type="{}xmlTaxRate"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="revenueRelevant" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlInvoicePosition", propOrder = {
    "quantity",
    "pricePerUnit",
    "taxRate",
    "description",
    "unit",
    "revenueRelevant"
})
public class XmlInvoicePosition {

    @XmlElement(required = true)
    protected BigDecimal quantity;
    @XmlElement(required = true)
    protected BigDecimal pricePerUnit;
    @XmlElement(required = true)
    protected XmlTaxRate taxRate;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String unit;
    protected boolean revenueRelevant;

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the pricePerUnit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Sets the value of the pricePerUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPricePerUnit(BigDecimal value) {
        this.pricePerUnit = value;
    }

    /**
     * Gets the value of the taxRate property.
     * 
     * @return
     *     possible object is
     *     {@link XmlTaxRate }
     *     
     */
    public XmlTaxRate getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the value of the taxRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlTaxRate }
     *     
     */
    public void setTaxRate(XmlTaxRate value) {
        this.taxRate = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnit(String value) {
        this.unit = value;
    }

    /**
     * Gets the value of the revenueRelevant property.
     * 
     */
    public boolean isRevenueRelevant() {
        return revenueRelevant;
    }

    /**
     * Sets the value of the revenueRelevant property.
     * 
     */
    public void setRevenueRelevant(boolean value) {
        this.revenueRelevant = value;
    }

}
