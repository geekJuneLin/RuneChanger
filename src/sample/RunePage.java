package sample;

public class RunePage {
    String[] autoModifiedSelections;
    boolean current;
    int id;
    boolean isActive;
    boolean isDeletable;
    boolean isEditable;
    boolean isValid;
    String lastModified;
    String name;
    String order;
    int primaryStyleId;
    int[] selectedPerkIds;
    int subStyleId;

    public RunePage(String[] auto, boolean curr, int id, boolean isActive, boolean isDeletable, boolean isEditable, boolean isValid, String lastModified, String name, String order, int primaryStyleId, int[] selectedPerkIds, int subStyleId){
        this.autoModifiedSelections = auto;
        this.current = curr;
        this.id = id;
        this.isActive = isActive;
        this.isDeletable = isDeletable;
        this.isEditable = isEditable;
        this.isValid = isValid;
        this.lastModified = lastModified;
        this.name = name;
        this.order = order;
        this.primaryStyleId = primaryStyleId;
        this.selectedPerkIds = selectedPerkIds;
        this.subStyleId = subStyleId;
    }
}

