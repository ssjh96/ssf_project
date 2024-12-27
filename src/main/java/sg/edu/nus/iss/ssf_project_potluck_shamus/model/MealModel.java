package sg.edu.nus.iss.ssf_project_potluck_shamus.model;

import java.util.List;

public class MealModel 
{
    private String id;
    private String name;
    private String category;
    private String area;
    private String instructions;
    private String thumbnailLink;
    private String tags;
    private String youtubeLink;
    private List<String> ingredients;
    private List<String> measurements;
    private String source;

    // CONSTRUCTORS
    public MealModel() {
    }
    
    public MealModel(String id, String name, String category, String area, String instructions, String thumbnailLink,
            String tags, String youtubeLink, List<String> ingredients, List<String> measurements, String source) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.thumbnailLink = thumbnailLink;
        this.tags = tags;
        this.youtubeLink = youtubeLink;
        this.ingredients = ingredients;
        this.measurements = measurements;
        this.source = source;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<String> measurements) {
        this.measurements = measurements;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
