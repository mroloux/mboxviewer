package models;

import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Attachment {

	private final String fileName;
	private final long fileSizeBytes;

	public Attachment(String fileName, long fileSizeBytes) {
		this.fileName = fileName;
		this.fileSizeBytes = fileSizeBytes;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSizeBytes() {
		return fileSizeBytes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		return new EqualsBuilder().reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return reflectionToString(this);
	}
}
