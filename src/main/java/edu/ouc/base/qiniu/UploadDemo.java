package edu.ouc.base.qiniu;

import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class UploadDemo {


    private static final String QINIU_URL = "http://ob7uytere.bkt.clouddn.com/";
    
    
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "CPzADA3dQlT2fhwXbZfGvJlIsDVVLyoiWSOaB-T4";
    String SECRET_KEY = "7gQjAIiuPMv9CSumxyuABsOW-qajHIyKWjd0ZvB2";
    //要上传的空间
    String bucketname = "ynote";
    //上传到七牛后保存的文件名
    String key = "my-java.png";
    //上传文件的路径
    String FilePath = "/.../...";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    public void upload(String filePath) throws IOException{
        try {
            if(!"".equals(filePath)){
                FilePath = filePath;
            }else{
                FilePath = "E:\\test.png";
            }
            
            key = "timestamp_" + System.currentTimeMillis() + "_test.png";
            
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, getUpToken());
            
            StringMap map = res.jsonToMap();
            String content = QINIU_URL + map.get("key");
            String markdown = "![" + map.get("key") + "](" + content + ")";
            
            //将返回信息放入黏贴板中
            StringSelection stringSelection = new StringSelection(markdown);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents( stringSelection, null );
        } catch (QiniuException e) {}
    }

    public static void main(String args[]) throws IOException, HeadlessException, UnsupportedFlavorException{  
        try {
            Image image = getImageFromClipboard();
            if(image != null){
                BufferedImage image1 = toBufferedImage(image);
                
                ImageIO.write(image1, "png", new File("e://test.png"));
                
                new UploadDemo().upload("e://test.png");
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    public static Image getImageFromClipboard() throws Exception {
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null)
            return null;
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
            return (Image) cc.getTransferData(DataFlavor.imageFlavor);
        return null;
    }
    
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}


