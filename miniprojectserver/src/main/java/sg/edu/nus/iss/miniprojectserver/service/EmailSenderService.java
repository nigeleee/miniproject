package sg.edu.nus.iss.miniprojectserver.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import sg.edu.nus.iss.miniprojectserver.entity.CartItem;
import sg.edu.nus.iss.miniprojectserver.entity.Order;
import sg.edu.nus.iss.miniprojectserver.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    // @Value("{spring.mail.username}")
    // private String email;
    private static final String email = "nigeleee@gmail.com";

    public void sendEmail(String toEmail, String body, String subject) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);

        System.out.println("HTML email sent");
    }

    public void sendReceiptEmail(Order order, String applicationUrl) throws MessagingException {
    StringBuilder sb = new StringBuilder();
    sb.append("<h1>Your receipt</h1>")
      .append("<h3>Thank you for shopping with us!</h3>")
      .append("<p>Order ID: ").append(order.getOrderId()).append("</p>")
      .append("<p>Name: ").append(order.getFirstName()).append(" ").append(order.getLastName()).append("</p>")
      .append("<p>Email: ").append(order.getEmail()).append("</p>")
      .append("<p>Phone: ").append(order.getPhone()).append("</p>")
      .append("<p>Address: ").append(order.getAddress()).append("</p>");

    sb.append("<h2>Items:</h2><ul>");
    for (CartItem item : order.getCartItems()) {
        sb.append("<li>").append(item.getProduct().getName())
          .append(" (Quantity: ").append(item.getQuantity())
          .append(", Price: ").append(item.getProduct().getPrice()).append(")</li>");
    }
    sb.append("</ul>");

    String subject = "Invoice";

    sendEmail((String) order.getEmail(), sb.toString(), subject);
}


    public void sendVerificationEmail(User user, String token, String applicationUrl) throws MessagingException {
        String url = applicationUrl + "/api/verifyRegistration?token=" + token;
        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h2>Thank you for signing up with Lee's Kimchi!</h2>" +
                "        <p>Please click the button below to verify your account:</p>" +
                "        <a class=\"btn\" href=\"" + url + "\">Verify Account</a>" +
                "        <p>Best regards,<br>Lee's Kimchi Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
        String subject = "Verify your Account";

        sendEmail(user.getEmail(), body, subject);
    }

     // public void sendSimpleEmail(String toEmail, String body, String subject) throws MessagingException {

    //     SimpleMailMessage message = new SimpleMailMessage();

    //     message.setFrom(email);
    //     message.setTo(toEmail);
    //     message.setText(body);
    //     message.setSubject(subject);

    //     mailSender.send(message);

    //     System.out.println("Mail sent");
    // }

}
