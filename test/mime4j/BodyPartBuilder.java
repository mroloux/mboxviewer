package mime4j;

import org.apache.james.mime4j.message.BodyPart;

public class BodyPartBuilder extends EntityBuilder<BodyPartBuilder> {

	public BodyPart build() {
		BodyPart bodyPart = new BodyPart();
		build(bodyPart);
		return bodyPart;
	}

}
