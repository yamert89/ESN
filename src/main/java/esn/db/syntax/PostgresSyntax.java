package esn.db.syntax;

public class PostgresSyntax implements Syntax{
    @Override
    public final String checkTable() {
        return "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME=";
    }

    @Override
    public final String createTableConstraints() {return " (id SERIAL, message varchar(500), userId int, time timestamp, orgId int)";}

    @Override
    public final String createTableConstraintsPrivate() {
        return " (id SERIAL, message varchar(500), userId int, time timestamp, orgId int)"; //TODO TIMESTAMP?;
    }

    @Override
    public final String selectChatMessages() {return "select * from generalchat where orgId = ? order by time desc limit ?";}

    @Override
    public final String selectWallMessages() {
        return "select * from wall where orgId = ? order by time desc limit ?";
    }

    @Override
    public final String selectChatMessagesWithIdx() {return "select * from generalchat where orgId = ? and id < ? order by time desc limit ?";}

    @Override
    public final String selectWallMessagesWithIdx() { return "select * from wall where orgId = ? and id < ? order by time desc limit ?";}
}
