package assertions;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import models.Attachment;

import org.fest.assertions.ListAssert;

import com.google.common.base.Function;

public class AttachmentsAssert extends ListAssert {

	public AttachmentsAssert(List<Attachment> actual) {
		super(actual);
	}

	public AttachmentsAssert containsExactly(String... fileNames) {
		List<String> actualFileNames = newArrayList(transform((List<Attachment>) actual, new Function<Attachment, String>() {

			@Override
			public String apply(Attachment attachment) {
				return attachment.getFileName();
			}

		}));
		assertThat(actualFileNames).containsOnly((Object[]) fileNames);
		return this;
	}
}
