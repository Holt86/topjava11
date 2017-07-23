package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by user on 21.07.2017.
 */
public class MealServlet extends HttpServlet {

    private MealRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        switch (action) {
            case "delete": {
                logger.info("Delete meal with {}", req.getParameter("id"));
                repository.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/meals");
                break;
            }
            case "update": {
                Meal meal = repository.get(Integer.parseInt(req.getParameter("id")));
                req.setAttribute("meal", meal);
                logger.info("forward on update");
                req.getRequestDispatcher("/meal.jsp").forward(req, resp);
                break;
            }
            case "create": {
                req.setAttribute("meal", new Meal(LocalDateTime.now().withSecond(0).withNano(0), "description", 2000));
                logger.info("forward on create");
                req.getRequestDispatcher("/meal.jsp").forward(req, resp);
                break;
            }
            default: {
                req.setAttribute("meals", repository.getAll());
                logger.info("forward on getAll");
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            }

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        int id = Integer.valueOf(req.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.valueOf(req.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);
        meal.setId(id);
        Meal saveMeal = repository.save(meal);
        logger.info("save meal {} and redirect on meals", saveMeal.getId());
        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
