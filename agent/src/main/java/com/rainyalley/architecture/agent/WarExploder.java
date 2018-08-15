package com.rainyalley.architecture.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * classloader which allows user to set a war archive path to be included in the
 * classpath dynamically.
 *
 * @author <a href="mailto:miles.wy.1@gmail.com">pf_miles</a>
 *
 */
public class WarExploder {

    private String warPath;
    private File explodedFile;

    public WarExploder(String warPath) {
        this.warPath = warPath;
    }

    public boolean explod() {
        File war = new File(warPath);
        if(!war.exists()){
            throw new RuntimeException("File not exists: " + warPath);
        }
        // 0.create a temp dir
        final File dir = createTempDirectory(war.getName());
        // 1.extract the war to the temp dir
        extractTo(war, dir);
        this.explodedFile = dir;
        return true;
    }

    public void deleteExploded(){
        if (explodedFile != null) {
            delRecur(explodedFile);
            explodedFile = null;
        }
    }

    private static void delRecur(File file) {
        if (!file.exists()){
            return;
        }
        if (file.isDirectory()) {
            // 1.del sub files first
            for (File s : file.listFiles()){
                delRecur(s);
            }
            // 2.del the dir
            if(!file.delete()){
                file.deleteOnExit();
            }
        } else {
            if(!file.delete()){
                file.deleteOnExit();
            }
        }
    }

    // extract content of 'w' to dir
    private static void extractTo(File w, File dir) {
        String dirPath = dir.getAbsolutePath();
        if (!dirPath.endsWith("/")){
            dirPath += "/";
        }
        try {
            JarFile jar = new JarFile(w);
            Enumeration<JarEntry> e = jar.entries();
            byte[] buf = new byte[4096];

            while (e.hasMoreElements()) {
                JarEntry file = (JarEntry) e.nextElement();
                File f = new File(dirPath + file.getName());

                if (file.isDirectory()) { // if its a directory, create it
                    f.mkdirs();
                    continue;
                }

                InputStream is = jar.getInputStream(file);
                FileOutputStream fos = ensureOpen(f);

                // write contents of 'is' to 'fos'
                for (int avai = is.read(buf); avai != -1; avai = is.read(buf)) {
                    fos.write(buf, 0, avai);
                }
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // if does not exist, create one, and ensure all parent dirs exist
    private static FileOutputStream ensureOpen(File f) throws IOException {
        if (!f.exists()) {
            File p = f.getParentFile();
            if (p != null && !p.exists()){
                p.mkdirs();
            }
            f.createNewFile();
        }
        return new FileOutputStream(f);
    }

    private static File createTempDirectory(String dirName) {
        final File temp;

        try {
            temp = File.createTempFile(dirName, Long.toString(System.currentTimeMillis()));
            temp.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!(temp.delete())) {
            throw new RuntimeException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new RuntimeException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }

    /**
     * get the temporary directory path which contains the exploded war files
     */
    public File getExplodedFile() {
        return this.explodedFile;
    }

}
