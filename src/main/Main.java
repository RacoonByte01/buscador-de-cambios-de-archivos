package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * Main
 * @author JaviLeL
 * @version 1.0
 */
public class Main {
    // Guardare los archivos con sus atributos
    static Map<File, BasicFileAttributes> archivos = new HashMap<>();
    static final File data = new File("data.dat"); // Archivo donde se guardara la informacion
    public static void main(String[] args) {
        
        while (true) {
            buscar("/mnt/pruebaBuscador/");
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
                        archivos.put(file, attr);
                        System.out.println(file.getPath()+" es un archivo nuevo");
                    }else if(!file.getAbsolutePath().equals(data.getAbsolutePath())){
                        if (!archivos.get(file).lastModifiedTime().equals(attr.lastModifiedTime())) {
                            archivos.put(file, attr);
                            System.out.println(file.getPath()+" Se ha modificado");
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e+" (no se pudo consegir los atributos)");
                }
            }
        }

        // Se mirara si alguno se ha borrado
        for (File file : archivos.keySet()) {
            if (!file.isFile()) {
                System.out.println(file+" Se ha eliminado");
                archivos.remove(file);
            }
        }
    }
}