package utilities;

import java.util.ArrayList;

import constants.FilePaths;
import entities.Branch;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * The {@link LoadStaffs} class loads Branch data from the CSV database
 */
public class LoadBranches extends LoadData<Branch>{
    /**
     * The {@link loadDatafromCSV} method in this class loads in Branch data from branch_list.csv 
     * @return a list of Branch objects with information loaded in
     */
    @Override
    public ArrayList<Branch> loadDatafromCSV(){
        ArrayList<Branch> branches = new ArrayList<>();
        ArrayList<String> serialisedData = SerialiseCSV.readCSV(FilePaths.branchListPath.getPath());

        boolean isFirstLine = true; // Flag to skip the header
        for (String s : serialisedData) {
            if (isFirstLine) {
                isFirstLine = false; // Skip the first line (header)
                continue;
            }
            if (s.isEmpty()) continue; // Skip empty lines

            String[] row = s.split(",");
            if (row.length < 4) continue; // Ensure there are enough columns

            try {
                String name = row[0].trim();
                String location = row[1].trim();
                int quota = Integer.parseInt(row[2].trim());
                String status = row[3].trim();

                branches.add(new Branch(name, location, quota, status));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing quota for branch: " + e.getMessage());
                // Optionally handle this error or log it
            }
        }
        return branches;
    }

    /**
     * Add a new branch to the CSV file
     * @param branchName The name of the branch
     * @param location The location of the branch
     * @param quota The staff quota of the branch
     * @param status The status of the branch
     * @return true if the branch was added successfully, false otherwise
     * @author @Theawmaster
     */
    public boolean addBranch(String branchName, String location, int quota, String status) {
        String newBranchData = branchName + "," + location + "," + quota + "," + status;
        return SerialiseCSV.appendToCSV(newBranchData, FilePaths.branchListPath.getPath());
    }

    /**
     * Update the status of a branch in the CSV file
     * @param branchName The name of the branch
     * @param status The new status of the branch
     * @return true if the branch status was updated successfully, false otherwise
     * @author @Theawmaster
     */
    public boolean updateBranchStatus(String branchName, String status) {
        return SerialiseCSV.replaceColumnValue(branchName, 3, status, FilePaths.branchListPath.getPath());
    }

    /**
     * Remove a branch from the CSV file
     * @param branchName The name of the branch to remove
     * @return true if the branch was removed successfully, false otherwise
     * @author @Theawmaster
     */
    public boolean removeBranch(String branchName) {
        return SerialiseCSV.deleteToCSV(branchName, FilePaths.branchListPath.getPath());
    }

    /**
     * Gets the staff quota for a specific branch.
     *
     * @param branchName The name of the branch.
     * @param branchLines The list of lines from the branch list file.
     * @return The staff quota for the branch.
     * @throws IOException 
     * @Author @Theawmaster
     */
    public int getBranchQuota(String branchName) throws IOException {
        List<String> branchLines = Files.readAllLines(Paths.get(FilePaths.branchListPath.getPath()));
        return branchLines.stream()
            .skip(1)
            .map(line -> line.split(","))
            .filter(parts -> parts.length >= 3 && parts[0].trim().equalsIgnoreCase(branchName))
            .findFirst()
            .map(parts -> Integer.parseInt(parts[2].trim()))
            .orElse(0);
    }
    
    public List<String> getBranchNames() throws IOException {
        List<String> branchLines = Files.readAllLines(Paths.get(FilePaths.branchListPath.getPath()));
        return branchLines.stream()
                          .skip(1) // Skip header
                          .map(line -> line.split(",")[0].trim())
                          .collect(Collectors.toList());
    }
    
    public boolean branchExists(String branchName) throws IOException {
        List<String> branchNames = getBranchNames();
        return branchNames.contains(branchName);
    }

}

