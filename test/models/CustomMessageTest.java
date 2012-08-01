package models;

import static assertions.CustomAssertions.assertThat;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import mime4j.BodyPartBuilder;
import mime4j.MultipartBuilder;

import org.apache.james.mime4j.message.BodyFactory;
import org.apache.james.mime4j.message.BodyPart;
import org.junit.Test;

public class CustomMessageTest {

	@Test
	public void getAttachments_ReturnsAttachment() throws IOException {
		BodyPart bodyPart = new BodyPartBuilder()
				.withContentDisposition("attachment; filename=\"sample.pdf\"")
				.withBody(new BodyFactory().binaryBody(getClass().getResourceAsStream("/data/sample.pdf")))
				.build();
		CustomMessage message = new CustomMessageBuilder().multipart(new MultipartBuilder().withBodyParts(bodyPart).build()).build();
		Attachment attachment = getOnlyElement(message.getAttachments());
		assertThat(attachment).isEqualTo(new Attachment("sample.pdf", 113801));
	}

	@Test
	public void getAttachments_ReturnsAttachments() throws IOException {
		BodyPart bodyPart1 = new BodyPartBuilder().withContentDisposition("attachment; filename=\"sample1.pdf\"").build();
		BodyPart bodyPart2 = new BodyPartBuilder().withContentDisposition("attachment; filename=\"sample2.pdf\"").build();
		CustomMessage message = new CustomMessageBuilder().multipart(new MultipartBuilder().withBodyParts(bodyPart1, bodyPart2).build()).build();
		assertThat(message.getAttachments()).containsExactly("sample1.pdf", "sample2.pdf");
	}

	@Test
	public void getAttachments_ReturnsEmptyListWhenNoAttachments() throws IOException {
		CustomMessage message = new CustomMessageBuilder().withTextBody("test").build();
		assertThat(message.getAttachments()).isEmpty();
	}
}
