package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hilo.HiloBuscador;

/**
 * Main
 * @author JaviLeL
 * @version 1.0.1  
 */
public class Main {
    // Guardare los archivos con sus atributos
    public static Map<File, String> archivos = new HashMap<>();
    private static List<File> paths = new ArrayList<>();
    private static List<HiloBuscador> hiloBuscadores = new ArrayList<>();
    public static final File data = new File("data.dat"); // Archivo donde se guardara la informacion
 
    public static void main(String[] args) {
        setNewPath(new File("D:\\Trabajos\\2º_Año\\Clase\\PSP\\UD2\\buscador-de-cambios-de-archivos"));
        setNewHilo(new HiloBuscador());
        while (true) {
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (hiloBuscadores.size()==0) {
                // La deteccion de borrado en multi hilo es imposible debido a que el mapa se esta simpre modificando
                // por lo que se realiza al final de este
                List<File> filesDelete = new ArrayList<>();
                for (File file : archivos.keySet()) {
                    if (!file.isFile()) {
                        System.out.println(file+" Se ha eliminado");
                        filesDelete.add(file);
                    }
                }
                filesDelete.stream().forEach(Main::removeInMap);
                setNewPath(new File("D:\\Trabajos\\2º_Año\\Clase\\PSP\\UD2\\buscador-de-cambios-de-archivos\\src"));
                setNewHilo(new HiloBuscador());
            }
        }
       
    }
    public static synchronized void putInMap(File file, String attr){
        archivos.put(file, attr);
    }
    public static synchronized String getInMap(File file){
        return archivos.get(file);
    }
    public static synchronized void removeInMap(File file){
        archivos.remove(file);
    }
    public static synchronized void setNewPath(File file){
        paths.add(file);
    }
    public static synchronized File getPathOfList(){
        try{
            File folder = paths.get(0);
            if (folder!=null) {
                paths.remove(0);
            }
            return folder;
        }catch(Exception e){
            return null;
        }
        
    }
    public static synchronized void setNewHilo(HiloBuscador hiloBuscador){
        if((hiloBuscadores.size()<20)){
            hiloBuscadores.add(hiloBuscador);
            hiloBuscador.start();
            // System.out.println(hiloBuscador.getName() + " // " +hiloBuscadores.size());
        }
    }
    public static synchronized void removeHilo(HiloBuscador hiloBuscador){
        hiloBuscadores.remove(hiloBuscador);
    }
    public static synchronized Set<File> getSetKeys(){
        return Main.archivos.keySet();
    }
    /*
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
    */
}
