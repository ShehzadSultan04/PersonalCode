package avengers;

public class DFS {
    boolean[] visited; 
    int[][] adjacencyMatrix;
    int numberOfNodes; 

    public DFS(int[][] am, int n){
        numberOfNodes = n; 
        adjacencyMatrix = am; 
        visited = new boolean[numberOfNodes];
    }


    public void checkIsolation(int index){
        visited[index] = true; 
        for(int j = 0; j < adjacencyMatrix.length; j++)
            if (adjacencyMatrix[index][j] == 1 && !visited[j])
                checkIsolation(j);
        

        for (int i = 0; i < adjacencyMatrix.length; i++)
            if (adjacencyMatrix[i][index] == 1 && !visited[i])
            checkIsolation(i);
        
    }

    public boolean connectedCheck(){
        int connectionCount = 0;
        for (int i = 0; i < visited.length; i++){
            if (visited[i])
            connectionCount++;
        }

        if (connectionCount == visited.length)
        return true; 

        else 
        return false; 
    }
}
