package CustomDatabase.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Row {
    private String rowId;
    private Map<String, String> columns;
    private Date CreatedAt;
    @Setter
    private Date ModifiedAt;

    public Row(String rowId, Map<String, String> columns, Date createdAt, Date modifiedAt) {
        this.rowId = rowId;
        this.columns = columns;
        CreatedAt = createdAt;
        ModifiedAt = modifiedAt;
    }
}
