import java.util.List;

public class MachineManager {

    private List<Machine> machines;

    public MachineManager(List<Machine> machines) {
        this.machines = machines;
    }

    public int findLeastOccupiedMachineWithTool(String tool, int maxDay) {
        Machine selectedMachine = null;
        int highestAvailableDay = 0;
    
        for (Machine machine : this.machines) {
            if (machine.hasTool(tool)) {
                // Get the highest occupied day less than maxDay
                int maxUnderMaxDay = machine.getOccupiedDays().stream()
                                            .filter(day -> day < maxDay)
                                            .max(Integer::compare)
                                            .orElse(-1); // No valid days found
    
                // If no occupied days and tool matches, use maxDay
                if (maxUnderMaxDay == -1 && machine.getOccupiedDays().isEmpty()) {
                    machine.addDay(maxDay);
                    return maxDay;
                }
    
                // Find the next highest day that is still under maxDay
                if (maxUnderMaxDay != -1 && maxUnderMaxDay > highestAvailableDay) {
                    highestAvailableDay = maxUnderMaxDay;
                    selectedMachine = machine;
                }
            }
        }
    
        // If a suitable machine is found
        if (selectedMachine != null && highestAvailableDay > 0) {
            if (highestAvailableDay < maxDay) {
                selectedMachine.addDay(highestAvailableDay);
                return highestAvailableDay;
            }
        }
    
        // If no suitable machine is found or the only option is to use the maxDay
        if (selectedMachine != null && highestAvailableDay == 0) {
            selectedMachine.addDay(maxDay);
            return maxDay;
        }
    
        // No suitable machine found or all machines are fully occupied below maxDay
        return -1;
    }
    

    }

