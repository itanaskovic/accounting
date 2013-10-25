/*
 *  Copyright 2012 thorsten frank (thorsten.frank@gmx.de).
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
package de.togginho.accounting.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import de.togginho.accounting.model.DepreciationMethod;
import de.togginho.accounting.model.Expense;
import de.togginho.accounting.model.ExpenseType;
import de.togginho.accounting.xml.generated.XmlExpense;
import de.togginho.accounting.xml.generated.XmlUser;

/**
 * @author thorsten
 *
 */
public class XmlToModelTest extends XmlTestBase {

	/** */
	private static final String TEST_FILE_NAME = "XmlMappingTestFile.xml";
	
	/**
	 * Test method for {@link ModelMapper#xmlToModel(String)}.
	 */
	@Test
	public void testModelMapper() {
		ImportResult result = ModelMapper.xmlToModel(TEST_FILE_NAME);
		assertNotNull(result);
		assertNotNull(result.getImportedUser());
		assertNotNull(result.getImportedClients());
		assertNotNull(result.getImportedInvoices());
		assertNotNull(result.getImportedExpenses());
	}
	
	/**
	 * Test method for {@link XmlToModel#convert(XmlUser)}.
	 */
	@Test
	public void testConvert() {
        try {
			Unmarshaller unmarshaller = JAXBContext.newInstance(ModelMapper.JAXB_CONTEXT).createUnmarshaller();
			final XmlUser xmlUser = (XmlUser) unmarshaller.unmarshal(new File(TEST_FILE_NAME));
			// USER
			assertUserSame(getTestUser(), xmlUser);
			// CLIENTS
			assertNotNull(xmlUser.getClients());
			assertEquals(1, xmlUser.getClients().getClient().size());
			assertClientsSame(getTestClient(), xmlUser.getClients().getClient().get(0));
			// INVOICES
			assertNotNull(xmlUser.getInvoices());
			assertEquals(1, xmlUser.getInvoices().getInvoice().size());
			assertInvoicesSame(createInvoice(), xmlUser.getInvoices().getInvoice().get(0));
			// EXPENSES
			assertNotNull(xmlUser.getExpenses());
			assertEquals(3, xmlUser.getExpenses().getExpense().size());
			for (XmlExpense xmlExpense : xmlUser.getExpenses().getExpense()) {
				switch (xmlExpense.getExpenseType()) {
				case OPEX:
					assertExpensesSame(createExpense(ExpenseType.OPEX, true), xmlExpense);
					break;
				case CAPEX:
					Expense capex = createExpense(ExpenseType.CAPEX, true);
					capex.setCategory("CapexCategory");
					capex.setDescription("CapexDescription");
					capex.setNetAmount(new BigDecimal("100"));
					capex.setDepreciationMethod(DepreciationMethod.STRAIGHTLINE);
					capex.setDepreciationPeriodInYears(3);
					capex.setSalvageValue(BigDecimal.ONE);
					assertExpensesSame(capex, xmlExpense);
					break;
				case OTHER:
					assertExpensesSame(createExpense(ExpenseType.OTHER, false), xmlExpense);
					break;
				}
			}
		} catch (JAXBException e) {
			fail(e.toString());
		}
	}
}
