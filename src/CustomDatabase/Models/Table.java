package CustomDatabase.Models;

import lombok.Getter;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public class Table {
    private String tableName;
    private ConcurrentMap<String, Row> rows;
    private Date createdAt;
    private Date modifiedAt;

    public Table(String tableName) {
        this.tableName = tableName;
        this.rows = new ConcurrentHashMap<>();
        this.createdAt = new Date();
        this.modifiedAt = new Date();
    }

    public synchronized void insertEntry(String rowId, Map<String, String> columnsMap){
        if(rows.containsKey(rowId)){
            System.out.println("Duplicate primary key :" + "Insertion failed");
        }
        else {
            Row row = new Row(rowId, columnsMap, new Date(), new Date());
            rows.put(rowId, row);
            System.out.println("Successfully added a row");
        }
    }

    public synchronized void updateEntry(String rowId, Map<String, String> valuesMap){
       Row row = rows.get(rowId);
       valuesMap.forEach((k, v) -> {
           row.getColumns().put(k, v);
       });
       System.out.println("Row successfully updated");
       row.setModifiedAt(new Date());
    }

    public synchronized void deleteEntry(String rowId){
        rows.remove(rowId);
        System.out.println("Row successfully deleted");
    }

    public Map<String, String> readEntry(String rowId){
        return rows.get(rowId).getColumns();
    }


}
