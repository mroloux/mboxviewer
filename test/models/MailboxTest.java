package models;

import static assertions.CustomAssertions.assertThat;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.fest.assertions.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.james.mime4j.message.Message;
import org.junit.Test;

import ext.MessageExtensions;

public class MailboxTest {

	@Test
	public void containsNoEmailsWhenInputFileIsEmpty() throws IOException {
		assertThat(new Mailbox(toInputStream("")).getEmails()).isEmpty();
	}

	@Test
	public void containsAnEmailIfInputFileContainsOneMessageWithoutMessageMarker() throws IOException {
		List<Message> actual = new Mailbox(toInputStream("Subject: my mail")).getEmails();
		assertThat(actual).containsExactly("my mail");
	}

	@Test
	public void containsAnEmailIfInputFileContainsOneMessage() throws IOException {
		List<Message> actual = new Mailbox(toInputStream("From xxx\nSubject: my mail")).getEmails();
		assertThat(actual).containsExactly("my mail");
	}

	@Test
	public void handlesEmailWithMessageMarkerInBody() throws IOException {
		List<Message> actual = new Mailbox(toInputStream("From xxx\nSubject: my mail\nthis is the contents blabla From blabla")).getEmails();
		assertThat(actual).containsExactly("my mail");
	}

	@Test
	public void containsAListOfEmails() throws IOException {
		List<Message> actual = new Mailbox(toInputStream("From xxx\nSubject: my first mail\n\nFrom xxx\nSubject: my second mail\n\nFrom xxx\nSubject: my third mail")).getEmails();
		assertThat(actual).containsExactly("my first mail", "my second mail", "my third mail");
	}

	@Test
	public void handlesISO88591Characters() throws UnsupportedEncodingException {
		List<Message> actual = new Mailbox(new ByteArrayInputStream("From xxx\nContent-Type: text/html; charset=iso-8859-1\nSubject: my mail\n\nthé content".getBytes("ISO-8859-1"))).getEmails();
		assertThat(MessageExtensions.bodyAsString(getOnlyElement(actual))).isEqualTo("thé content");
	}

	@Test
	public void handlesUTF8Characters() throws UnsupportedEncodingException {
		List<Message> actual = new Mailbox(new ByteArrayInputStream("From xxx\nContent-Type: text/html; charset=utf-8\nSubject: my mail\n\nthé content".getBytes("UTF-8"))).getEmails();
		assertThat(MessageExtensions.bodyAsString(getOnlyElement(actual))).isEqualTo("thé content");
	}

}
