package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {
    private final Path classPath;

    public CustomClassLoader(String classPath) {
        super(); // Используем родительский ClassLoader
        this.classPath = Paths.get(classPath).toAbsolutePath().normalize();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 1. Проверяем уже загруженные классы
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }

            // 2. Пытаемся загрузить через родительский ClassLoader (стандартные классы)
            try {
                return super.loadClass(name, resolve);
            } catch (ClassNotFoundException e) {
                // 3. Если не найден - пробуем наш кастомный загрузчик
                return findClass(name);
            }
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String filePath = name.replace('.', File.separatorChar) + ".class";
            Path fullPath = classPath.resolve(filePath).normalize();

            // Проверка безопасности пути
            if (!fullPath.startsWith(classPath)) {
                throw new SecurityException("Attempt to access path outside of classpath directory");
            }

            byte[] classData = loadClassData(fullPath);
            return defineClass(name, classData, 0, classData.length);
        } catch (Exception e) {
            throw new ClassNotFoundException("Failed to load class " + name, e);
        }
    }

    private byte[] loadClassData(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}