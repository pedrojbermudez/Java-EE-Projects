
package servlet.user;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import constants.Constant;
import database.UserDB;
import utils.MD5Checksum;

@MultipartConfig

public class NewUser extends HttpServlet {

  /**
   * This class contains the necessary manage of user functions new, edit and
   * delete
   */
  private static final long serialVersionUID = 7142930697264928870L;
  private UserDB db;

  public void init() throws ServletException {
    db = new UserDB();
  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String savePath;
      Part filePart = request.getPart("user_profile_picture");
      if (filePart != null && filePart.getSize() > 0) {
        savePath = Constant.SAVE_IMAGE_DIR + File.separator;
        String md5 = (new MD5Checksum(
            savePath + filePart.getSubmittedFileName())).getCheckSum();
        filePart.write(savePath + md5);
        savePath += md5;
      } else {
        savePath = Constant.PROFILE_PICTURE_DEFAULT;
      }
      
      if (db.newUser(request.getParameter("user_name"),
          request.getParameter("user_surname"),
          request.getParameter("user_email"),
          request.getParameter("user_user_name"),
          request.getParameter("user_password"), savePath,
          request.getParameter("user_country"),
          request.getParameter("user_state"),
          request.getParameter("user_city"))) {
        // sending a confirmation email.
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mipruebajava@hotmail.com",
                    "Asd123asd");
              }
            });
        try {
          Message message = new MimeMessage(session);
          message.setFrom(new InternetAddress("mipruebajava@hotmail.com"));
          message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse("pedrojavierbermudezaraguez@live.com"));
          message.setSubject("Registration Complete");
          message.setText("Thank you for registering in the forum,");
          Transport.send(message);
        } catch (MessagingException e) {
          System.err.println(e.getMessage());
        }
        response.setHeader("New_User",
            "The user " + request.getAttribute("user_name") + " was created.");
        response.sendRedirect("/Forum/index.jsp");
      } else {
        response.setHeader("New_User_ERROR_MESSAGE",
            "The user " + request.getAttribute("user_name")
                + " was created by other user.");
        response.sendRedirect("/Forum/index.jsp");
      }

    } catch (ServletException | IOException e) {
      System.err.println(e.getMessage());
    }
  }
}