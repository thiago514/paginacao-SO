package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;

import entities.Quadro;

class Program {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner file = new Scanner(new File("file.txt"));
        String algoritimo = file.nextLine();
        int faults = 0;
        int size = file.nextInt();
        file.nextLine();
        System.out.println(algoritimo);
        switch (algoritimo) {
            case "FIFO":
                faults = FIFO(file, size);
                break;
            case "SC":
                faults = SC(file, size);
                break;

            case "CLOCK":
                faults = Clock(file, size);
                break;
            default:
                System.out.println("não suportado");

                break;

        }

        System.out.println("Total Page Fault = " + faults);

    }

    public static int FIFO(Scanner file, int size) {
        int faults = 0;
        Queue<Quadro> fila = new LinkedList<>();
        while (file.hasNextLine()) {
            int id = file.nextInt();
            Quadro q = new Quadro(id);
            file.nextLine();
            if (!fila.contains(q)) {
                System.out.println("Page Fault - Página " + q);

                if (fila.size() == size) {
                    fila.remove();
                }
                fila.add(q);
                faults++;
            } else {
                System.out.println("Page Hit - Página " + q);
            }

        }
        return faults;
    }

    public static int SC(Scanner file, int size) {
        int faults = 0;
        Queue<Quadro> fila = new LinkedList<>();
        Quadro q = new Quadro(Integer.MAX_VALUE);
        while (file.hasNextLine()) {
            int id = file.nextInt();
            q = new Quadro(id);
            file.nextLine();
            if (!fila.contains(q)) {
                while (fila.size() == size) {
                    Quadro rmQ = fila.remove();
                    if (rmQ.getSecondChance() == 1) {
                        System.out.println("id: " + rmQ.toString() + " used your second chance");
                        rmQ = new Quadro(rmQ.getid());
                        fila.add(rmQ);
                    }
                }

                System.out.println("Page Fault - Página " + q);

                fila.add(q);
                faults++;
            } else {

                final Quadro fQ = q;
                fila = new LinkedList<>(fila.stream().map(p -> {
                    if (p.getid() == fQ.getid()) {
                        System.out.println("id: " + p + " win second chance");
                        p.addSecondChance();
                    }
                    return p;
                }).toList());
                System.out.println("Page Hit - Página " + q);
            }

            for (Quadro el : fila) {
                System.out.println("id: " + el);
            }
        }

        return faults;
    }


    public static int Clock(Scanner file, int size) {
        int faults = 0;
        List<Quadro> lista = new ArrayList<>();
        int clock = 0;
        while (file.hasNextLine()) {
            int id = file.nextInt();
            Quadro q = new Quadro(id);
            file.nextLine();
            boolean inside = false;
            System.out.println(lista.size());
            if (!lista.contains(q)) {
                if (lista.size() == size) {
                    inside = false;
                    while (!inside) {
                        final Quadro rmQ = lista.get((clock%4));
                        if (rmQ.getSecondChance() == 1) {
                            System.out.println("id: " + rmQ.toString() + " used your second chance");
                            lista = new ArrayList<>(lista.stream().map(p -> {
                                if (p.getid() == rmQ.getid()) {
                                    System.out.println("id: " + p + " win second chance");
                                    p.removeSecondChance();
                                }
                                return p;
                            }).toList());

                        } else {
                            lista.remove((clock%4));
                            lista.set((clock%4), new Quadro(id));
                            inside = true;

                        }
                        clock += 1;
                    }
                }else{
                    lista.add(q);
                }
                
                if(lista.size() < size){
                    lista.add(q);
                }else{
                    lista.remove((clock%4));
                    lista.set((clock%4), q);
                }

                System.out.println("Page Fault - Página " + q);
                System.out.println("Clock: " + clock);
                faults++;
                clock++;
            } else {
                    System.out.println("Page Hit - Página " + q);
                    System.out.println("Clock: " + clock);
                    final Quadro fQ = q;
                    lista = lista.stream().map(p -> {
                        if (p.getid() == fQ.getid()) {
                            System.out.println("id: " + p + " win second chance");
                            p.addSecondChance();
                        }
                        return p;
                    }).toList();
                }
        }




        return faults;
    }
}
