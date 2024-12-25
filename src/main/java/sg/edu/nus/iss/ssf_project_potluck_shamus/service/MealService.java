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
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.MealModel;

@Service
public class MealService {

    @Value("${mealdb.api.key}")
    private String apiKey;

    public List<MealModel> fetchMeals(String url)
    {
        List<MealModel> meals = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JsonReader jReader = Json.createReader(new StringReader(response));
        JsonObject jResponse = jReader.readObject();

        JsonArray mealsArray = jResponse.getJsonArray("meals");

        for (int i = 0; i < mealsArray.size(); i++)
        {
            MealModel meal = new MealModel();
            JsonObject jMeal = mealsArray.getJsonObject(i);

            meal.setId(jMeal.getString("idMeal", ""));
            meal.setName(jMeal.getString("strMeal", ""));
            meal.setCategory(jMeal.getString("strCategory", ""));
            meal.setArea(jMeal.getString("strArea", ""));
            meal.setInstructions(jMeal.getString("strInstructions", ""));
            meal.setThumbnailLink(jMeal.getString("strMealThumb", ""));
            meal.setTags(jMeal.getString("strTags", ""));
            meal.setYoutubeLink(jMeal.getString("strYoutube", ""));
            meal.setSource(jMeal.getString("strSource", ""));


            List<String> ingredients = new ArrayList<>();
            List<String> measurements = new ArrayList<>();

            for (int j = 1; j <= 20; j++)
            {
                String ingredient = jMeal.getString("strIngredient" + j, "");
                String measurement = jMeal.getString("strMeasure" + j, "");

                if (!ingredient.isEmpty() && !ingredient.isBlank())
                {
                    ingredients.add(ingredient);
                }

                if (!measurement.isEmpty() && !measurement.isBlank())
                {
                    measurements.add(measurement);
                }
            }

            meal.setIngredients(ingredients);
            meal.setMeasurements(measurements);

            meals.add(meal);
        }

        return meals;

    }
    


    public List<MealModel> getByName(String mealName)
    {
        System.out.println("Getting by name...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
        System.out.println("Name is >>> " + mealName);

        String url = String.format("%s/%s/search.php?s=%s", Url.mealApiBaseUrl, apiKey, mealName);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }

    public List<MealModel> getByFirstLetter(String firstLetter)
    {
        System.out.println("Getting by ID...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
        System.out.println("Name is >>> " + firstLetter);

        String url = String.format("%s/%s/search.php?f=%s", Url.mealApiBaseUrl, apiKey, firstLetter);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }

    public List<MealModel> getById(String mealId)
    {
        System.out.println("Getting by ID...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
        System.out.println("Name is >>> " + mealId);

        String url = String.format("%s/%s/lookup.php?i=%s", Url.mealApiBaseUrl, apiKey, mealId);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }

    public List<MealModel> getRandomMeal()
    {
        System.out.println("Getting 1 random meal...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);

        String url = String.format("%s/%s/random.php", Url.mealApiBaseUrl, apiKey);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }



    public List<MealModel> filterByCategory(String category)
    {
        System.out.println("Getting by category...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
        System.out.println("Category is >>> " + category);
                
        String url = String.format("%s/%s/filter.php?c=%s", Url.mealApiBaseUrl, apiKey, category);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }

    public List<MealModel> filterByIngredient(String ingredient)
    {
        System.out.println("Getting by category...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
        System.out.println("Ingredient is >>> " + ingredient);
                
        String url = String.format("%s/%s/filter.php?i=%s", Url.mealApiBaseUrl, apiKey, ingredient);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }

    public List<MealModel> filterByArea(String area)
    {
        System.out.println("Getting by category...");
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
        System.out.println("Area is >>> " + area);
                
        String url = String.format("%s/%s/filter.php?a=%s", Url.mealApiBaseUrl, apiKey, area);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }

}
