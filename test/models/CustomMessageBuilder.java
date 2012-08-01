package models;

import mime4j.EntityBuilder;

public class CustomMessageBuilder extends EntityBuilder<CustomMessageBuilder> {

	public CustomMessage build() {
		CustomMessage message = new CustomMessage();
		build(message);
		return message;
	}

}
