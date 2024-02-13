# Buscador de cambios de archivos

Este es un muy pequeño prollecto bastante potente. Su fin es pasar al programa un directorio raiz y este imprimira por pantalla todo los cambios que este encuentre.

## Uso

Al usarlo por primera vez este imprimira por pantalla que se ha topado con todos los archivos que esa ruta posee. Una vez hecho esto el programa creara un archivo para que el programa al ser lanzado la proxima vez no detecte de nuevo todos los archivos.

Una vez terminado el primer repasao se quedara en modo monitoreo. En el momento en que se realice un cambio en alguno de sus archivos este lo sabra y lo cantara por pantalla.

## Multi Hilo

La finalidad del *multiThread* es hacer que todo sea mas rapido con lo cual las ventajas con la version anterior son enormes:

- Acepta **mas** directorios
- Es mas rapido
- Menor gasto de ram y procesador

Las pricipales diferencias entre uno y otro son:

- Ahora hay un archivo en cada carpeta con informacion de la misma ahora tambien se guarda informacion de las carpetas en dichos archivos cosa que antes no se hacia porque se consideraba inutil *(Lo cual no lo es)*. 

- Cuando una carpeta que contien un conjunto de carpetas es borrada solo muestra la carpeta raiz que ha sido eliminada.

- Los hilos se dividen al encontrar una carpeta si hay mas busquedas la cogen si no mueren asi hasta que todos desaparecen y vuelta ha empezar.

- Los hilos son limitados ha 20 pero esto se puede cambiar en la variable `numeroHilos`.


## Localización

El programa se puede encontrar en el lugar que se dessee ya que pese a tener un archivo que se estara editando siempre este programa lo **ignorara por completo**.