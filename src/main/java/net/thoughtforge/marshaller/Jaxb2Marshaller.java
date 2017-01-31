package net.thoughtforge.marshaller;

import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringSource;

@Component(value="marshaller")
public class Jaxb2Marshaller implements Marshaller {

	@Autowired
	@Qualifier(value="jaxb2Marshaller")
	private org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller;
	
	public String marshal(Object object) {
		final StringWriter out = new StringWriter();
		marshaller.marshal(object, new StreamResult(out));
		return out.toString();
	}

	public Object unmarshal(String string) {
		return marshaller.unmarshal(new StringSource(string));
	}
}
