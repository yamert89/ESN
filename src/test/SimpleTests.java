import esn.utils.GeneralSettings;

public class SimpleTests {
    public static void main(String[] args) {
        try{
            props();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


   /* public static void imageResizer(){
        try {
            BufferedImage image = ImageIO.read(new File("C:/2.jpg"));
            BufferedImage image1 = ImageResizer.resizeBig(image);
            ImageIO.write(image1, "JPG", new File("D:/out.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static  void props(){
        System.out.println(GeneralSettings.AVATAR_PATH);
    }

}
