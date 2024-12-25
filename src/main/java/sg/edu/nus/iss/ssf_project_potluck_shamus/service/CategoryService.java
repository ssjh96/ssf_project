package sg.edu.nus.iss.ssf_project_potluck_shamus.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Url;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.CategoryModel;

@Service
public class CategoryService {

    @Value("${mealdb.api.key}")
    private String apiKey;

    public List<CategoryModel> fetchCategories()
    {
        System.out.println("Getting all categories...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
                
        String url = String.format("%s/%s/categories.php", Url.mealApiBaseUrl, apiKey);
        System.out.println(url);

        List<CategoryModel> categories = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JsonReader jReader = Json.createReader(new StringReader(response));
        JsonObject jResponse = jReader.readObject();

        JsonArray categoriesArray = jResponse.getJsonArray("categories");

        for (int i = 0; i < categoriesArray.size(); i++)
        {
            CategoryModel category = new CategoryModel();
            JsonObject jCategory = categoriesArray.getJsonObject(i);

            category.setId(jCategory.getString("idCategory", ""));
            category.setCategory(jCategory.getString("strCategory", ""));
            category.setThumbnailLink(jCategory.getString("strCategoryThumb", ""));
            category.setDescription(jCategory.getString("strCategoryDescription", ""));

            categories.add(category);
        }

        return categories;
    }
}
