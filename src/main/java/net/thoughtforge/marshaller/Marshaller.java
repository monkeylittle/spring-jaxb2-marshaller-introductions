package net.thoughtforge.marshaller;

public interface Marshaller {

	String marshal(Object object);

	Object unmarshal(String string);
}
