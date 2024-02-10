package hilo;

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

import main.Main;

public class HiloBuscador extends Thread{
    // Se guardara en el mapa la path de los archivos con la vez que fueron modificados
    private Map<File, String> archivos;
    public final static String NAMESAVEFILES = ".data.dat";
    @Override
    public void run() {
        File file;
        
        while ((file=Main.getPathOfList())!=null){
            // System.out.println("Inicio de analisis de "+this.getName());
            archivos = recolectarInformacion(file);
            buscar(file.getAbsolutePath());
            guardarInformacion(archivos, file);
        }
        Main.removeHilo(this);
    }

    public void buscar(String path){
        File fileRoot = new File(path);
        // Se recorreran todos los archivos de la carpeta raiz y si alguno se ha modificado
        for (File file : fileRoot.listFiles()) {
            if (file.isDirectory()) {
                // Añado la carpeta ha la lista
                Main.setNewPath(file);
                // Lanzo un nuevo hilo buscador
                Main.setNewHilo(new HiloBuscador());
            }
            try {
                // System.out.println(file.getPath());
                BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getPath()) , BasicFileAttributes.class);
                if (archivos.get(file)==null && !file.toString().equals(fileRoot.getAbsolutePath()+"\\"+NAMESAVEFILES)) {
                    archivos.put(file, attr.lastModifiedTime().toString());
                    System.out.println(file.getPath()+" es un archivo nuevo");
                }else if(!file.toString().equals(fileRoot.getAbsolutePath()+"\\"+NAMESAVEFILES)){
                    if (!archivos.get(file).equals(attr.lastModifiedTime().toString())) {
                        archivos.put(file, attr.lastModifiedTime().toString());
                        System.out.println(file.getPath()+" Se ha modificado");
                    }
                }
            } catch (IOException e) {
                System.out.println(e+" (no se pudo consegir los atributos)");
            }
        }
        // Se mirara si alguno se ha borrado
        List<File> filesDelete = new ArrayList<>();
        for (File file : archivos.keySet()) {
            if (!file.exists()) {
                System.out.println(file+" Se ha eliminado");
                filesDelete.add(file);
            }
        }
        filesDelete.stream().forEach(archivos::remove);
    }
    public void guardarInformacion(Map<File, String> infoMap, File file){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(file+"\\"+NAMESAVEFILES)))){
            oos.writeObject(infoMap);
        } catch (Exception e) {
            System.out.println(e+" (Error al guardar)");
        }
    }
    @SuppressWarnings("unchecked")
    public Map<File, String> recolectarInformacion(File file){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(file+"\\"+NAMESAVEFILES)))){
            return (Map<File, String>) ois.readObject();
        } catch (Exception e) {
            // System.out.println("Data no creado");
            return new HashMap<>();
        }
    }
}
