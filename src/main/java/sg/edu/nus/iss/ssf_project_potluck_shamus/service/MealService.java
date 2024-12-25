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

            meal.setIdMeal(jMeal.getString("idMeal", ""));
            meal.setStrMeal(jMeal.getString("strMeal", ""));
            meal.setStrCategory(jMeal.getString("strCategory", ""));
            meal.setStrArea(jMeal.getString("strArea", ""));
            meal.setStrInstructions(jMeal.getString("strInstructions", ""));
            meal.setStrMealThumb(jMeal.getString("strMealThumb", ""));
            meal.setStrTags(jMeal.getString("strTags", ""));
            meal.setStrYoutube(jMeal.getString("strYoutube", ""));
            meal.setStrSource(jMeal.getString("strSource", ""));


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



    public List<MealModel> getMealsByCategory(String category)
    {
        System.out.println("Base is >>> " + Url.mealApiBaseUrl);
        System.out.println("API Key is >>> " + apiKey);
        System.out.println("Category is >>> " + category);
                
        String url = String.format("%s/%s/filter.php?c=%s", Url.mealApiBaseUrl, apiKey, category);
        System.out.println(url);

        List<MealModel> meals = fetchMeals(url);

        return meals;
    }
    
}
