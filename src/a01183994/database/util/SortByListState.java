package a01183994.database.util;

public class SortByListState {
	private static SortByListState instance = null;
	private boolean sortByJoinDate;

    // Private constructor for Singleton pattern
    private SortByListState() {
        sortByJoinDate = false;
    }
    
 // Get the single instance of AppState
    public static SortByListState getInstance() {
        if (instance == null) {
            instance = new SortByListState();
        }
        return instance;
    }

    // Getter and setter for sortByJoinDate
    public boolean isSortByJoinDate() {
        return sortByJoinDate;
    }

    public void setSortByJoinDate(boolean sortByJoinDate) {
        this.sortByJoinDate = sortByJoinDate;
    }

}

