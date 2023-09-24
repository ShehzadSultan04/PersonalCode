package avengers;

import java.util.ArrayList;
import java.util.HashMap;

public class Timeline {
    int adjacencyMatrix[][]; 
    int utilityMatrix[];
    int numOfRealities = 0; 

    private HashMap<String, Integer> hashMap = new HashMap<>(); 
    private ArrayList<String> realities = new ArrayList<>(); 

    public Timeline(int[][] am, int[] um){
        adjacencyMatrix = am; 
        utilityMatrix = um; 
        realities.add("0");
        hashMap.put(realities.get(0), utilityMatrix[0]);
    }

    public void findAllRealities(int index){
        for (int i = 0; i < adjacencyMatrix.length; i++){
            if (adjacencyMatrix[index][i] == 1){

                if (index == 0)
                    numOfRealities = 0;

                realities.add(realities.get(numOfRealities) + i);
                hashMap.put(realities.get(realities.size()-1), hashMap.get(realities.get(numOfRealities)) + utilityMatrix[i]);
                numOfRealities = realities.size()-1;

                findAllRealities(i);
            }
        }
        numOfRealities--;
        
    }

    public int getNumOfRealities(){
        return realities.size(); 
    }

    public ArrayList<String> getRealities(){
        return realities; 
    }

    public int getUtilityForReality(int i){
        return hashMap.get(realities.get(i)); 
    }
}
