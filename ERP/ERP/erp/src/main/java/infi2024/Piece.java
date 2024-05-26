package infi2024;
import java.util.ArrayList;
import java.util.List;

public class Piece {

    // dia em que tem que comecar
    private int day;

    // tempo que a peca vai demorar a ser construida ou a chegar
    private int time;

    private String tool;

    // "P1", "P2", etc..
    private String workPiece;
    
    public Piece(String workPiece) {
        this.day = 0;
        switch(workPiece){
            case "P1":
                this.time = 60;
                break;
            case "P2":
                this.time = 60;
                break;
            case "P3":
                this.time = 45;
                this.tool = "T1";
                break;
            case "P4":
                this.time = 25;
                this.tool = "T2";
                break;
            case "P5":
                this.time = 25;
                this.tool = "T4";
                break;
            case "P6":
                this.time = 25;
                this.tool = "T2";
                break;
            case "P7":
                this.time = 15;
                this.tool = "T3";
                break;
            case "P8":
                this.time = 45;
                this.tool = "T1";
                break;
            case "P9":
                this.time = 45;
                this.tool = "T5";
                break;
        }
        this.workPiece = workPiece;
    }
    
    public String getWorkPiece(){
        return this.workPiece;
    }

    public int getDay(){
        return this.day;
    }

    public Piece getPreviousPiece() {
        switch (workPiece) {
            case "P1":
                return null;
            case "P2":
                return null;  // Both P1 and P2 have no previous piece
    
            case "P3":
                return new Piece("P1");
    
            case "P4":
                return new Piece("P3");
    
            case "P5":
                return new Piece("P4");
            case "P6":
                return new Piece("P4");
            case "P7":
                return new Piece("P4");
            case "P8":
                return new Piece("P2"); 
    
            case "P9":
                return new Piece("P8");
    
            default:
                return null;  // Improved default case to handle unexpected inputs
        }
    }
    public int totalTime(){
        Piece p = this.getPreviousPiece();
        if (p == null) return 1;
        else return p.totalTime() + 1;
    }

    // Damos o dia em que a peca tem que estar pronta e coloca a variavel local do dia da peca para quando a peca tem que ser comecada a contruir
    private void changeDay(int day){
       this.day = (int) Math.floor((day*60 - this.time)/60) ;
    }

    // Dada uma peca e um dia final, devolve uma lista com todas as pecas necessarias para a sua construcao, tendo em conta que estas pecas ja teem num metodo o dia em que tem que comecar a sua producao
    public List<Piece> getProduction(int finalDay, MachineManager mm) {
        List<Piece> list = new ArrayList<>();
        Piece p = this;
        int n = finalDay;
    
        
        while (p != null) {
            if (p.tool != null) {
                int startDay = mm.findLeastOccupiedMachineWithTool(p.tool, n);
                if (startDay == -1) {
                    System.out.println("No available machine found for " + p.workPiece + " with tool " + p.tool + " before day " + n);
                    return new ArrayList<>(); // or handle this case appropriately
                }
                n = startDay; // Adjust n to the latest available start day found by the MachineManager
            }
            p.changeDay(n); // Set the start day based on the available day found
            if (p.day < 0) {
                System.out.println("Scheduled day for " + p.workPiece + " is negative, which is not possible.");
                return new ArrayList<>(); // or handle this case appropriately
            }
            list.add(0, p); // Add to the front to maintain correct order
            n = p.day; // Set next n to one day before the current piece's start day
            p = p.getPreviousPiece();
        }
        return list;
    }
    

    @Override
    public String toString() {
        return "Piece{" +
            "workPiece='" + workPiece + '\'' +
            ", day=" + day +
            ", time=" + time +
            '}';
    }


}
