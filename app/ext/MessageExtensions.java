package ext;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.owasp.html.Sanitizers.BLOCKS;
import static org.owasp.html.Sanitizers.FORMATTING;
import static org.owasp.html.Sanitizers.LINKS;
import static org.owasp.html.Sanitizers.STYLES;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import play.templates.JavaExtensions;

public class MessageExtensions extends JavaExtensions {

	public static String bodyAsString(Message message) {
		if (message.isMultipart()) {
			return multipartBodyAsString(message);
		} else {
			return textBodyAsString(message);
		}
	}

	private static String textBodyAsString(Entity entity) {
		return textBodyAsString((TextBody) entity.getBody());
	}

	private static String textBodyAsString(TextBody textBody) {
		try {
			String textBodyAsString = IOUtils.toString(textBody.getReader());
			PolicyFactory inlineFormatting = new HtmlPolicyBuilder().allowCommonInlineFormattingElements().allowStyling().toFactory();
			PolicyFactory policy = FORMATTING.and(LINKS).and(BLOCKS).and(STYLES).and(inlineFormatting);
			return policy.sanitize(textBodyAsString);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String multipartBodyAsString(Entity entity) {
		Multipart multipart = (Multipart) entity.getBody();
		for (BodyPart bodyPart : multipart.getBodyParts()) {
			if (bodyPart.isMimeType("text/html")) {
				return textBodyAsString((TextBody) bodyPart.getBody());
			} else if (bodyPart.isMultipart()) {
				return multipartBodyAsString(bodyPart);
			}
		}
		return textBodyAsString(getOnlyElement(multipart.getBodyParts()));
	}
}
