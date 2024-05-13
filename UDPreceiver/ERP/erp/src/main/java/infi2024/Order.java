package infi2024;

public class Order {
    String number;
    String workPiece;
    int quantity;
    int dueDate;
    int latePenalty;
    int earlyPenalty;
    String clientNameId;

    public Order(String number, String workPiece, int quantity, int dueDate, int latePenalty, int earlyPenalty, String clientNameId) {
        this.number = number;
        this.workPiece = workPiece;
        this.quantity = quantity;
        this.dueDate = dueDate;
        this.latePenalty = latePenalty;
        this.earlyPenalty = earlyPenalty;
        this.clientNameId = clientNameId;
    }
}
