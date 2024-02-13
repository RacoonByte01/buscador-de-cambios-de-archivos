package main;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import hilo.HiloBuscador;

/**
 * Main
 * @author JaviLeL
 * @version 1.0.1  
 */
public class Main {
    // numero de hilos maxiomos en el programa 
    final private static int numeroHilos = 20;

    // Dependiendo de tu sistema operativo Linux = / Windows \
    public final static String SEPARADOR = "/";

    // Lista que contendra todas las paths 
    private static List<File> paths = new ArrayList<>(); 

    // Lista que contiene el acceso en memoria de los hilos que se lanzan
    private static List<HiloBuscador> hiloBuscadores = new ArrayList<>();

    // Path donde iniciara a buscar 
    public static File pathInicio = new File("/home/javier/Documents/buscador-de-cambios-de-archivos/src");
    // Path donde se guardara la informacion recolectada de los archivos
    public static final File data = new File("data.dat"); // Archivo donde se guardara la informacion

    /**
     * Metodo que lanzara el hilo principal
     * @param args
     */
    public static void main(String[] args) {

        // archivos = recolectarInformacion();
        try {
            HiloBuscador.socket = new Socket("192.168.1.129", 5000);
        } catch (Exception e) {
            System.out.println("No se conecto al servidor");
        }
        setNewPath(pathInicio);
        setNewHilo(new HiloBuscador());

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e + "(Error al dormir el hilo)");
            }
            if (hiloBuscadores.size()==0) {
                setNewPath(pathInicio);
                setNewHilo(new HiloBuscador());
            }

        }
       
    }
    /**
     * Añade una path a la lista
     * @param file
     */
    public static synchronized void setNewPath(File file){
        paths.add(file);
    }
    /**
     * Devuelve la primera path de la lista y la elimina de esta para evitar conflictos
     * @return
     */
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
    /**
     * Lanza y guarda un nuevo hilo que busca objetos
     * @param hiloBuscador
     */
    public static synchronized void setNewHilo(HiloBuscador hiloBuscador){
        if((hiloBuscadores.size()<numeroHilos)){
            hiloBuscadores.add(hiloBuscador);
            hiloBuscador.start();
        }
    }
    /**
     * Elimina de la lista un hilo en especifico
     * @param hiloBuscador
     */
    public static synchronized void removeHilo(HiloBuscador hiloBuscador){
        hiloBuscadores.remove(hiloBuscador);
    }
}