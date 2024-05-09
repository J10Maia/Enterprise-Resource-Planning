import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Order Encomenda1 = new Order("orderNumber1", "P6", 1, 7);
        List <Machine> list = new ArrayList<>();

        list.add(new Machine("M1"));
        list.add(new Machine("M2"));
        list.add(new Machine("M3"));
        list.add(new Machine("M4"));

        MachineManager mm = new MachineManager(list);
        Piece p = new Piece(Encomenda1.getWorkPiece());
        List<Piece> listP = p.getProduction(Encomenda1.getDueDate(),mm);

        for (Piece piece : listP) {
            System.out.println(piece);
        }

         //
         System.out.println("");
         //

        Order Encomenda2 = new Order("orderNumber1", "P9", 1, 7);

        Piece p2 = new Piece(Encomenda2.getWorkPiece());
        List<Piece> listP2 = p2.getProduction(Encomenda2.getDueDate(),mm);

        for (Piece piece : listP2) {
            System.out.println(piece);
        }

        //
        System.out.println("");
        //

        // Order Encomenda3 = new Order("orderNumber1", "P4", 1, 7);

        //  Piece p3 = new Piece(Encomenda3.getWorkPiece());
        // List<Piece> listP3 = p3.getProduction(Encomenda3.getDueDate(),mm);

        // for (Piece piece : listP3) {
        //     System.out.println(piece);
        // }

        //
        System.out.println("");
        //

        for (Machine m : list) {
            System.out.println(m);
        }

    }
}
