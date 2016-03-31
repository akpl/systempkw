package pkw;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*; //Pokaże się unused. Ignore, mail potrzebuje.

public class SendMail
{
    private final String username = "elleander1@gmail.com";
    private final String password = "hibrzqfucdffdcwt"; //Generowane hasło dla tej apki, użyczam swoje konto na czas testów.
    private Properties properties;
    private Session session;
    private String subject;
    private String content;
    private Message message;
    public SendMail()
    {
        subject="Test Message from: "+username;
        content="This is a test message. Do not reply.";
        setProperties();
        createSession();
        message = new MimeMessage(session);
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void addRecipientToMail(String mail)
    {
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    private void setProperties()
    {
        properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
    }
    private void createSession()
    {
        session = Session.getDefaultInstance(properties);
    }

    public void sendEmail()
    {
        try {
            message.setSubject(subject);
            message.setContent(content, "text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
