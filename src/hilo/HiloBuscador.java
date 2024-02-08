package hilo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import main.Main;

public class HiloBuscador extends Thread{
    
    @Override
    public void run() {
        File file;
        
        if ((file=Main.getPathOfList())!=null){
            // System.out.println("Inicio de analisis de "+this.getName());
            buscar(file.getAbsolutePath());
        }
        Main.removeHilo(this);
    }

    public void buscar(String path){
        File fileRoot = new File(path);
        // Se recorreran todos los archivos de la carpeta raiz y si alguno se ha modificado
        for (File file : fileRoot.listFiles()) {
            if (file.isDirectory()) {
                Main.setNewPath(file);
                Main.setNewHilo(new HiloBuscador());
            }else{
                try {
                    // System.out.println(file.getPath());
                    BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getPath()) , BasicFileAttributes.class);
                    if (Main.getInMap(file)==null && !file.getAbsolutePath().equals(Main.data.getAbsolutePath())) {
                        Main.putInMap(file, attr.lastModifiedTime().toString());
                        System.out.println(file.getPath()+" es un archivo nuevo");
                    }else if(!file.getAbsolutePath().equals(Main.data.getAbsolutePath())){
                        if (!Main.getInMap(file).equals(attr.lastModifiedTime().toString())) {
                            Main.putInMap(file, attr.lastModifiedTime().toString());
                            System.out.println(file.getPath()+" Se ha modificado");
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e+" (no se pudo consegir los atributos)");
                }
            }
        }
        // Se mirara si alguno se ha borrado
    }

}
