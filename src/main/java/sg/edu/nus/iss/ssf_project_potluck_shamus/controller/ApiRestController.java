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
import org.springframework.web.bind.annotation.PathVariable;


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

    @GetMapping("/test_c/{c}") // http://localhost:3000/api/test_c?c=seafood
    public List<MealModel> testByCategory(@PathVariable ("c") String category) {
        return mealService.filterByCategory(category);
    }

    @GetMapping("/test_f/{f}") // http://localhost:3000/api/test_f?f=a
    public List<MealModel> testByLetter(@PathVariable ("f") String letter) {
        return mealService.filterByFirstLetter(letter);
    }



    // NOT IN USE
    // @GetMapping("/test_i/{i}") // http://localhost:3000/api/test_i?i=chicken
    // public List<MealModel> testByIngredient(@PathVariable ("i") String ingredient) {
    //     return mealService.filterByIngredient(ingredient);
    // }

    // @GetMapping("/test_a/{a}") // http://localhost:3000/api/test_a?a=canadian
    // public List<MealModel> testByArea(@PathVariable ("a") String area) {
    //     return mealService.filterByArea(area);
    // }
    
}
