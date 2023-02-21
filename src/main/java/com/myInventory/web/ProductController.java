package com.myInventory.web;


import com.myInventory.dao.ProductRepository;
import com.myInventory.model.Product;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
/* The directory used by the controller will be /products and any mapping values hereinafter
 * will be assumed to be under the /products directory
 */
@RequestMapping("/products")
public class ProductController {

    // declaration of 'log' for debugging purpose
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    // Declare and connect the ProductDao class to use here
    @Autowired
    private ProductRepository productRepository;

    // Method that takes us to the directory of the products list
    @RequestMapping(value = "", "/list")
    public String list(Model model) {
        model.addAttribute("list", productRepository.findAll());
        return "/products/list";
    }

    // Method to the form of the new product
    @RequestMapping("/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "/products/newProduct";
    }

    // Creates new product, if any errors goes back to the form
    @RequestMapping("/create")
    public String createProduct(@Valid Product products, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "/products/newProduct";
        }
        products = productRepository.save(products);
        redirectAttributes.addFlashAttribute("message", "The Product " + products.getModel() + " has been created");
        return "redirect:/products/see" + products.getId() + "/";
    }

    // Shows the values we input for the product attributes
    @RequestMapping("/see/{id}")
    public String see(@PathVariable Long id, Model model) {
        Product product = productRepository.findOne(id);
        model.addAttribute("product", product);
        return "/products/see";
    }

    // Deletes a product
    @RequestMapping("/delete/{id}/")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findOne(id);
        productRepository.delete(product);
        redirectAttributes.addFlashAttribute("message", "The product " + id + " has been deleted");
        return "redirect:/products/";
    }
}
