
package servlet.user;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
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
    // getting app path and let into default save path.
    String appPath = request.getServletContext().getRealPath("");
    String savePath = appPath + File.separator + Constant.SAVE_IMAGE_DIR;

    // creating directory
    File fileSaveDir = new File(savePath);
    if (!fileSaveDir.exists()) {
      fileSaveDir.mkdir();
    }
    // getting the file and write in the server.
    Part part = request.getPart("user_profile_picture"); // name used in
    // form.
    String tmp = (new ExtractFileName()).extractFileName(part);
    String fileName = tmp != null ? (new MD5Checksum(tmp)).getCheckSum()
        : Constant.PROFILE_PICTURE_DEFAULT;
    if (!(new File(fileName)).exists()) {
      part.write(savePath + File.separator + fileName);
    }
    if (!db.editUser(Integer.parseInt(session.getAttribute("id").toString()),
        request.getParameter("user_name"), request.getParameter("user_surname"),
        (savePath.replace("\\", "/") + "/" + fileName),
        request.getParameter("user_country"),
        request.getParameter("user_state"),
        request.getParameter("user_city"))) {
      response.setHeader("Edit_User", "ok");
    } else {
      response.setHeader("Edit_User", "error");
    }
    response.sendRedirect("index.jsp");
  }
}
