package infi2024;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Machine {
    private String machineType; // Use camelCase for variable names
    private List<String> tools; // List to store the names of the tools
    private List<Integer> occupiedDays;
    // Constructor
    public Machine(String machineType) {
        this.machineType = machineType;
        this.tools = new ArrayList<>();
        this.occupiedDays= new ArrayList<>();
        configureTools(machineType);
    }

    private void configureTools(String machineType) {
        switch (machineType) {
            case "M1":
                tools.addAll(Arrays.asList("T1", "T2", "T3"));
                break;
            case "M2":
                tools.addAll(Arrays.asList("T1", "T2", "T3"));
                break;
            case "M3":
                tools.addAll(Arrays.asList("T1", "T4", "T5"));
                break;
            case "M4":
                tools.addAll(Arrays.asList("T1", "T4", "T6"));
                break;
            default:
                System.out.println("Unknown machine type: " + machineType);
                break;
        }
    }

    // Check if the machine has a specific tool
    public boolean hasTool(String tool) {
        return tools.contains(tool);
    }
    
    // Get the minimum occupied day
    public int getMinOccupiedDay() {
        if (occupiedDays.isEmpty()) return 0;
        return occupiedDays.stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
    }

    // Method to add a day to the list
    public void addDay(int day) {
        occupiedDays.add(day);
    }

    // Getter for machineType
    public String getMachineType() {
        return machineType;
    }

    // Setter for machineType
    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    // Getter for tools
    public List<String> getTools() {
        return new ArrayList<>(tools); // Return a copy to preserve encapsulation
    }

    // Add a tool to the list
    public void addTool(String tool) {
        this.tools.add(tool);
    }

    // Remove a tool from the list
    public boolean removeTool(String tool) {
        return this.tools.remove(tool);
    }

    // Getter for occupied days
    public List<Integer> getOccupiedDays() {
        return new ArrayList<>(occupiedDays); // Return a copy of the list to prevent external modifications
    }

    // toString method to return information about the machine
    @Override
    public String toString() {
        return "Machine{" +
            "machineType='" + machineType + '\'' +
            ", tools=" + tools +
            ", occupiedDays=" + occupiedDays +
            '}';
    }

}
