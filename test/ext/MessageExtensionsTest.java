package ext;

import static org.fest.assertions.Assertions.assertThat;
import mime4j.BodyPartBuilder;
import mime4j.MultipartBuilder;
import models.CustomMessageBuilder;

import org.apache.james.mime4j.message.BodyFactory;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.junit.Test;

public class MessageExtensionsTest {

	@Test
	public void multipartWithOneBody() {
		Message message = new CustomMessageBuilder()
				.multipart(new MultipartBuilder().withBody(new BodyFactory().textBody("da body")).build())
				.build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("<pre>da body</pre>");
	}

	@Test
	public void multipartWithBodiesTextPlainAndTextHtml() {
		BodyPart textIdunnowBodyPart = new BodyPartBuilder().withContentType("text/idunnow").withTextBody("da text/idunnow body").build();
		BodyPart textHtmlBodyPart = new BodyPartBuilder().withContentType("text/html").withTextBody("da text/html body").build();
		BodyPart textPlainBodyPart = new BodyPartBuilder().withContentType("text/plain").withTextBody("da text/plain body").build();
		Message message = new CustomMessageBuilder()
				.multipart(new MultipartBuilder().withBodyParts(textIdunnowBodyPart, textHtmlBodyPart, textPlainBodyPart).build())
				.build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("da text/html body");
	}

	@Test
	public void multipartWithNestedMultipartBody() {
		BodyPart textHtmlBodyPart = new BodyPartBuilder().withContentType("text/html").withTextBody("da text/html body").build();
		BodyPart textPlainBodyPart = new BodyPartBuilder().withContentType("text/plain").withTextBody("da text/plain body").build();
		Multipart nestedBody = new MultipartBuilder().withBodyParts(textHtmlBodyPart, textPlainBodyPart).build();
		Message message = new CustomMessageBuilder()
				.multipart(new MultipartBuilder().withBodyParts(new BodyPartBuilder().multipart(nestedBody).build()).build())
				.build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("da text/html body");
	}

	@Test
	public void encodedAsUTF8() {
		Message message = new CustomMessageBuilder().withTextBody("dá body", "utf-8").withContentType("text/html").build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("dá body");
	}

	@Test
	public void encodedAsISO88591() {
		Message message = new CustomMessageBuilder().withTextBody("dá body", "iso-8859-1").withContentType("text/html").build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("dá body");
	}

	@Test
	public void invalidTagsAreStrippedFromTextHtml() {
		BodyPart bodyPart = new BodyPartBuilder().withTextBody("da body<script>evil</script>").withContentType("text/html").build();
		Message message = new CustomMessageBuilder()
				.multipart(new MultipartBuilder().withBodyParts(bodyPart).build())
				.build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("da body");
	}

	@Test
	public void tagsAreEscapedWhenTextPlain() {
		BodyPart bodyPart = new BodyPartBuilder().withTextBody("da body<script>evil</script>").withContentType("text/plain").build();
		Message message = new CustomMessageBuilder()
				.multipart(new MultipartBuilder().withBodyParts(bodyPart).build())
				.build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("<pre>da body&lt;script&gt;evil&lt;/script&gt;</pre>");
	}

	@Test
	public void textBody() {
		Message message = new CustomMessageBuilder().withTextBody("da body").build();

		assertThat(MessageExtensions.bodyAsString(message)).isEqualTo("<pre>da body</pre>");
	}

}
