
package utils;

import java.util.ArrayList;

import database.MessageDB;

public class GetterMessage {
  private MessageDB db;
  
  private ArrayList<String[]> setMessageList(int sender, int receiver, int index, int totalElements){
    db = new MessageDB();
    return db.getMessages(sender, receiver, index, totalElements);
  }
  
}
