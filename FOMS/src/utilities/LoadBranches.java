package utilities;

import java.util.ArrayList;

import constants.FilePaths;
import entities.Branch;

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

    // Add a new branch to the CSV
    public boolean addBranch(String branchName, String location, int quota, String status) {
        String newBranchData = branchName + "," + location + "," + quota + "," + status;
        return SerialiseCSV.appendToCSV(newBranchData, FilePaths.branchListPath.getPath());
    }

    // Update the status of an existing branch
    public boolean updateBranchStatus(String branchName, String status) {
        return SerialiseCSV.replaceColumnValue(branchName, 3, status, FilePaths.branchListPath.getPath());
    }

    // Remove a branch from the CSV
    public boolean removeBranch(String branchName) {
        return SerialiseCSV.deleteToCSV(branchName, FilePaths.branchListPath.getPath());
    }

}

