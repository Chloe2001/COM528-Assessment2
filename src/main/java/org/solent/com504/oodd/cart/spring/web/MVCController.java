package org.solent.com504.oodd.cart.spring.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.dto.User;
import org.solent.com504.oodd.cart.model.dto.UserRole;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.service.ShoppingService;
import org.solent.com504.oodd.cart.web.WebObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MVCController {

    final static Logger LOG = LogManager.getLogger(MVCController.class);

    // this could be done with an autowired bean
    //private ShoppingService shoppingService = WebObjectFactory.getShoppingService();
    @Autowired
    ShoppingService shoppingService = null;

    // note that scope is session in configuration
    // so the shopping cart is unique for each web session
    @Autowired
    ShoppingCart shoppingCart = null;

    private User getSessionUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            sessionUser = new User();
            sessionUser.setUsername("anonymous");
            sessionUser.setUserRole(UserRole.ANONYMOUS);
            session.setAttribute("sessionUser",sessionUser);
        }
        return sessionUser;
    }

    // this redirects calls to the root of our application to index.html
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewCart(@RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "itemName", required = false) String itemName,
            @RequestParam(name = "itemUUID", required = false) String itemUuid,
            Model model,
            HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        // used to set tab selected
        model.addAttribute("selectedPage", "home");

        String message = "";
        String errorMessage = "";

        // note that the shopping cart is is stored in the sessionUser's session
        // so there is one cart per sessionUser
       // ShoppingCart shoppingcart = (ShoppingCart) session.getAttribute("shoppingCart");
      // if (shoppingcart == null) synchronized (this) {
           // if (shoppingcart == null) {
               //shoppingcart = WebObjectFactory.getNewShoppingCart();
                //session.setAttribute("shoppingCart", shoppingcart);
      //    }
      // }
      if (action == null) {
            // do nothing but show page
        } else if ("addItemToCart".equals(action)) {
            ShoppingItem shoppingItem = shoppingService.getNewItemByName(itemName);
            if (shoppingItem == null) {
                message = "cannot add unknown " + itemName + " to cart";
            } else {
                message = "adding " + itemName + " to cart  " + shoppingItem.getPrice();
                shoppingCart.addItemToCart(shoppingItem);
            }
        } else if ("removeItemFromCart".equals(action)) {
            message = "removed " + itemName + " from cart";
            shoppingCart.removeItemFromCart(itemUuid);
        } else {
            message = "unknown action=" + action;
        }

        List<ShoppingItem> availableItems = shoppingService.getAvailableItems();

        List<ShoppingItem> shoppingCartItems = shoppingCart.getShoppingCartItems();

        Double shoppingcartTotal = shoppingCart.getTotal();

        // populate model with values
        model.addAttribute("availableItems", availableItems);
        model.addAttribute("shoppingCartItems", shoppingCartItems);
        model.addAttribute("shoppingcartTotal", shoppingcartTotal);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);

        return "home";
    }

    @RequestMapping(value = "/about", method = {RequestMethod.GET, RequestMethod.POST})
    public String aboutCart(Model model, HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        
        // used to set tab selected
        model.addAttribute("selectedPage", "about");
        return "about";
    }

    @RequestMapping(value = "/contact", method = {RequestMethod.GET, RequestMethod.POST})
    public String contactCart(Model model, HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        
        // used to set tab selected
        model.addAttribute("selectedPage", "contact");
        return "contact";
    }
    @Autowired
      ShoppingItemCatalogRepository shoppingItemCatalogRepository;
    
     @RequestMapping(value = "/catalog", method = {RequestMethod.GET, RequestMethod.POST})
    public String catalogList(Model model, HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        
        if(!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())){
            model.addAttribute("errormessage","You need administrator access for this page");
            return "catalog";
        }
        
        List<ShoppingItem> availableItems = shoppingItemCatalogRepository.findAll();
        
        model.addAttribute("availableItems", availableItems);
        
        
        
        // used to set tab selected
        model.addAttribute("selectedPage", "admin");
        return "catalog";
    }

    @RequestMapping(value = {"/ModifyItem"}, method = RequestMethod.GET)
    public String modifyItem(
            @RequestParam(value = "itemName", required = true) String itemName,
            @RequestParam(value = "action", required = false) String action,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        // set values
        model.addAttribute("selectedPage", "admin");
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            model.addAttribute("errorMessage", "You need administrator access for this page");
            return "ModifyItem";
        }

        List<ShoppingItem> shoppingItem = shoppingItemCatalogRepository.findByName(itemName);
        if (shoppingItem.isEmpty()) {
            errorMessage = "No items to find!";
            model.addAttribute("errorMessage", errorMessage);
            return "ModifyItem";
        }

        ShoppingItem modifyItem = shoppingItem.get(0);
        System.out.println("TESTING: " + modifyItem);
        System.out.println("TESTING: " + itemName);
        System.out.println("TESTING: " + shoppingItem);
        model.addAttribute("modifyItem", modifyItem);

        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        if ("view".equals(action)) return "viewItem";
        if ("edit".equals(action)) return "viewModifyItem";
        if ("delete".equals(action)) {
            List<ShoppingItem> item = shoppingItemCatalogRepository.findByName(itemName);
            shoppingItemCatalogRepository.delete(item.get(0));
            List<ShoppingItem> availableItems = shoppingItemCatalogRepository.findAll();
            model.addAttribute("availableItems", availableItems);
            return "catalog";
        }
        return "ModifyItem";
    }
    
     @RequestMapping(value = {"/ModifyItem"}, method = RequestMethod.POST)
    public String updateItem(
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";


        // set values
        model.addAttribute("selectedPage", "admin");
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            model.addAttribute("errorMessage", "You need administrator access for this page");
            return "ModifyItem";
        }


        Optional<ShoppingItem> shoppingItem = null;
        if (id != null) {
            shoppingItem = shoppingItemCatalogRepository.findById(id);
        }

        ShoppingItem modifyItem;
        if (shoppingItem == null || shoppingItem.isEmpty()) {
            modifyItem = new ShoppingItem();
        } else {
            modifyItem = shoppingItem.get();
        }


        if (quantity != null) modifyItem.setQuantity(quantity);
        if (name != null) modifyItem.setName(name);
        if (price != null) modifyItem.setPrice(price);
        shoppingItemCatalogRepository.save(modifyItem);
        message = "Updated item " + modifyItem.getName();
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("modifyItem", modifyItem);
        return "viewItem";
    }

    @RequestMapping(value = {"/newItem"}, method = RequestMethod.GET)
    public String viewCreateItem(
            @RequestParam(value = "action", required = false) String action,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        // set values
        model.addAttribute("selectedPage", "admin");
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            model.addAttribute("errorMessage", "You need administrator access for this page");
            return "newItem";
        }

        return "newItem";
    }
    
    @RequestMapping(value = {"newItem"}, method = RequestMethod.POST)
    public String PostCreateItem(
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        // set values
        model.addAttribute("selectedPage", "admin");
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            model.addAttribute("errorMessage", "You need administrator access for this page");
            return "newItem";
        }
        if (name == null || price == null || quantity == null) {
            model.addAttribute("errorMessage", "Please fill in all the fields");
            return "newItem";
        }
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setName(name);
        shoppingItem.setQuantity(quantity);
        shoppingItem.setPrice(price);
        shoppingItemCatalogRepository.save(shoppingItem);
        message = "Added item " + shoppingItem.getName();
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("message", message);
        return "newItem";
    }
    
    
    
    
    
    
    
    
    
    
    

    /*
     * Default exception handler, catches all exceptions, redirects to friendly
     * error page. Does not catch request mapping errors
     */
    @ExceptionHandler(Exception.class)
    public String myExceptionHandler(final Exception e, Model model, HttpServletRequest request) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        final String strStackTrace = sw.toString(); // stack trace as a string
        String urlStr = "not defined";
        if (request != null) {
            StringBuffer url = request.getRequestURL();
            urlStr = url.toString();
        }
        model.addAttribute("requestUrl", urlStr);
        model.addAttribute("strStackTrace", strStackTrace);
        model.addAttribute("exception", e);
        //logger.error(strStackTrace); // send to logger first
        return "error"; // default friendly exception message for sessionUser
    }

}
