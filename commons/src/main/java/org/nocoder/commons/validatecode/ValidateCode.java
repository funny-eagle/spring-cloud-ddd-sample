package org.nocoder.commons.validatecode;

import org.apache.commons.lang.Validate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class ValidateCode {
    // 图片的宽度。
    private int width = 160;
    // 图片的高度。  
    private int height = 40;

    // 验证码干扰线数
    private int lineCount = 150;
    // 验证码  
    private String code = null;
    // 验证码图片Buffer  
    private BufferedImage buffImg = null;

    public ValidateCode(String code) {
        Validate.notEmpty(code, "code cannot be empty");
        this.createCode(code);
    }

    /**
     * @param width  图片宽
     * @param height 图片高
     * @param code   验证码字符
     */
    public ValidateCode(int width, int height, String code) {
        Validate.notEmpty(code, "code cannot be empty");
        this.width = width;
        this.height = height;
        this.code = code;
        this.createCode(code);
    }

    /**
     * @param width     图片宽
     * @param height    图片高
     * @param code      验证码字符
     * @param lineCount 干扰线条数
     */
    public ValidateCode(int width, int height, int lineCount, String code) {
        Validate.notEmpty(code, "code cannot be empty");
        this.width = width;
        this.height = height;
        this.code = code;
        this.lineCount = lineCount;
        this.createCode(code);
    }

    public void createCode(String code) {
        int x = 0, fontHeight = 0, codeY = 0;
        int red = 0, green = 0, blue = 0;

        int codeCount = code.length();

        x = width / (codeCount + 2);//每个字符的宽度
        fontHeight = height - 2;//字体的高度  
        codeY = height - 4;

        // 图像buffer  
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 生成随机数  
        Random random = new Random();
        // 将图像填充为白色  
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体  
        ImgFontByte imgFont = new ImgFontByte();
        Font font = imgFont.getFont(fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 8);
            int ye = ys + random.nextInt(height / 8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        // 随机产生codeCount个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            String str = code.substring(i, i + 1);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。  
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(str, (i + 1) * x, codeY);
        }
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code;
    }
}
