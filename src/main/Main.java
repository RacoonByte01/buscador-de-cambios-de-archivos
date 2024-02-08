package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hilo.HiloBuscador;

/**
 * Main
 * @author JaviLeL
 * @version 1.0.1  
 */
public class Main {
    // numero de hilos maxiomos en el programa 
    final private static int numeroHilos = 20;

    // Se guardara en el mapa la path de los archivos con la vez que fueron modificados
    private static Map<File, String> archivos;

    // Lista que contendra todas las paths 
    private static List<File> paths = new ArrayList<>();

    // Lista que contiene el acceso en memoria de los hilos que se lanzan
    private static List<HiloBuscador> hiloBuscadores = new ArrayList<>();

    // Path donde iniciara a buscar 
    private static File pathInicio = new File("C:\\Users\\javie\\desktop");
    // Path donde se guardara la informacion recolectada de los archivos
    public static final File data = new File("data.dat"); // Archivo donde se guardara la informacion

    /**
     * Metodo que lanzara el hilo principal
     * @param args
     */
    public static void main(String[] args) {

        archivos = recolectarInformacion();

        setNewPath(pathInicio);
        setNewHilo(new HiloBuscador());

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e + "(Error al dormir el hilo)");
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

                guardarInformacion(archivos);
                
                setNewPath(pathInicio);
                setNewHilo(new HiloBuscador());
            }

        }
       
    }

    @SuppressWarnings("unchecked")
    public static Map<File, String> recolectarInformacion(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(data))){
            return (Map<File, String>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Data no creado");
            return new HashMap<>();
        }
    }
    public static void guardarInformacion(Map<File, String> infoMap){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(data))){
            oos.writeObject(infoMap);
        } catch (Exception e) {
            System.out.println(e+" (Error al guardar)");
        }
    }
    // Sincronizo las diferentes acciones para evitar problemas con los hilos

    /**
     * pone en el map un archo con su informacion 
     * @param file
     * @param attr
     */
    public static synchronized void putInMap(File file, String attr){
        archivos.put(file, attr);
    }
    /**
     * Obtiene la informacion de un archivo apartir de su ruta
     * @param file
     * @return
     */
    public static synchronized String getInMap(File file){
        return archivos.get(file);
    }
    /**
     * Elimina del mapa un archivo con su informacion
     * @param file
     */
    public static synchronized void removeInMap(File file){
        archivos.remove(file);
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
    /**
     * Obtiene el conjunto de keys del mapa de forma sincronizada
     * Esto no dunciona pues devuelve la direccion en memoria por lo que es inutil en multi hilo
     * Ya que al iterar puede redimensionarse dando lugar a problemas no es solucionable con un iterador
     * @return
     */
    /*public static synchronized Set<File> getSetKeys(){
        return Main.archivos.keySet();
    }*/
}
