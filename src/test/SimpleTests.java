import esn.configs.GeneralSettings;

public class SimpleTests {
    public static void main(String[] args) {
        try{
            //props();
            String s = "fuck234".intern();
            String s1 = "fuck234";
            String s2 = "fuck234";
            String s3 = "fuck234";

            System.out.println(s == s1);
            System.out.println(s == s2);
            System.out.println(s3 == s2);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


   /* public static void imageResizer(){
        try {
            BufferedImage image = ImageIO.read(new File("C:/2.jpg"));
            BufferedImage image1 = ImageUtil.resizeBig(image);
            ImageIO.write(image1, "JPG", new File("D:/out.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static  void props(){
        System.out.println(GeneralSettings.STORAGE_PATH);
    }


}
