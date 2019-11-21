package model;

public class RunePage {
    public String[] autoModifiedSelections;
    public boolean current;
    public int id;
    public boolean isActive;
    public boolean isDeletable;
    public boolean isEditable;
    public boolean isValid;
    public String lastModified;
    public String name;
    public String order;
    public int primaryStyleId;
    public int[] selectedPerkIds;
    public int subStyleId;

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

