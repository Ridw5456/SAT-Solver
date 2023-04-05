import java.util.HashMap;

public class test {
    public static int findUnit(int[] partialAssignment, int[] clause) {
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


    public static void main(String[] args){
        int[] pa = {0,1,1,0,-1,-1};
        int[] cl = {-1,-2,-3,4,5};
        System.out.println(findUnit(pa,cl));

        //HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
//        int maxCount = 0;
//        int value = 0;
//
//        for (int i = 0; i < clauseDatabase.length; i++){
//            for (int j = 0; j < clauseDatabase[i].length; j++){
//                int count = map.getOrDefault(clauseDatabase[i][j], 0) + 1;
//                map.put(clauseDatabase[i][j], count);
//                if (count > maxCount) {
//                    maxCount = count;
//                    value = clauseDatabase[i][j];
//                }
//            }
//        }
//        // if not yet assigned, assign it and recursion
//        if (assignment[Math.abs(value)] == 0){
//            return getInts(clauseDatabase, assignment, value);
//        }
//        else{
//            for (int i = 1; i < assignment.length; i++){
//                // check if the current literal is unassigned
//                // if it isnt, assign true / false values and into recursion
//                if (assignment[i] == 0){
//                    int[] newAT = Arrays.copyOf(assignment,assignment.length);
//                    newAT[assignment[i]] = 1;
//                    int[] t = solve(clauseDatabase,newAT);
//                    int[] newAF = Arrays.copyOf(assignment,assignment.length);
//                    newAF[assignment[i]] = -1;
//                    int[] f = solve(clauseDatabase,newAF);
//                    if (t != null){
//                        return t;
//                    }
//                    if (f != null){
//                        return f;
//                    }
//                    return null;
//                }
//            }
//        }

    }
}