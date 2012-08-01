package mime4j;

import static org.apache.james.mime4j.field.AbstractField.parse;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyFactory;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Header;
import org.apache.james.mime4j.message.Multipart;

public class EntityBuilder<T extends EntityBuilder<?>> {

	private Body body = new BodyFactory().textBody("body");
	private Header header = new Header();

	protected void build(Entity entity) {
		entity.setBody(body);
		entity.setHeader(header);
	}

	public T withContentType(String contentType) {
		return withHeader("Content-Type: " + contentType);
	}

	public T withCharset(String string) {
		return withContentType("text/plain; charset=utf-8");
	}

	public T withContentDisposition(String contentDisposition) {
		return withHeader("Content-Disposition: " + contentDisposition);
	}

	public T withBody(Body body) {
		this.body = body;
		return (T) this;
	}

	public T withTextBody(String string) {
		return withBody(new BodyFactory().textBody(string));
	}

	public T withTextBody(String string, String mimeCharset) {
		return withBody(new BodyFactory().textBody(string, mimeCharset));
	}

	public T withHeader(String field) {
		try {
			header.addField(parse(field));
			return (T) this;
		} catch (MimeException e) {
			throw new RuntimeException(e);
		}
	}

	public T multipart(Multipart body) {
		withContentType("multipart/alternative; boundary=--NextPart_048F8BC8A2197DE2036A");
		return withBody(body);
	}

}
