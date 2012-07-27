package models;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.io.IOUtils.toInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.storage.DefaultStorageProvider;
import org.apache.james.mime4j.storage.MemoryStorageProvider;

public class Mailbox {

	static {
		DefaultStorageProvider.setInstance(new MemoryStorageProvider());
	}

	private List<Email> emails = newArrayList();

	public Mailbox(InputStream input) {
		for (Message message : getMessages(input)) {
			emails.add(new Email(message));
		}
	}

	private List<Message> getMessages(InputStream input) {
		try {
			List<Message> messages = newArrayList();
			StringBuilder currentMessage = new StringBuilder();
			for (String line : readLines(input)) {
				if (isMessageMarker(line)) {
					if (currentMessage.length() != 0) {
						messages.add(new Message(toInputStream(currentMessage.toString())));
					}
					currentMessage = new StringBuilder();
				} else {
					currentMessage.append(line);
				}
			}
			if (currentMessage.length() != 0) {
				messages.add(new Message(toInputStream(currentMessage.toString())));
			}
			return messages;
		} catch (MimeIOException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isMessageMarker(String line) {
		return line.toLowerCase().startsWith("from ");
	}

	public List<Email> getEmails() {
		return emails;
	}

}
