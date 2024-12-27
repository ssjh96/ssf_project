package sg.edu.nus.iss.ssf_project_potluck_shamus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.ssf_project_potluck_shamus.model.CategoryModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.model.MealModel;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.CategoryService;
import sg.edu.nus.iss.ssf_project_potluck_shamus.service.MealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ApiRestController 
{
    @Autowired 
    private MealService mealService;

    @Autowired
    private CategoryService categoryService;

    // TEST API CALL
    @GetMapping("/test_ac") // http://localhost:3000/api/test_ac
    public List<CategoryModel> getAllCategories() 
    {
        return categoryService.fetchCategories();
    }

    @GetMapping("/test_c") // http://localhost:3000/api/test_c?c=seafood
    public List<MealModel> testByCategory(@RequestParam ("c") String category) {
        return mealService.filterByCategory(category);
    }
    
    @GetMapping("/test_i") // http://localhost:3000/api/test_i?i=chicken
    public List<MealModel> testByIngredient(@RequestParam ("i") String ingredient) {
        return mealService.filterByIngredient(ingredient);
    }

    @GetMapping("/test_a") // http://localhost:3000/api/test_a?a=canadian
    public List<MealModel> testByArea(@RequestParam ("a") String area) {
        return mealService.filterByArea(area);
    }

    @GetMapping("/test_f") // http://localhost:3000/api/test_f?f=a
    public List<MealModel> testByLetter(@RequestParam ("f") String letter) {
        return mealService.filterByFirstLetter(letter);
    }
    
}
