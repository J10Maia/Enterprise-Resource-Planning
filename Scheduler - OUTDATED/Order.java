public class Order {
    private String orderNumber;
    private String workPiece;
    private int quantity;
    private int dueDate;

    // Constructor
    public Order(String orderNumber, String workPiece, int quantity, int dueDate) {
        this.orderNumber = orderNumber;
        this.workPiece = workPiece;
        this.quantity = quantity;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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
                "orderNumber='" + orderNumber + '\'' +
                ", workPiece='" + workPiece + '\'' +
                ", quantity=" + quantity +
                ", dueDate=" + dueDate +
                '}';
    }
}
