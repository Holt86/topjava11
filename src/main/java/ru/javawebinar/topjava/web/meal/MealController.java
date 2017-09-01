package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Controller
@RequestMapping(value = "/meals")
public class MealController extends MealRestController {

    @Autowired
    public MealController(MealService service) {
        super(service);
    }

    @GetMapping()
    public String getAll(ModelMap map) {
        map.put("meals", getAll());
        return "meals";
    }

    @GetMapping(value = "/remove")
    public String remove(@RequestParam("id") int id){
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping(value = "/create")
    public String create(@RequestParam("id") int id, ModelMap map){
        if (id == 0){
            map.put("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "description", 1000));
        }else {
            map.put("meal", get(id));
        }
        return "mealForm";
    }

    @PostMapping(value = "/save")
    public String save(@RequestParam("id") Integer id,
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("dateTime") LocalDateTime dateTime,
                       @RequestParam("description") String description, @RequestParam("calories") int calories){

       Meal meal = new Meal(id, dateTime, description, calories);
       if (meal.isNew()){
           create(meal);
       }else {
           update(meal, meal.getId());
       }
       return "redirect:/meals";
    }

    @PostMapping(value = "/filter")
    public String filter(ModelMap map, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("startDate")LocalDate startDate,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("endDate") LocalDate endDate,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @RequestParam("startTime")LocalTime startTime,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @RequestParam("endTime") LocalTime endTime){
        map.put("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
