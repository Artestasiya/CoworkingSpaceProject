package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomClassLoader extends ClassLoader {

    private final String classPath;

    public CustomClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String path = classPath + File.separatorChar + name.replace('.', File.separatorChar) + ".class";
        byte[] classData = loadClassData(path);

        if (classData == null) {
            throw new ClassNotFoundException("Class " + name + " not found at " + path);
        }

        return defineClass(name, classData, 0, classData.length);
    }

    private byte[] loadClassData(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            return buffer;
        } catch (IOException e) {
            System.err.println("Error loading class data: " + e.getMessage());
            return null;
        }
    }
}