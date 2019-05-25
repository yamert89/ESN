import org.junit.Test;

import java.nio.file.Path;

public class Paths {

    @Test
    public void currentPath(){
        String pathExpected = "E:/JavaProject/ESN/target/ESN";
        //String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String path = Main.class.getClassLoader().getResource("").getPath();
        Path p = java.nio.file.Paths.get(path.substring(1));
        p = java.nio.file.Paths.get(p.getParent().toString(), "/ESN/resources/");
        //Assert.assertEquals(pathExpected, .getResource());
        System.out.println(p.toString());
        //System.out.println(System.getProperty("user.dir") + "\\src\\main\\webapp\\resources");
    }
}
