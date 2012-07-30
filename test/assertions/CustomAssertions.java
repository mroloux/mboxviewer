package assertions;

import java.util.List;


import org.apache.james.mime4j.message.Message;
import org.fest.assertions.Assertions;

public class CustomAssertions extends Assertions {

	public static MessageAssert assertThat(List<Message> actual) {
		return new MessageAssert(actual);
	}
}
