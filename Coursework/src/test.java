import java.util.HashMap;

public class test {
    public static void main(String[] args){
        int[][] clauseDatabase = {{1,5,-3,4,2,-5,2,1,2,-3},{1,4,2,-4,2,4,6,-2,1,4},{5,-5,2,4,-9,2,1,-3},{3,2,1,4,-6,3,-2,-1}};
        HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
        int maxCount = 0;
        int value = 0;
        for (int i = 0; i < clauseDatabase.length; i++){
            for (int j = 0; j < clauseDatabase[i].length; j++){
                int count = map.getOrDefault(clauseDatabase[i][j], 0) + 1;
                map.put(clauseDatabase[i][j], count);
                if (count > maxCount) {
                    maxCount = count;
                    value = clauseDatabase[i][j];
                }
            }
        }
        System.out.println(value+" : "+maxCount);
    }
}