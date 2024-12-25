package sg.edu.nus.iss.ssf_project_potluck_shamus.model;

public class CategoryModel 
{
    private String id;
    private String category;
    private String thumbnailLink;
    private String description;

    // CONSTRUCTORS
    public CategoryModel() {
    }

    public CategoryModel(String id, String category, String thumbnailLink, String description) {
        this.id = id;
        this.category = category;
        this.thumbnailLink = thumbnailLink;
        this.description = description;
    }

    // GETTERS SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

    
}
