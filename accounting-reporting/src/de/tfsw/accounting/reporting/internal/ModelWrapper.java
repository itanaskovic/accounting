/*
 *  Copyright 2013 , 2014 Thorsten Frank (accounting@tfsw.de).
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
package de.tfsw.accounting.reporting.internal;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tfsw.accounting.Constants;
import de.tfsw.accounting.model.Expense;
import de.tfsw.accounting.model.Invoice;
import de.tfsw.accounting.model.InvoicePosition;
import de.tfsw.accounting.model.Price;
import de.tfsw.accounting.util.CalculationUtil;
import de.tfsw.accounting.util.FormatUtil;

/**
 * @author thorsten
 *
 */
public class ModelWrapper {
	
	public static final String CALCULATED_TOTAL = "CALCULATED_TOTAL"; //$NON-NLS-1$
	
	protected static final String DOT = "."; //$NON-NLS-1$

	private static final String GET = "get"; //$NON-NLS-1$
	
	private static final Logger LOG = LogManager.getLogger(ModelWrapper.class);
	
	private Object model;
	
	private Price calculatedTotal;
	
    /**
     * 
     * @param model
     */
    protected ModelWrapper(Object model) {
    	setModel(model);
    }
    
	/**
     * @param model the model to set
     */
    protected void setModel(Object model) {
    	this.model = model;
    	
    	if (model == null) {
    		return; // don't do anything further...
    	}
    	
    	if (model instanceof InvoicePosition) {
    		calculatedTotal = CalculationUtil.calculatePrice((InvoicePosition) model);
    	} else if (model instanceof Invoice) {
    		calculatedTotal = CalculationUtil.calculateTotalPrice((Invoice) model);
    	} else if (model instanceof Expense) {
    		calculatedTotal = CalculationUtil.calculatePrice((Expense) model);
    	}
    }
    
    /**
     * 
     * @return
     */
    protected Object getModel() {
    	return model;
    }
    
    /**
     * 
     * @param property
     * @return
     */
    public String get(String property) {
    	
    	try {    		
	        Object result = get(model, property);
	        
	        if (result != null) {
	        	if (result instanceof String) {
	        		return (String) result;
	        	} else if (result instanceof BigDecimal) {
	        		return FormatUtil.formatDecimalValue((BigDecimal) result);
	        	}
	        	
	        	return result.toString();
	        }
        } catch (Exception e) {
        	LOG.error(String.format("Error reading property [%s] from model of type [%s]", property, model.getClass().getName()), e);
        }
    	
    	return null;
    }
    
    /**
     * 
     * @param property
     * @param defaultValue
     * @return
     */
    public String get(String property, String defaultValue) {
    	String result = get(property);
    	return result != null ? result : defaultValue;
    }
    
    /**
     * 
     * @param property
     * @return
     */
    public Object getRaw(String property) {
    	try {
			return get(model, property);
		} catch (Exception e) {
			LOG.error(String.format("Error reading raw property [%s] from model of type [%s]", property, model.getClass().getName()), e);
		}
    	
    	return null;
    }
    
    /**
     * 
     * @param msgKey
     * @param propertyToBind
     * @return
     */
    public String bind(String msgKey, String propertyToBind) {
    	String msg = getMessage(msgKey);
    	String binding = get(propertyToBind);
    	if (msg != null) {
    		return binding != null ? Messages.bind(msg, binding) : msg;
    	}
    
    	return null;
    }
    
    /**
     * 
     * @param msgKey
     * @param propertyToBind
     * @return
     */
    public String bindAsDate(String msgKey, String propertyToBind) {
    	return Messages.bind(getMessage(msgKey), formatAsDate(propertyToBind));
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public String getMessage(String key) {
    	try {
	        return (String) Messages.class.getField(key).get(new String());
        } catch (NoSuchFieldException e) {
	        LOG.error(String.format("Message key [%s] doesn't exist", key), e); //$NON-NLS-1$
        } catch (Exception e) {
        	LOG.error("Error accessing message key " + key, e); //$NON-NLS-1$
        }
    	
    	// if there is no message under the supplied key, return the key with notifiers
    	return "!" + key + "!";
    }
    
    /**
     * 
     * @param property
     * @return
     */
    public String formatAsDate(String property) {
    	try {
	        Object result = get(model, property);
	        
	        if (result != null && result instanceof LocalDate) {
	        	return FormatUtil.formatDate((LocalDate) result);
	        }
        } catch (Exception e) {
        	LOG.error(String.format("Error parsing date from property [%s]", property), e); //$NON-NLS-1$
        }
    	
    	return null;
    }
    
    /**
     * 
     * @param property
     * @param pattern
     * @return
     */
    public String formatAsDate(String property, String pattern) {
    	try {
			Object result = get(model, property);
			
			if (result != null && result instanceof LocalDate) {
				return ((LocalDate) result).format(DateTimeFormatter.ofPattern(pattern));
			}
		} catch (Exception e) {
			LOG.error(String.format("Error parsing date from property [%s] with pattern [%s]", property, pattern), e); //$NON-NLS-1$
		}
    	
    	return null;
    }
    
    /**
     * This is a shortcut for calling <code>formatAsCurrency(property, Constants.HYPHEN)}
     * 
     * @param property
     * @return
     */
    public String formatAsCurrency(String property) {
    	return formatAsCurrency(property, Constants.HYPHEN);
    }
    
    /**
     * 
     * @param property
     * @return
     */
    public String formatAsCurrency(String property, String defaultIfNull) {
    	try {
	        Object result = get(model, property);
	        
	        if (result != null && result instanceof BigDecimal) {
	        	return FormatUtil.formatCurrency((BigDecimal) result);
	        }
        } catch (Exception e) {
        	LOG.error(String.format("Error parsing currency from property [%s]", property), e); //$NON-NLS-1$
        }
    	
    	return defaultIfNull;    	
    }
    
    /**
     * 
     * @param property
     * @param defaultIfNull
     * @return
     */
    public String formatAsPercentage(String property, String defaultIfNull) {
    	try {
	        Object result = get(model, property);
	        
	        if (result != null && result instanceof BigDecimal) {
	        	return FormatUtil.formatPercentValue((BigDecimal) result);
	        }
        } catch (Exception e) {
        	LOG.error(String.format("Error parsing percentage from property [%s]", property), e); //$NON-NLS-1$
        }
    	
    	return defaultIfNull;
    }
    
    /**
     * Builds a {@link JRDataSource} for the value of the supplied property, where the 
     * {@link JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)} will always return a {@link ModelWrapper}
     * instance.
     * 
     * <p>
     * If the property's type is {@link Map}, the data source will contain instances of type {@link KeyValueModelWrapper}.
     * </p>
     * 
     * @param property
     * 
     * @return a {@link JRDataSource} containing 0 to n {@link ModelWrapper} objects
     */
    public JRDataSource getAsDataSource(String property) {
    	try {
	        Object result = get(model, property);
	        List<ModelWrapper> wrapped = new ArrayList<ModelWrapper>();
	        
	        if (result == null) {
	        	LOG.warn(String.format("Property [%s] returned null, will return empty data source!", property)); //$NON-NLS-1$
	        } else if (result instanceof Collection) {
	        	for (Object source : (Collection<?>) result) {
	        		wrapped.add(new ModelWrapper(source));
	        	}
	        } else if (result instanceof Map) {
	        	Map<?,?> map = (Map<?,?>) result;
	        	for (Object key : map.keySet()) {
	        		wrapped.add(new KeyValueModelWrapper(key, map.get(key)));
	        	}
	        } else {
	        	wrapped.add(new ModelWrapper(result));
	        }
	        
	        return new ModelWrapperDataSource(wrapped);
        } catch (Exception e) {
	        LOG.error("Could not build data source", e);
        }
    	
    	return null;
    }
    
    /**
     * 
     * @param property
     * @return
     */
    public ModelWrapper getWrapped(String property) {
    	Object target = getRaw(property);
    	return target != null ? new ModelWrapper(target) : null;
    }
    
    /**
     * 
     * @param object
     * @param property
     * @return
     * @throws Exception
     */
    protected Object get(Object object, String property) throws Exception {
    	String children = null;
    	
    	if (property.startsWith(CALCULATED_TOTAL)) {
    		property = property.substring(property.indexOf(DOT) + 1);
    		object = calculatedTotal;
    	}
    	
    	if (property.contains(DOT)) {
    		children = property.substring(property.indexOf(DOT) + 1);
    		property = property.substring(0, property.indexOf(DOT));
    	}
    	
    	final String readMethodName = GET + property.substring(0, 1).toUpperCase() + property.substring(1);
    	LOG.debug("Looking for accessor method " + readMethodName); //$NON-NLS-1$
    	
    	Method readMethod = object.getClass().getMethod(readMethodName);
    	    	    	
    	Object result = readMethod.invoke(object);
    	
    	if (children != null && result != null) {
    		return get(result, children);
    	}
    	
    	return result;
    }
}

