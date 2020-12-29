//Instituto Politecnico Nacional 
//Escuela Superior de Computo 
//Teoria Computacional. 2CV1
//Profr. Benjamin Luna Benoso 
//Gloria Oliva Olivares Menez

/*Funcionamiento: Las configuraciones están en el archivo .rar con los nombres de C1.txt, C2.txt y C3.txt 
                  Para compilar el programa, se hace a través de la terminal (Linux) o del cmd (Windows).
                  Primero se coloca el comando javac seguido del nombre del archivo con la extension
                  .java, por tanto, quedaria de la siguiente forma: javac EvalAFD.java 
                  Posteriormente para ejecutarlo, se coloca el comando java, el nombre del archivo sin la 
                  extension y finalmente el nombre del archivo con la configuracion. Es decir: 
                  java EvalAFD C1.txt*/

/*Configuraciones: C1 -> Acepta las cadenas que tienen un numero par de 0's
                   C2 -> Contiene cadenas terminadas en 00
                   C3 -> Son cadenas que tienen tres 0's consecutivos, no necesariamente al final */
                  

import java.util.Arrays;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

public class EvalAFD{

    public int numColum;
    private String ini, edoActual;
    private String[] fin,symbols;
    private HashMap<String,String[]> table;
       
    public EvalAFD(String name){
        table = new HashMap<String,String[]>();
        try{
            // Se crea y se abre un fichero.
            File inputFile = new File(name);

            // Se crea un lector del archivo y un buffer
            // que contendrá el texto del archivo
            FileReader fr = new FileReader(inputFile);
            BufferedReader br = new BufferedReader(fr);

            // Se lee linea por linea el archivo
            String linea;
            String[] aux;
            int i = 0;
            linea = br.readLine();
            while(linea != null){
                // Se divide la cadena y se guarda en un array
                String dato[] = linea.split(",");
                if(i == 0){
                    if(linea.charAt(0) == '@'){
                        this.numColum = dato.length;
                        symbols = Arrays.copyOfRange(dato, 1, this.numColum);
                    }else{
                        System.out.println("Archivo no valido");
                        System.exit(0);
                    }
                }else if(dato[0].equals("Inicio")){
                    ini = dato[1];
                    edoActual = dato[1];
                }else if(dato[0].equals("Final")){
                    fin = Arrays.copyOfRange(dato, 1, dato.length);
                }else{
                    aux = Arrays.copyOfRange(dato, 1, this.numColum);
                    table.put(dato[0], aux);
                }
                linea = br.readLine();
                i++;
            }
        
            table.remove("");   // Elimino espacio en blanco

            // Se cierra el lector del archivo
            fr.close();

        }catch(FileNotFoundException e){
            System.err.println("ArchivoText: " + e);
            System.exit(0);
        }catch(IOException e){
            System.err.println("ArchivoText: " + e);
            System.exit(0);
        }
    }

    public void showData() {            //Este método muestra la tabla de transiciones 
        String[] aux;
        System.out.print("Alfabeto: ");       //Muestra el lenguaje que acepta 
        for(int i=0;i<this.numColum-1;i++){
            System.out.print(symbols[i]+" ");   
        }
        System.out.println();
        for (String i : table.keySet()) {           //Obtiene el estado y muestra las transiciones 
            System.out.print("Estado: " + i + "; Transicion: ");  //El estado funge como la llave del HashMap (aqui nombrado table)
            aux = table.get(i);
            for(int j=0; j<this.numColum-1;j++){
                System.out.print(aux[j]+",");
            }
            System.out.println();
        }
        //Muestra estados iniciales y finales 
        System.out.println("Estado inicial: "+ini);  
        System.out.print("Estados finales: ");
        for(int i=0;i<this.fin.length;i++){
            System.out.print(fin[i]+",");
        }
        System.out.println();
    }

    //Va a realizar las transiciones 
    //Se tiene un atributo llamado estado actual y va a guardarse en una variable que va a estar moviendose a traves de la cadena
    public void transicion(String[] edos,int index){
        this.edoActual = edos[index];
    }

    //Se va a analizar la cadena para ver si es aceptada o no por el automata
    public void analizarCadena(){
        String cadena;
        Scanner cad = new Scanner(System.in);
        System.out.print("Ingrese la cadena: ");
        cadena = cad.nextLine();
        for(int i=0; i<cadena.length(); i++){        // va a leer la cadena elemento por elemento 
            int indAux = -1;                         //se establece un indice auxiliar 
            for(int j=0; j<numColum-1; j++){        //Va a pasar por cada columna e imprimira los caracteres 
                if(cadena.charAt(i) == this.symbols[j].charAt(0)){
                    indAux = j;
                    break;
                }
            }
            
            if(indAux != -1){        //Si el indice auxiliar es diferente a -1                                       
                String[] auxEdo = this.table.get(this.edoActual);    //Se va a instanciar un estado auxiliar
                this.transicion(auxEdo,indAux);  //Se van a realizar las transiciones con el indice y el estado auxiliar 
            }else{
                System.out.println("No pertenece al alfabeto");  //De otra forma, como no pertenence al alfabeto, termina el programa
                System.exit(0);
            }
           
        }
        for(int i=0;i<fin.length;i++){   //Si el estado actual de la cadena es igual al estado final, la cadena es compatible
            if(this.edoActual.equals(this.fin[i])){
                System.out.println("Cadena compatible");
                System.out.println("Estado final: "+this.fin[i]);    //imprime el estado final 
                System.exit(0);
            }
        }
        System.out.println("Cadena no compatible");  //Si el estado actual de la cadena no es igual al estado final, la cadena no es compatible
    }

    public static void main(String[] args) {   //Se hace uso de los metodos 
        EvalAFD afd = new EvalAFD(args[0]);
        afd.showData();
        afd.analizarCadena();
    }
}