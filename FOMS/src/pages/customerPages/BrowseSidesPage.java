package pages.customerPages;

import utilities.Session;
import constants.MealCategory;

/**
 * This page is responsible for displaying and handling inputs for the sides category, and inherits from BrowseCategoriesPage
 * @author Siah Yee Long
 * @author Lee Jedidiah
 */
public class BrowseSidesPage extends BrowseCategoriesPage{
    /**
     * The constructor for the BrowseSidesPage. It sets the category to MealCategory.SIDE
     * @param s the current session
     */
    public BrowseSidesPage(Session s){
        super(s);
        this.category = MealCategory.SIDE;
    }
}