package esn.db.syntax;

public interface Syntax {
    String checkTable();
    String createTableConstraints();
    String createTableConstraintsPrivate();
    String selectChatMessages();
    String selectWallMessages();
    String selectChatMessagesWithIdx();
    String selectWallMessagesWithIdx();
}
