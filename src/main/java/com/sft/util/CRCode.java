package com.sft.util;

import com.aioute.util.ZXingCode;
import com.google.zxing.BarcodeFormat;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class CRCode {

    public static String getTomcatWebappsPath(HttpServletRequest request) {
        String tomcatRoot = request.getSession().getServletContext().getRealPath("/");
        String[] foo = tomcatRoot.split("/");
        StringBuilder tomcatWebAppsBuilder = new StringBuilder();
        int i = 0;
        for (String paths : foo) {
            ++i;
            if (i != foo.length) {
                tomcatWebAppsBuilder.append(paths);
                tomcatWebAppsBuilder.append("/");
            }
        }
        String tomcatWebApps = tomcatWebAppsBuilder.toString();
        return tomcatWebApps;
    }

    public static String getQRCode(String qrUrl, String productName, HttpServletRequest request) {
        String filePath = request.getSession().getServletContext().getRealPath("");
        String fileName = productName + ".png";

        productName = new ChangeToPinYin().getStringPinYin(productName);
        if (productName.contains("@"))
            productName = productName.substring(0, productName.indexOf("@"));
        String content = qrUrl;
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(filePath, productName.replace(" ",""));
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            ZXingCode zp = new ZXingCode();
            BufferedImage bim = zp.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 400, 400, zp.getDecodeHintType());
            addLogo_QRCode(bim, null, file, new ZXingCode.LogoConfig(), productName);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addLogo_QRCode(BufferedImage bim, File logoPic, File returnFile, ZXingCode.LogoConfig logoConfig, String productName) {
        try {
            BufferedImage image = bim;
            Graphics2D g = bim.createGraphics();
            int strWidth;
            if (logoPic != null && logoPic.exists()) {
                BufferedImage logo = ImageIO.read(logoPic);
                if (logo != null) {
                    int widthLogo = logo.getWidth((ImageObserver) null) > bim.getWidth() * 3 / 10 ? bim.getWidth() * 3 / 10 : logo.getWidth((ImageObserver) null);
                    int heightLogo = logo.getHeight((ImageObserver) null) > bim.getHeight() * 3 / 10 ? bim.getHeight() * 3 / 10 : logo.getWidth((ImageObserver) null);
                    strWidth = (bim.getWidth() - widthLogo) / 2;
                    int y = (bim.getHeight() - heightLogo) / 2;
                    g.drawImage(logo, strWidth, y, widthLogo, heightLogo, (ImageObserver) null);
                    g.dispose();
                    logo.flush();
                }
            }

            if (productName != null && !productName.equals("")) {
                BufferedImage outImage = new BufferedImage(400, 445, 6);
                Graphics2D outg = outImage.createGraphics();
                outg.drawImage(bim, 0, 0, bim.getWidth(), bim.getHeight(), (ImageObserver) null);
                outg.setColor(Color.BLACK);
                outg.setFont(new Font("宋体", 1, 30));
                strWidth = outg.getFontMetrics().stringWidth(productName);
                if (strWidth > 399) {
                    String productName1 = productName.substring(0, productName.length() / 2);
                    String productName2 = productName.substring(productName.length() / 2, productName.length());
                    int strWidth1 = outg.getFontMetrics().stringWidth(productName1);
                    int strWidth2 = outg.getFontMetrics().stringWidth(productName2);
                    outg.drawString(productName1, 200 - strWidth1 / 2, bim.getHeight() + (outImage.getHeight() - bim.getHeight()) / 2 + 12);
                    BufferedImage outImage2 = new BufferedImage(400, 485, 6);
                    Graphics2D outg2 = outImage2.createGraphics();
                    outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), (ImageObserver) null);
                    outg2.setColor(Color.BLACK);
                    outg2.setFont(new Font("宋体", 1, 30));
                    outg2.drawString(productName2, 200 - strWidth2 / 2, outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
                    outg2.dispose();
                    outImage2.flush();
                    outImage = outImage2;
                } else {
                    outg.drawString(productName, 200 - strWidth / 2, bim.getHeight() + (outImage.getHeight() - bim.getHeight()) / 2 + 12);
                }

                outg.dispose();
                outImage.flush();
                image = outImage;
            }

            image.flush();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.flush();
            ImageIO.write(image, "png", baos);
            ImageIO.write(image, "png", returnFile);
            String imageBase64QRCode = Base64.encodeBase64URLSafeString(baos.toByteArray());
            baos.close();
            return imageBase64QRCode;
        } catch (Exception var17) {
            var17.printStackTrace();
            return null;
        }
    }
}
