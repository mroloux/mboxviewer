package controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.mail.MessagingException;

import models.Mailbox;
import play.mvc.Controller;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void convertMboxFile(byte[] mboxFile) throws MessagingException, IOException {
		Mailbox mailbox = new Mailbox(new ByteArrayInputStream(mboxFile));
		renderTemplate("Application/mailbox.html", mailbox);
	}

}