package net.thoughtforge.marshaller;

import java.math.BigDecimal;
import java.util.Calendar;

import net.thoughtforge.model.Person;

import org.apache.commons.lang.text.StrBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext/applicationContext-*.xml"})
public class Jaxb2MarshallerTest {

	private static final String MARSHALLED_PERSON =
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Person xmlns=\"http://thoughtforge.net/model\"><dateOfBirth>1965-01-01T00:00:00Z</dateOfBirth><firstName>Joe</firstName><height>1.85</height><lastName>Bloggs</lastName><weight>12.2</weight></Person>";
	
	private static Calendar dateOfBirth;
	
	private static String firstName;
	
	private static BigDecimal height;
	
	private static String lastName;
	
	private static BigDecimal weight;
	
	@Autowired
	@Qualifier(value="marshaller")
	private Jaxb2Marshaller marshaller;

	@BeforeClass
	public static void beforeClass() {
		dateOfBirth = Calendar.getInstance();
		dateOfBirth.clear();
		dateOfBirth.set(Calendar.DATE, 1);
		dateOfBirth.set(Calendar.MONTH, Calendar.JANUARY);
		dateOfBirth.set(Calendar.YEAR, 1965);
		
		firstName = "Joe";
		height = new BigDecimal("1.85");
		lastName = "Bloggs";
		weight = new BigDecimal("12.2");
	}
	
	@Test
	public void marshallPerson() {
		Person person = new Person();
		person.setDateOfBirth(dateOfBirth);
		person.setFirstName(firstName);
		person.setHeight(height);
		person.setLastName(lastName);
		person.setWeight(weight);
		
		String xml = marshaller.marshal(person);
		
		Assert.assertNotNull(xml);
		Assert.assertEquals(MARSHALLED_PERSON, xml);
	}

	@Test
	public void marshallPersonInvalidFirstName() {
		Person person = new Person();
		person.setDateOfBirth(dateOfBirth);
		person.setFirstName(new StrBuilder(firstName).appendPadding(50, '0').toString());
		person.setHeight(height);
		person.setLastName(lastName);
		person.setWeight(weight);
		
		try {
			marshaller.marshal(person);
			Assert.fail("First name length restriction not applied.");
		} catch (MarshallingFailureException marshallingFailureException) {
			Throwable rootCause = marshallingFailureException.getRootCause();
			Assert.assertFalse(rootCause.getMessage().indexOf("is not facet-valid with respect to maxLength '50'") == -1);
		}
	}
	
	@Test
	public void marshallPersonInvalidHeight() {
		Person person = new Person();
		person.setDateOfBirth(dateOfBirth);
		person.setFirstName(firstName);
		person.setHeight(height.add(new BigDecimal("0.1111")));
		person.setLastName(lastName);
		person.setWeight(weight);
		
		try {
			marshaller.marshal(person);
			Assert.fail("Height precision restriction not applied.");
		} catch (MarshallingFailureException marshallingFailureException) {
			Throwable rootCause = marshallingFailureException.getRootCause();
			Assert.assertFalse(rootCause.getMessage().indexOf("the number of fraction digits has been limited to 2") == -1);
		}
	}
	
	@Test
	public void marshallPersonInvalidLastName() {
		Person person = new Person();
		person.setDateOfBirth(dateOfBirth);
		person.setFirstName(firstName);
		person.setHeight(height);
		person.setLastName(new StrBuilder(lastName).appendPadding(50, '0').toString());
		person.setWeight(weight);
		
		try {
			marshaller.marshal(person);
			Assert.fail("First name length restriction not applied.");
		} catch (MarshallingFailureException marshallingFailureException) {
			Throwable rootCause = marshallingFailureException.getRootCause();
			Assert.assertFalse(rootCause.getMessage().indexOf("is not facet-valid with respect to maxLength '50'") == -1);
		}
	}
	
	@Test
	public void marshallPersonInvalidWeight() {
		Person person = new Person();
		person.setDateOfBirth(dateOfBirth);
		person.setFirstName(firstName);
		person.setHeight(height);
		person.setLastName(lastName);
		person.setWeight(weight.add(new BigDecimal("0.1111")));
		
		try {
			marshaller.marshal(person);
			Assert.fail("Weight precision restriction not applied.");
		} catch (MarshallingFailureException marshallingFailureException) {
			Throwable rootCause = marshallingFailureException.getRootCause();
			Assert.assertFalse(rootCause.getMessage().indexOf("the number of fraction digits has been limited to 2") == -1);
		}
	}
	@Test
	public void unmarshallPerson() {
		Person person = (Person) marshaller.unmarshal(MARSHALLED_PERSON);
		
		Assert.assertNotNull(person);
		Assert.assertTrue(dateOfBirth.compareTo(person.getDateOfBirth()) == 0);
		Assert.assertEquals(firstName, person.getFirstName());
		Assert.assertEquals(height, person.getHeight());
		Assert.assertEquals(lastName, person.getLastName());
		Assert.assertEquals(weight, person.getWeight());
	}
}
