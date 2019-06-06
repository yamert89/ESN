package esn.db.syntax;

public class MySQLSyntax{
    public final String CHECKTABLE = "show tables like ";
    public final String CREATE_TABLE_CONSTRAINTS_WALL_GEN_MESSAGE = " (id int not null auto_increment primary key, message varchar(500), userId int, time timestamp, orgId int)"; //TODO TIMESTAMP?
    public final String CREATE_TABLE_CONSTRAINTS_PRIVATE = " (id int not null auto_increment primary key, message varchar(500), userId int, time timestamp, orgId int)"; //TODO TIMESTAMP?
    public final String SELECT_CHAT_MESSAGES = "select * from generalchat where orgId = ? order by time desc limit ?";
    public final String SELECT_WALL_MESSAGES = "select * from wall where orgId = ? order by time desc limit ?";
    public final String SELECT_CHAT_MESSAGES_WITHIDX = "select * from generalchat where orgId = ? and id < ? order by time desc limit ?";
    public final String SELECT_WALL_MESSAGES_WITHIDX = "select * from wall where orgId = ? and id < ? order by time desc limit ?";
}
