package assertions;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Collections2.transform;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.apache.james.mime4j.message.Message;
import org.fest.assertions.ListAssert;

import com.google.common.base.Function;

public class MessageAssert extends ListAssert {

	public MessageAssert(List<Message> actual) {
		super(actual);
	}

	public MessageAssert containsExactly(String... subjects) {
		List<String> actualSubjects = newArrayList(transform((List<Message>) actual, new Function<Message, String>() {

			@Override
			public String apply(Message message) {
				return message.getSubject();
			}

		}));
		assertThat(actualSubjects).containsOnly((Object[]) subjects);
		return this;
	}
}
