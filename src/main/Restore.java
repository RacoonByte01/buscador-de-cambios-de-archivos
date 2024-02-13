package main;

import java.io.File;

import hilo.HiloBuscador;

public class Restore {
    public static void main(String[] args) {
        busca(Main.pathInicio.getAbsolutePath());
    }
    static void busca(String path){
        File fileRoot = new File(path);
        for (File file : fileRoot.listFiles()) {
            if (file.isDirectory()) {
                busca(file.getAbsolutePath());
            }else if (file.getName().equals(HiloBuscador.NAMESAVEFILES)) {
                file.delete();
            }
        }
    }
}