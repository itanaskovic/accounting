
package de.tfsw.accounting.io.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for xmlExpense complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xmlExpense">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expenseType" type="{}xmlExpenseType" minOccurs="0"/>
 *         &lt;element name="netAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="paymentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="taxRate" type="{}xmlTaxRate" minOccurs="0"/>
 *         &lt;element name="category" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="depreciationMethod" type="{}xmlDepreciationMethod" minOccurs="0"/>
 *         &lt;element name="depreciationPeriodInYears" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="salvageValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlExpense", propOrder = {
    "id",
    "description",
    "expenseType",
    "netAmount",
    "paymentDate",
    "taxRate",
    "category",
    "depreciationMethod",
    "depreciationPeriodInYears",
    "salvageValue"
})
public class XmlExpense {

    protected String id;
    @XmlElement(required = true)
    protected String description;
    @XmlSchemaType(name = "string")
    protected XmlExpenseType expenseType;
    @XmlElement(required = true)
    protected BigDecimal netAmount;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar paymentDate;
    protected XmlTaxRate taxRate;
    protected String category;
    @XmlSchemaType(name = "string")
    protected XmlDepreciationMethod depreciationMethod;
    protected Integer depreciationPeriodInYears;
    protected BigDecimal salvageValue;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
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
     * Gets the value of the expenseType property.
     * 
     * @return
     *     possible object is
     *     {@link XmlExpenseType }
     *     
     */
    public XmlExpenseType getExpenseType() {
        return expenseType;
    }

    /**
     * Sets the value of the expenseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlExpenseType }
     *     
     */
    public void setExpenseType(XmlExpenseType value) {
        this.expenseType = value;
    }

    /**
     * Gets the value of the netAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetAmount() {
        return netAmount;
    }

    /**
     * Sets the value of the netAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetAmount(BigDecimal value) {
        this.netAmount = value;
    }

    /**
     * Gets the value of the paymentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaymentDate() {
        return paymentDate;
    }

    /**
     * Sets the value of the paymentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaymentDate(XMLGregorianCalendar value) {
        this.paymentDate = value;
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
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the depreciationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link XmlDepreciationMethod }
     *     
     */
    public XmlDepreciationMethod getDepreciationMethod() {
        return depreciationMethod;
    }

    /**
     * Sets the value of the depreciationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlDepreciationMethod }
     *     
     */
    public void setDepreciationMethod(XmlDepreciationMethod value) {
        this.depreciationMethod = value;
    }

    /**
     * Gets the value of the depreciationPeriodInYears property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDepreciationPeriodInYears() {
        return depreciationPeriodInYears;
    }

    /**
     * Sets the value of the depreciationPeriodInYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDepreciationPeriodInYears(Integer value) {
        this.depreciationPeriodInYears = value;
    }

    /**
     * Gets the value of the salvageValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSalvageValue() {
        return salvageValue;
    }

    /**
     * Sets the value of the salvageValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSalvageValue(BigDecimal value) {
        this.salvageValue = value;
    }

}
