package main.java.jatch;

import java.io.*;
import java.util.zip.*;

public class Unzipper {
    public static void unzip(String zipfile, String newdir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfile));
        ZipEntry zipEntry = zis.getNextEntry();
        new File(newdir).mkdirs();
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            File newFile = new File(newdir + fileName);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
}