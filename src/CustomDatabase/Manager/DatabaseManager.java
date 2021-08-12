package CustomDatabase.Manager;

import CustomDatabase.Models.Database;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DatabaseManager {
    private Map<String, Database> databaseMap;

    public DatabaseManager() {
        databaseMap = new HashMap<>();
    }

    public Database createDatabase(String databaseName){
        if(databaseMap.containsKey(databaseName)){
            System.out.println("A database exists with given name");
        }
        else {
            databaseMap.put(databaseName, new Database(databaseName));
        }
        return databaseMap.get(databaseName);
    }

    public void deleteDatabase(String databaseName) {
        databaseMap.remove(databaseName);
    }
}
