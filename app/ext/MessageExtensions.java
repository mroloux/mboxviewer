package ext;

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
			return multipartBodyAsString((Entity) message);
		} else {
			return textBodyAsString(message);
		}
	}

	private static String textBodyAsString(Entity entity) {
		if (entity.isMimeType("text/html")) {
			return textHtmlBodyAsString((TextBody) entity.getBody());
		} else if (entity.isMimeType("text/plain")) {
			return textPlainBodyAsString((TextBody) entity.getBody());
		}
		return "";
	}

	private static String textPlainBodyAsString(TextBody textBody) {
		try {
			String textBodyAsString = IOUtils.toString(textBody.getReader());
			return "<pre>" + escapeHtml(textBodyAsString) + "</pre>";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String textHtmlBodyAsString(TextBody textBody) {
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
		TextBody textHtmlBody = findBodyPart(multipart, "text/html");
		if (textHtmlBody != null) {
			return textHtmlBodyAsString(textHtmlBody);
		}
		return textPlainBodyPartAsString(multipart);
	}

	private static String textPlainBodyPartAsString(Multipart multipart) {
		TextBody textPlainBody = findBodyPart(multipart, "text/plain");
		if (textPlainBody != null) {
			return textPlainBodyAsString(textPlainBody);
		}
		return "";
	}

	private static TextBody findBodyPart(Multipart multipart, String mimeType) {
		for (BodyPart bodyPart : multipart.getBodyParts()) {
			if (bodyPart.isMimeType(mimeType)) {
				return (TextBody) bodyPart.getBody();
			} else if (bodyPart.isMultipart()) {
				return findBodyPart((Multipart) bodyPart.getBody(), mimeType);
			}
		}
		return null;
	}
}
