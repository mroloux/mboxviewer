package models;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.io.IOUtils.toByteArray;

import java.io.ByteArrayInputStream;
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

	private List<Message> emails = newArrayList();

	public Mailbox(InputStream input) {
		try {
			byte[] inputAsBytes = toByteArray(input);
			if (inputAsBytes.length == 0) {
				return;
			}
			getMessages(inputAsBytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void getMessages(byte[] inputAsBytes) {
		try {
			int startIndexOfMessage = 0;
			boolean previousMessageMarkerFound = false;
			for (int i = 0; i < inputAsBytes.length - 5; ++i) {
				if (messageMarkerAt(inputAsBytes, i)) {
					if (previousMessageMarkerFound) {
						emails.add(new CustomMessage(new ByteArrayInputStream(inputAsBytes, startIndexOfMessage, i - startIndexOfMessage)));
					}
					previousMessageMarkerFound = true;
					startIndexOfMessage = i;
				}
			}
			emails.add(new CustomMessage(new ByteArrayInputStream(inputAsBytes, startIndexOfMessage, inputAsBytes.length - startIndexOfMessage)));
		} catch (MimeIOException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean messageMarkerAt(byte[] inputAsBytes, int i) {
		String extractedString = new String(inputAsBytes, i, 5);
		return isMessagePrefix(extractedString) && (startOfMessage(i) || startOfLine(inputAsBytes, i));
	}

	private boolean startOfLine(byte[] inputAsBytes, int i) {
		return inputAsBytes[i - 1] == '\n';
	}

	private boolean startOfMessage(int i) {
		return i == 0;
	}

	protected boolean isMessagePrefix(String extractedString) {
		return extractedString.toLowerCase().startsWith("from ");
	}

	public List<Message> getEmails() {
		return emails;
	}

}
