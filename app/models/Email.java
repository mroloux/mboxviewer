package models;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.getOnlyElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.james.mime4j.message.Message;

public class Email {

	private String from;

	public Email(Message message) {
		validateMessage(message);
		from = getOnlyElement(message.getFrom()).getDisplayString();
	}

	private void validateMessage(Message message) {
		checkState(message.getFrom() != null);
	}

	protected Email() {
	}

	public String getFrom() {
		return from;
	}

	@Override
	public boolean equals(Object obj) {
		return new EqualsBuilder().reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
