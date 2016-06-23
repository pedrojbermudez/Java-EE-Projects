
package servlet.user;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import constants.Constant;
import database.UserDB;
import sun.security.provider.MD5;
import utils.ExtractFileName;
import utils.MD5Checksum;

@MultipartConfig
public class EditUser extends HttpServlet {

  /**
   * 
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
    // creating directory
    String appPath = request.getServletContext().getRealPath("");
    String savePath = appPath + File.separator + Constant.SAVE_IMAGE_DIR;
    File fileSaveDir = new File(savePath);
    if (!fileSaveDir.exists()) {
      fileSaveDir.mkdir();
    }
    // getting the file and write in the server and checking type name "path"
    if(request.getParameter("path").isEmpty() || request.getParameter("path").equals("")){
      Part part = request.getPart("user_profile_picture"); // name used in
      // form.
      fileName = (new ExtractFileName()).extractFileName(part);
      if (!fileName.isEmpty()) {
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        fileName = (new MD5Checksum(part.getInputStream())).getCheckSum();
        if (!(new File(savePath + File.separator + fileName)).exists()) {
          part.write(savePath + File.separator + fileName + ext);
        }
        fileName = (Constant.SAVE_IMAGE_DIR + File.separator + fileName + ext)
            .replace("\\", "\\\\");
      } else {
        fileName = Constant.PROFILE_PICTURE_DEFAULT;
      }
    } else {
      fileName = request.getParameter("path"); 
    }
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
