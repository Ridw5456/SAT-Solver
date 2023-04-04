import java.util.*;

public class DPLL {
    public static boolean dpll(ArrayList<HashSet<Integer>> clauses, HashSet<Integer> assigned) {
        // Remove all clauses that are already satisfied
        clauses.removeIf(clause -> clause.stream().anyMatch(assigned::contains));

        // Check if all clauses are satisfied
        if (clauses.stream().allMatch(clause -> clause.isEmpty())) {
            return true; // All clauses are satisfied
        } else if (clauses.stream().anyMatch(clause -> clause.isEmpty())) {
            return false; // Some clause is unsatisfiable
        }

        // Select a literal to assign
        int literal = 0;
        for (HashSet<Integer> clause : clauses) {
            if (clause.size() == 1) {
                literal = clause.iterator().next();
                break;
            }
        }

        // If there is no unit clause, choose a literal with the minimum occurrences
        if (literal == 0) {
            HashMap<Integer, Integer> occurrences = new HashMap<>();
            for (HashSet<Integer> clause : clauses) {
                for (int l : clause) {
                    occurrences.put(l, occurrences.getOrDefault(l, 0) + 1);
                }
            }
            literal = Collections.min(occurrences.keySet(), Comparator.comparingInt(occurrences::get));
        }

        // Try assigning the literal to true
        HashSet<Integer> assignedTrue = new HashSet<>(assigned);
        assignedTrue.add(literal);
        boolean resultTrue = dpll(new ArrayList<>(clauses), assignedTrue);
        if (resultTrue) {
            assigned.addAll(assignedTrue);
            return true;
        }

        // Try assigning the literal to false
        HashSet<Integer> assignedFalse = new HashSet<>(assigned);
        assignedFalse.add(-literal);
        boolean resultFalse = dpll(new ArrayList<>(clauses), assignedFalse);
        if (resultFalse) {
            assigned.addAll(assignedFalse);
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        ArrayList<HashSet<Integer>> clauses = new ArrayList<>();
        HashSet<Integer> clause1 = new HashSet<>(Arrays.asList(1, 2, 3));
        HashSet<Integer> clause2 = new HashSet<>(Arrays.asList(-1, -2));
        HashSet<Integer> clause3 = new HashSet<>(Arrays.asList(3, -2, 4));
        clauses.add(clause1);
        clauses.add(clause2);
        clauses.add(clause3);
        HashSet<Integer> assigned = new HashSet<>();
        boolean result = dpll(clauses, assigned);
        System.out.println(result); // true
        System.out.println(assigned); // [1, 3, -2, 4]
    }
}
