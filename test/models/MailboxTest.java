package models;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class MailboxTest {

	@Test(expected = IllegalStateException.class)
	public void throwsExceptionWhenInputFileNotValid() throws IOException {
		assertThat(new Mailbox(toInputStream("I AM NOT VALID")).getEmails()).isEmpty();
	}

	@Test
	public void containsNoEmailsWhenInputFileIsEmpty() throws IOException {
		assertThat(new Mailbox(toInputStream("")).getEmails()).isEmpty();
	}

	@Test
	public void containsAnEmailIfInputFileContainsOneMessage() throws IOException {
		Email expected = new EmailBuilder().withFrom("joske@test.be").build();
		List<Email> actual = new Mailbox(toInputStream("From: joske@test.be")).getEmails();
		assertThat(actual).containsExactly(expected);
	}

	@Test
	public void containsAListOfEmails() throws IOException {
		Email expectedMail1 = new EmailBuilder().withFrom("joske@test.be").build();
		Email expectedMail2 = new EmailBuilder().withFrom("pierre@test.be").build();
		Email expectedMail3 = new EmailBuilder().withFrom("armand@test.be").build();
		List<Email> actual = new Mailbox(toInputStream("From xxx\nFrom: joske@test.be\n\nFrom xxx\nFrom: pierre@test.be\n\nFrom xxx\nFrom: armand@test.be")).getEmails();
		assertThat(actual).containsExactly(expectedMail1, expectedMail2, expectedMail3);
	}

}
