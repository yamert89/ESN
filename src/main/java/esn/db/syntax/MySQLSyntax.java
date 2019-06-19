package esn.db.syntax;

public class MySQLSyntax implements Syntax{

    @Override
    public final String checkTable() {
        return "show tables like ";
    }

    @Override
    public final String createTableConstraints() {
        return " (id int not null auto_increment primary key, message varchar(500), userId int, time timestamp, orgId int)"; //TODO TIMESTAMP?;
    }

    @Override
    public final String createTableConstraintsPrivate() {
        return " (id int not null auto_increment primary key, message varchar(500), userId int, time timestamp, orgId int)"; //TODO TIMESTAMP?;
    }

    @Override
    public final String selectChatMessages() {
        return "select * from generalchat where orgId = ? order by time desc limit ?";
    }

    @Override
    public final String selectWallMessages() {
        return "select * from wall where orgId = ?  or orgId = 0 order by time desc limit ?";
    }

    @Override
    public final String selectChatMessagesWithIdx() {
        return "select * from generalchat where orgId = ? or orgId = 0 and id < ? order by time desc limit ?";
    }

    @Override
    public final String selectWallMessagesWithIdx() {
        return "select * from wall where orgId = ? and id < ? order by time desc limit ?";
    }

    @Override
    public String currentDate() {
        return "NOW()";
    }


}
