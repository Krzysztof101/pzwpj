

package model1.Category;

public class Category implements Cloneable {
    private int id;
    private String categoryName;
    private String description;
    private final static String NO_DESCRIPTION="";
    public static String  getNoDescription()
    {
        return NO_DESCRIPTION;
    }
    public boolean noDescription()
    {
        return description.equals(NO_DESCRIPTION);
    }
    public Category(int id, String name, String description)
    {
        this.id = id;
        this.categoryName = name;
        this.description = description;
    }
    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int getId() {return id;}
    public String getCategoryName() {return categoryName; }
    public String getDescription() {return description; }
    public void setCategoryName(String categoryName){this.categoryName = categoryName;}
    public void setDescription(String description) {this.description = description;}
    public void setId(int newId) { this.id = newId; }

    public String toString()
    {
        return "Category: id: "+Integer.toString(id) + ", name: "+categoryName +", description: "+description;
    }
}
