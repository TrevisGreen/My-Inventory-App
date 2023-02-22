package com.myInventory.web;


import com.myInventory.dao.ClientRepository;
import com.myInventory.dao.InvoiceRepository;
import com.myInventory.dao.ItemRepository;
import com.myInventory.dao.ProductRepository;
import com.myInventory.model.Invoice;
import com.myInventory.model.Item;
import com.myInventory.model.Product;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

// Defines class as a controller in the MVC architecture
@Controller
/* The directory used by the controller will be /invoice and any mapping values hereinafter
 * will be assumed to be under the /invoice directory
 */
@RequestMapping("/invoice")
public class InvoiceController {

    // declaration of 'log' for debugging purposes
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    // Declaring and connecting to the Dao classes to use in this controller
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemRepository itemRepository;

    // This method return the list of invoices
    @RequestMapping(value = {"", "/list"})
    public String list(Model model) {
        model.addAttribute("list", invoiceRepository.findAll());
        return "/invoice/list";
    }

    // Method that takes us to the form to create a new invoice
    @RequestMapping("/newInvoice")
    public String newProduct(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("invoice",new Invoice());
        return "/invoice/newInvoice";
    }

    // If any errors go back to the form, if good, show the created invoice
    @RequestMapping("/create")
    public String createInvoice(@Valid Invoice invoice, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            log.error("Couldn't create invoice {}", bindingResult.getAllErrors());
            return "/invoice/newInvoice";
        }
        invoice.setClient(clientRepository.getOne(invoice.getClient().getId()));
        invoice.setDateCreated(new Date());
        invoice = invoiceRepository.save(invoice);
        redirectAttributes.addFlashAttribute("message", "The Invoice " + invoice.getId() + " has been created");
        return "redirect:/invoice/see/" + invoice.getId() + "/";
    }

    // Method to edit the invoice
    @RequestMapping("/editing/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        Invoice invoice = invoiceRepository.findOne(id);
        model.addAttribute("invoice", invoice);
        return "invoice/edit";
    }

    // Method to remove the invoice, returns us to the list of invoice
    @RequestMapping("/delete/{id}/")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Item item = itemRepository.findOne(id);
        itemRepository.delete(item);
        redirectAttributes.addFlashAttribute("message", "The item " + id + " has been deleted");
        return "redirect:/invoice/see/"+item.getInvoice().getId();
    }

    // Method to add an item to the specify invoice
    @RequestMapping("/addItem/{invoiceId}")
    public String addItem(@PathVariable Long invoiceId, Model model) {
        Invoice invoice = invoiceRepository.findOne(invoiceId);
        Item item = new Item();
        item.setInvoice(invoice);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("item", item);
        return "invoice/addItem";
    }

    // Method to return the values fo the products of the search as you type functionality
    @RequestMapping(value = "/products", params = {"term"}, produces = "application/json")
    @ResponseBody
    public List<Product> products(@RequestParam String term) {
        log.debug("Looking for products with {}", term);
        Pageable pageable = new PageRequest(0,10);
        List<Product> products = productRepository.findByManufacturerIgnoreCaseContainingOrModelIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(term,term,term,pageable);
        return products;
    }

    // Method that adds the new product to the invoice, if any errors it goes back to the form
    @RequestMapping("/addItem")
    public String saveItem(@Valid Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            log.error("Couldn't create invoice {}",bindingResult.getAllErrors());
            return "/invoice/newInvoice";
        }
        item.setInvoice(invoiceRepository.getOne(item.getInvoice().getId()));
        item.setProduct(productRepository.getOne(item.getProduct().getId()));
        item = itemRepository.save(item);
        redirectAttributes.addFlashAttribute("message", "The Item " + item.getId() + " has been created");
        return "redirect:/invoice/see/" + item.getInvoice().getId() + "/";
    }
}
