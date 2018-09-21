package com.hp.householdpolicies.utils;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2018-06-26.
 */

public class PropUtils {
    /**
     * 得到netconfig.properties配置文件中的所有配置属性
     *
     * @return Properties对象
     */
    public static Properties getNetConfigProperties() {
        Properties props = new Properties();
        InputStream in = PropUtils.class.getResourceAsStream("/config.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
    public static String readFileFromSDCard(String path, String fileName) {
        File file = new File(getSDPath() + path + fileName);
        if (!file.exists()) {
            return null;
        } else {
            String text = null;
            FileInputStream fileInputStream = null;
            ByteArrayOutputStream outStream = null;

            try {
                fileInputStream = new FileInputStream(file);
                outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while((len = fileInputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                byte[] content_byte = outStream.toByteArray();
                text = new String(content_byte, "UTF-8");
            } catch (IOException var17) {
                var17.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }

                    if (outStream != null) {
                        outStream.close();
                    }
                } catch (IOException var16) {
                    var16.printStackTrace();
                }

            }

            return text;
        }
    }
    public static String getSDPath() {
        String sdPath = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        if (sdCardExist) {
            File sdDir = Environment.getExternalStorageDirectory();
            if (sdDir != null) {
                sdPath = sdDir.toString();
            }
        }

        return sdPath;
    }
}
