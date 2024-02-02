package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main
 * @author JaviLeL
 * @version 1.0.1  
 */
public class Main {
    // Guardare los archivos con sus atributos
    static Map<File, String> archivos;
    static final File data = new File("data.dat"); // Archivo donde se guardara la informacion
    public static void main(String[] args) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(data))){
            archivos = (Map<File, String>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Data no creado");
            archivos = new HashMap<>();
        }
        while (true) {
            buscar("C:\\Users\\ALUMNO\\Desktop");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(data))){
                oos.writeObject(archivos);
            } catch (Exception e) {
                System.out.println(e+" (Error al guardar)");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e+" (Error al parar el hilo)");
            }
        }
    }
    public static void buscar(String path){
        File fileRoot = new File(path);
        // Se recorreran todos los archivos de la carpeta raiz y si alguno se ha modificado
        for (File file : fileRoot.listFiles()) {
            if (file.isDirectory()) {
                buscar(file.getPath());
            }else{
                try {
                    // System.out.println(file.getPath());
                    BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getPath()) , BasicFileAttributes.class);
                    if (archivos.get(file)==null && !file.getAbsolutePath().equals(data.getAbsolutePath())) {
                        archivos.put(file, attr.lastModifiedTime().toString());
                        System.out.println(file.getPath()+" es un archivo nuevo");
                    }else if(!file.getAbsolutePath().equals(data.getAbsolutePath())){
                        if (!archivos.get(file).equals(attr.lastModifiedTime().toString())) {
                            archivos.put(file, attr.lastModifiedTime().toString());
                            System.out.println(file.getPath()+" Se ha modificado");
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e+" (no se pudo consegir los atributos)");
                }
            }
        }
        // Se mirara si alguno se ha borrado
        List<File> filesDelete = new ArrayList<>();
        for (File file : archivos.keySet()) {
            if (!file.isFile()) {
                System.out.println(file+" Se ha eliminado");
                filesDelete.add(file);
            }
        }
        filesDelete.stream().forEach(archivos::remove);
    }
}
