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

    // Getters and Setters
    public String getNumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }

    public String getWorkPiece() {
        return workPiece;
    }

    public void setWorkPiece(String workPiece) {
        this.workPiece = workPiece;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "number='" + number + '\'' +
                ", workPiece='" + workPiece + '\'' +
                ", quantity=" + quantity +
                ", dueDate=" + dueDate +
                '}';
    }
}
