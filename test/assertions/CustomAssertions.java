package assertions;

import java.util.List;

import models.Attachment;

import org.apache.james.mime4j.message.Message;
import org.fest.assertions.Assertions;

public class CustomAssertions extends Assertions {

	public static MessagesAssert assertThat(List<Message> actual) {
		return new MessagesAssert(actual);
	}

	public static AttachmentsAssert assertThat(List<Attachment> actual) {
		return new AttachmentsAssert(actual);
	}
}
