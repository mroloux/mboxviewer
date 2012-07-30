package mime4j;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Multipart;

public class MultipartBuilder {

	private List<BodyPart> bodyParts = newArrayList();

	public Multipart build() {
		Multipart multipart = new Multipart("");
		for (BodyPart bodyPart : bodyParts) {
			multipart.addBodyPart(bodyPart);
		}
		return multipart;
	}

	public MultipartBuilder withBody(Body body) {
		this.bodyParts = newArrayList(new BodyPartBuilder().withBody(body).build());
		return this;
	}

	public MultipartBuilder withBodyParts(BodyPart... bodyParts) {
		this.bodyParts = newArrayList(bodyParts);
		return this;
	}
}
