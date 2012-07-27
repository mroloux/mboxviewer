package models;

import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class EmailBuilder {

	private String from;

	public Email build() {
		Email email = new Email();
		setInternalState(email, "from", from);
		return email;
	}

	public EmailBuilder withFrom(String from) {
		this.from = from;
		return this;
	}

}
