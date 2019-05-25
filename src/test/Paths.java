import org.junit.Test;

public class Paths {

    @Test
    public void currentPath(){
        String pathExpected = "E:/JavaProject/ESN/target/ESN";
        ClassLoader loader = this.getClass().getClassLoader();
        //Assert.assertEquals(pathExpected, .getResource());
        System.out.println(System.getProperty("user.dir") + "\\src\\main\\webapp\\resources");
    }
}
