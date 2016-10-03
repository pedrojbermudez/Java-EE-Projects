
package servlet.user;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import database.Constant;
import database.UserDB;
import utils.ExtractFileName;
import utils.MD5Checksum;

@MultipartConfig
public class EditUser extends HttpServlet {

  /**
   * Servlet to edit an user
   */
  private static final long serialVersionUID = 4182190514156962668L;
  private UserDB db;

  @Override
  public void init() throws ServletException {
    db = new UserDB();
  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String fileName;
    // Setting path
    String appPath = request.getServletContext().getRealPath("");
    String savePath = appPath + File.separator + Constant.SAVE_IMAGE_DIR;
    // Getting the file and write in the server and checking type name "path"
    if (request.getParameter("path").isEmpty()) {
      // User changed him/her picture
      Part part = request.getPart("user_profile_picture");
      // Getting the file name
      fileName = (new ExtractFileName()).extractFileName(part);
      if (!fileName.isEmpty()) {
        // File name exists
        // Getting the extension
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        // Getting MD5
        fileName = (new MD5Checksum(part.getInputStream())).getCheckSum();
        if (!(new File(savePath + File.separator + fileName)).exists()) {
          // File not exists and write it in server
          part.write(savePath + File.separator + fileName + ext);
        }
        // Creating the final file name for saving it into database and
        // replace \ character by two \ to escape it in the database
        fileName = (Constant.SAVE_IMAGE_DIR + File.separator + fileName + ext)
            .replace("\\", "\\\\");
      } else {
        // There is not file so the final file name will be the picture by
        // default
        fileName = Constant.PROFILE_PICTURE_DEFAULT;
      }
    } else {
      // The user doesn't want to change the picture
      fileName = request.getParameter("path");
    }
    //Editing the user
    if ((Integer.parseInt(session.getAttribute("id").toString()) == 1
        || Integer.parseInt(session.getAttribute("id").toString()) == Integer
            .parseInt(request.getParameter("user_id")))
        && !db.editUser(Integer.parseInt(request.getParameter("user_id")),
            request.getParameter("user_name"),
            request.getParameter("user_surname"), (fileName),
            request.getParameter("user_country"),
            request.getParameter("user_state"),
            request.getParameter("user_city"))) {
      response.setHeader("Edit_User", "ok");
    } else {
      response.setHeader("Edit_User", "error");
    }
    response.sendRedirect("/Forum/index.jsp");
  }
}
