package CustomDatabase.Models;

import javafx.scene.control.Tab;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Database {
    private String name;
    private Map<String, Table> tableMap;
    private Date createdAt;

    public Database(String name) {
        this.name = name;
        tableMap = new HashMap<>();
        createdAt = new Date();
    }

    public Table createTable(String tableName){
        if(tableMap.containsKey(tableName)){
            System.out.println("A table already contains with same name");
        }
        else {
            Table table = new Table(tableName);
            tableMap.put(tableName, table);
            System.out.println("Created a new table with name "+ tableName);
        }

        return tableMap.get(tableName);
    }

    public void dropTable(String tableName){
        tableMap.remove(tableName);
        System.out.println("Table successfully dropped");
    }
}
