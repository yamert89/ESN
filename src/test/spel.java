import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class spel {

    @Test
    public void spel(){
        ExpressionParser parser = new SpelExpressionParser();
        Assert.assertEquals("dataSourceMySQL", parser.parseExpression("'mysql' eq 'mysql' ? 'dataSourceMySQL' : 'dataSourcePostgres'").getValue());
    }
}
