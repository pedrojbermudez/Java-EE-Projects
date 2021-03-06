package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Checksum {

  private InputStream file;

  public MD5Checksum(InputStream file) {
    this.file = file;
  }
  
  public void setFile(InputStream file){
    this.file = file;
  }

  private byte[] createChecksum() throws NoSuchAlgorithmException,
      IOException {
    MessageDigest digest = MessageDigest.getInstance("MD5");

    byte[] buffer = new byte[1024];

    int numRead;
    while ((numRead = file.read(buffer)) != -1) {
      digest.update(buffer, 0, numRead);
    }
    file.close();
    return digest.digest();
  }

  public String getCheckSum() {
    StringBuffer sb = new StringBuffer();
    byte[] bytes = null;
    try {
      bytes = createChecksum();
    } catch (NoSuchAlgorithmException | IOException e) {
      System.err.println(e.getMessage());
    }
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(0xff & bytes[i]);
      if(hex.length() == 1){
        sb.append(0);
      } else{
        sb.append(hex);
      }
    }
    return sb.toString();
  }
}