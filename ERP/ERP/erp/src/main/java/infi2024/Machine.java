package infi2024;

import java.util.List;
import java.util.ArrayList;

public class Machine {
    private int id;
    private String machineType;
    private List<String> tools;
    private List<Integer> occupiedDays;

    public Machine(int id, String machineType, List<String> tools, List<Integer> occupiedDays) {
        this.id = id;
        this.machineType = machineType;
        this.tools = tools != null ? tools : new ArrayList<>();
        this.occupiedDays = occupiedDays != null ? occupiedDays : new ArrayList<>();
    }

    public boolean hasTool(String tool) {
        return this.tools.contains(tool);
    }
    
    public int getMinOccupiedDay() {
        if (occupiedDays.isEmpty()) return 0;
        return occupiedDays.stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
    }

    public void addDay(int day) {
        occupiedDays.add(day);
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public List<String> getTools() {
        return new ArrayList<>(tools);
    }

    public List<Integer> getOccupiedDays() {
        return new ArrayList<>(occupiedDays);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Machine{" +
            "id=" + id +
            ", machineType='" + machineType + '\'' +
            ", tools=" + tools +
            ", occupiedDays=" + occupiedDays +
            '}';
    }
}
