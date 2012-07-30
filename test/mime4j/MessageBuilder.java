package mime4j;

import org.apache.james.mime4j.message.Message;

public class MessageBuilder extends EntityBuilder<MessageBuilder> {

	public Message build() {
		Message message = new Message();
		build(message);
		return message;
	}

}
