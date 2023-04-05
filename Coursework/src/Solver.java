// IN1002 Introduction to Algorithms
// Coursework 2022/2023
//
// Submission by
// Ridwan Salim
// Ridwan.Salim@city.ac.uk

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Solver {

    private int [][] clauseDatabase = null;
    private int numberOfVariables = 0;

    /* You answers go below here */

    // Part A.1
    // Worst case complexity : O(v)
    // Best case complexity : O(1)
    public boolean checkClause(int[] assignment, int[] clause) {
        int aMaxIndex = assignment.length-1;
        int abscli;
        for (int i = 0; i < clause.length; i++){
            abscli = Math.abs(clause[i]);
            if (abscli <= aMaxIndex){
                if ((clause[i] > 0 && assignment[abscli] == 1) || (clause[i] < 0 && assignment[abscli] == -1)){
                    return true;
                }
            }
        }
        return false;
    }

    // Part A.2
    // Worst case complexity : O(cl)
    // Best case complexity : O(l)
    public boolean checkClauseDatabase(int[] assignment, int[][] clauseDatabase) {
        for (int i = 0; i < clauseDatabase.length; i++){
            if (checkClause(assignment, clauseDatabase[i]) == false){
                return false;
            }
        }
        return true;
    }

    // Part A.3
    // Worst case complexity : O(v^2)
    // Best case complexity : O(1)
    public int checkClausePartial(int[] partialAssignment, int[] clause) {
        int aMaxIndex = partialAssignment.length-1;
        int abscli;
        int[] check = new int[clause.length];
        for (int i = 0; i < clause.length; i++){
            abscli = Math.abs(clause[i]);
            if (abscli <= aMaxIndex){
                if ((clause[i] > 0 && partialAssignment[abscli] == 1) || (clause[i] < 0 && partialAssignment[abscli] == -1)){
                    return 1;
                }
                else if (partialAssignment[abscli] == 0){
                    return 0;
                }
            }
        }
        return -1;
    }

    // Part A.4
    // Worst case complexity : O(v)
    // Best case complexity : O(1)
    public int findUnit(int[] partialAssignment, int[] clause) {
        // if only one unknown literal and all others are false then return unknown literal
        // else return 0
        int aMaxIndex = partialAssignment.length-1;
        int abscli;
        int ul = 0;
        boolean existsZero = false;
        for (int i = 0; i < clause.length; i++){
            abscli = Math.abs(clause[i]);
            if (abscli <= aMaxIndex){
                if (partialAssignment[abscli] == 0 && existsZero == false){
                    ul = clause[i];
                    existsZero = true;
                }
                else if ((clause[i] > 0 && partialAssignment[abscli] == 1) || (clause[i] < 0 &&
                        partialAssignment[abscli] == -1) || (partialAssignment[abscli] == 0 && existsZero == true)){
                    return 0;
                }
            }
        }
        return ul;
    }

    // Part B
    // I think this can solve ????

    public int[] solve(int[][] clauseDatabase, int[] assignment){

        // immediately returns true if satisfied

        if (checkClauseDatabase(assignment, clauseDatabase) == true){
            return assignment;
        }

        // check if unit clause

        for (int i = 0; i < clauseDatabase.length; i++){
            int checkUnitClause = findUnit(assignment, clauseDatabase[i]);
            if (checkUnitClause != 0){
                int[] newA = Arrays.copyOf(assignment,assignment.length);
                if (checkUnitClause > 0){
                    newA[Math.abs(checkUnitClause)] = 1;
                }
                else {
                    newA[Math.abs(checkUnitClause)] = -1;
                }
                return solve(clauseDatabase,newA);
            }
        }
        return null;
    }

    public int[] checkSat(int[][] clauseDatabase) {

        // create assignment

        int maxIndex = 0;
        for (int i = 0; i < clauseDatabase.length; i++){
            for (int j = 0; j < clauseDatabase[i].length; j++){
                if (Math.abs(clauseDatabase[i][j]) > maxIndex){
                    maxIndex = Math.abs(clauseDatabase[i][j]);
                }
            }
        }
        int[] a = new int[maxIndex+1];
        a[0] = 0;

        // checking for an empty clause

        int numZeros = 0;
        for (int i = 0; i < clauseDatabase.length; i++){
            for (int j = 0; j < clauseDatabase[i].length; j++){
                if (clauseDatabase[i][j] == 0){
                    numZeros++;
                    if (numZeros == clauseDatabase[i].length){
                        return null;
                    }
                }
            }
            numZeros = 0;
        }

        // directly assign literals, check pure

        // this takes a majority between the literal and its negation
        // then assigns the higher value
        // it would reach end case much faster if the majority literal
        // was true over its negation

        HashSet<Integer> checked = new HashSet<Integer>();
        HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();

        for (int i = 0; i < clauseDatabase.length; i++){
            for (int j = 0; j < clauseDatabase[i].length; j++){
                int count = map.getOrDefault(clauseDatabase[i][j],0)+1;
                map.put(clauseDatabase[i][j],count);
            }
        }

        for (int i = 0; i < clauseDatabase.length; i++){
            for (int j = 0; j < clauseDatabase[i].length; j++){
                // if the abs(current literal) is not in checked then
                // add to checked, so it doesnt get evaluated again
                // if map contains literal's negation then
                // check which one is more frequent
                // assign to the assignment

                if (checked.contains(Math.abs(clauseDatabase[i][j])) == false){
                    checked.add(Math.abs(clauseDatabase[i][j]));
                    if (map.containsValue(clauseDatabase[i][j] * -1)){
                        int pos = 0;
                        int neg = 0;
                        // positive
                        if (clauseDatabase[i][j] > 0){
                            pos = map.getOrDefault(clauseDatabase[i][j],0);
                            neg = map.getOrDefault(-clauseDatabase[i][j],0);
                        }
                        // negative
                        else if (clauseDatabase[i][j] < 0){
                            pos = map.getOrDefault(-clauseDatabase[i][j],0);
                            neg = map.getOrDefault(clauseDatabase[i][j],0);
                        }

                        if (pos > neg){
                            a[Math.abs(clauseDatabase[i][j])] = 1;
                        }
                        else if (neg > pos){
                            a[Math.abs(clauseDatabase[i][j])] = -1;
                        }
                        // they are both the same frequency
                        else{
                            a[Math.abs(clauseDatabase[i][j])] = 1;
                        }
                    }

                    // it is a pure literal
                    else{
                        if (clauseDatabase[i][j] > 0){
                            a[Math.abs(clauseDatabase[i][j])] = 1;
                        }
                        else if (clauseDatabase[i][j] < 0){
                            a[Math.abs(clauseDatabase[i][j])] = -1;
                        }
                    }
                }
            }
        }

        // check if the assignment contains any unknowns
        // assign them to 1 to prevent an unknown assignment error

        for (int i = 1; i < a.length; i++){
            if (a[i] == 0){
                a[i] = 1;
            }
        }

        // start recursion
        return solve(clauseDatabase, a);
    }

    /*****************************************************************\
     *** DO NOT CHANGE! DO NOT CHANGE! DO NOT CHANGE! DO NOT CHANGE! ***
     *******************************************************************
     *********** Do not change anything below this comment! ************
     \*****************************************************************/

    public static void main(String[] args) {
        try {
            Solver mySolver = new Solver();

            System.out.println("Enter the file to check");

            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String fileName = br.readLine();

            int returnValue = 0;

            Path file = Paths.get(fileName);
            BufferedReader reader = Files.newBufferedReader(file);
            returnValue = mySolver.runSatSolver(reader);

            return;

        } catch (Exception e) {
            System.err.println("Solver failed :-(");
            e.printStackTrace(System.err);
            return;

        }
    }

    public int runSatSolver(BufferedReader reader) throws Exception, IOException {

        // First load the problem in, this will initialise the clause
        // database and the number of variables.
        loadDimacs(reader);

        // Then we run the part B algorithm
        int [] assignment = checkSat(clauseDatabase);

        // Depending on the output do different checks
        if (assignment == null) {
            // No assignment to check, will have to trust the result
            // is correct...
            System.out.println("s UNSATISFIABLE");
            return 20;

        } else {
            // Cross check using the part A algorithm
            boolean checkResult = checkClauseDatabase(assignment, clauseDatabase);

            if (checkResult == false) {
                throw new Exception("The assignment returned by checkSat is not satisfiable according to checkClauseDatabase?");
            }

            System.out.println("s SATISFIABLE");

            // Check that it is a well structured assignment
            if (assignment.length != numberOfVariables + 1) {
                throw new Exception("Assignment should have one element per variable.");
            }
            if (assignment[0] != 0) {
                throw new Exception("The first element of an assignment must be zero.");
            }
            for (int i = 1; i <= numberOfVariables; ++i) {
                if (assignment[i] == 1 || assignment[i] == -1) {
                    System.out.println("v " + (i * assignment[i]));
                } else {
                    throw new Exception("assignment[" + i + "] should be 1 or -1, is " + assignment[i]);
                }
            }

            return 10;
        }
    }

    // This is a simple parser for DIMACS file format
    void loadDimacs(BufferedReader reader) throws Exception, IOException {
        int numberOfClauses = 0;

        // Find the header line
        do {
            String line = reader.readLine();

            if (line == null) {
                throw new Exception("Found end of file before a header?");
            } else if (line.startsWith("c")) {
                // Comment line, ignore
                continue;
            } else if (line.startsWith("p cnf ")) {
                // Found the header
                String counters = line.substring(6);
                int split = counters.indexOf(" ");
                numberOfVariables = Integer.parseInt(counters.substring(0,split));
                numberOfClauses = Integer.parseInt(counters.substring(split + 1));

                if (numberOfVariables <= 0) {
                    throw new Exception("Variables should be positive?");
                }
                if (numberOfClauses < 0) {
                    throw new Exception("A negative number of clauses?");
                }
                break;
            } else {
                throw new Exception("Unexpected line?");
            }
        } while (true);

        // Set up the clauseDatabase
        clauseDatabase = new int[numberOfClauses][];

        // Parse the clauses
        for (int i = 0; i < numberOfClauses; ++i) {
            String line = reader.readLine();

            if (line == null) {
                throw new Exception("Unexpected end of file before clauses have been parsed");
            } else if (line.startsWith("c")) {
                // Comment; skip
                --i;
                continue;
            } else {
                // Try to parse as a clause
                ArrayList<Integer> tmp = new ArrayList<Integer>();
                String working = line;

                do {
                    int split = working.indexOf(" ");

                    if (split == -1) {
                        // No space found so working should just be
                        // the final "0"
                        if (!working.equals("0")) {
                            throw new Exception("Unexpected end of clause string : \"" + working + "\"");
                        } else {
                            // Clause is correct and complete
                            break;
                        }
                    } else {
                        int var = Integer.parseInt(working.substring(0,split));

                        if (var == 0) {
                            throw new Exception("Unexpected 0 in the middle of a clause");
                        } else {
                            tmp.add(var);
                        }

                        working = working.substring(split + 1);
                    }
                } while (true);

                // Add to the clause database
                clauseDatabase[i] = new int[tmp.size()];
                for (int j = 0; j < tmp.size(); ++j) {
                    clauseDatabase[i][j] = tmp.get(j);
                }
            }
        }

        // All clauses loaded successfully!
        return;
    }

}