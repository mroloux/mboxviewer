package models;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.james.mime4j.field.ContentDispositionField.DISPOSITION_TYPE_ATTACHMENT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.SingleBody;

public class CustomMessage extends Message {

	public CustomMessage(InputStream is) throws MimeIOException, IOException {
		super(is);
	}

	public CustomMessage() {
	}

	public List<Attachment> getAttachments() {
		return getAttachments(this);
	}

	private List<Attachment> getAttachments(Entity entity) {
		List<Attachment> attachments = newArrayList();
		if (entity.isMultipart()) {
			Multipart multipart = (Multipart) entity.getBody();
			for (BodyPart bodyPart : multipart.getBodyParts()) {
				attachments.addAll(getAttachments(bodyPart));
			}
		} else if (isAttachment(entity)) {
			attachments.add(createAttachment(entity));
		}
		return attachments;
	}

	private boolean isAttachment(Entity entity) {
		return DISPOSITION_TYPE_ATTACHMENT.equals(entity.getDispositionType());
	}

	private Attachment createAttachment(Entity attachmentEntity) {
		try {
			return new Attachment(attachmentEntity.getFilename(), getBodySizeInBytes(attachmentEntity));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private long getBodySizeInBytes(Entity attachmentEntity) throws IOException {
		SingleBody body = (SingleBody) attachmentEntity.getBody();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		body.writeTo(outputStream);
		return outputStream.toByteArray().length;
	}
}
