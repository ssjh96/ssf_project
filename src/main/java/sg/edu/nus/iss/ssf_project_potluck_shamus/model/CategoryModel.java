package sg.edu.nus.iss.ssf_project_potluck_shamus.model;

public class CategoryModel 
{
    private String id;
    private String name;
    private String thumbnailLink;
    private String description;

    // CONSTRUCTORS
    public CategoryModel() {
    }

    public CategoryModel(String id, String name, String thumbnailLink, String description) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
