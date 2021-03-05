package dto;

import java.util.ArrayList;

public class RecordDTO implements Comparable<RecordDTO>{
    String id;
    ArrayList<String> entries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<String> entries) {
        this.entries = entries;
    }

    @Override
    public int compareTo(RecordDTO o) {
        return id.compareTo(o.getId());
    }
}
