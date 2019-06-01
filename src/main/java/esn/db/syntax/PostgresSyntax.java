package esn.db.syntax;

public class PostgresSyntax {
    public final String CHECKTABLE = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME=";
    public final String CREATE_TABLE_CONSTRAINTS = " (id SERIAL, message varchar(500), userId int, time timestamp, orgId int)"; //TODO TIMESTAMP?
    public final String SELECT_CHAT_MESSAGES = "select * from generalchat where orgId = ? order by time desc limit ?";
    public final String SELECT_WALL_MESSAGES = "select * from wall where orgId = ? order by time desc limit ?";
    public final String SELECT_CHAT_MESSAGES_WITHIDX = "select * from generalchat where orgId = ? and id < ? order by time desc limit ?";
    public final String SELECT_WALL_MESSAGES_WITHIDX = "select * from wall where orgId = ? and id < ? order by time desc limit ?";

}
