package com.mymart.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mymart.model.Category;
import com.mymart.model.Deal;
import com.mymart.model.Discount;
import com.mymart.model.DropdownItem;
import com.mymart.model.NavLink;
import com.mymart.model.PriceRange;
import com.mymart.model.Product;
import com.mymart.model.ProductDto;
import com.mymart.repository.CategoryRepository;
import com.mymart.repository.ProductsRepository;
import com.mymart.service.CategoryService;
import com.mymart.service.DealService;
import com.mymart.service.DiscountService;
import com.mymart.service.NavBarService;
import com.mymart.service.PriceRangeService;
import com.mymart.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/Admin")
public class AdminProductsController {
	
	@Autowired
	private ProductsRepository repo;
	
	@Autowired
	private CategoryRepository crepo;
	
	@Autowired
	private NavBarService service;
	
	@Autowired
	private DealService dealservice;
	
	@Autowired
	private PriceRangeService priceRangeService;
	
	
	 @Autowired
	 private DiscountService discountService;
	
	
	private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public AdminProductsController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("{categoryName}")
    public String displayProductsByCategory(@PathVariable String categoryName, Model model) {
    	
    	Map<NavLink, List<DropdownItem>> navbarWithDropdownData = service.getNavbarWithDropdownData();
    	model.addAttribute("navbarWithDropdownData", navbarWithDropdownData);
    	
        Category category = categoryService.getCategoryByName(categoryName);

        if (category == null) {
            return "error";
        }

        List<Product> products = productService.getProductsByCategory(category);

        model.addAttribute("category", category);
        model.addAttribute("products", products);

        return "products/AdminProduct"; 
    }


    

	@GetMapping("/addCategory")
	public String showAddForm(Model model) {
		model.addAttribute("cat", new Category());
		
		
		 List<Category> categories = categoryService.getAllCategories();
	        model.addAttribute("categories", categories);
		return "products/addCategory";
	}
	@PostMapping("/addCategory")
   public String addcategory(@ModelAttribute("cat")Category cat) {
  
   	crepo.save(cat);
   	
   	return "redirect:/Admin/Product";
   }
    
    
    
   
	@GetMapping("/Product")
	public String showProductList(Model model) {
		Map<NavLink, List<DropdownItem>> navbarWithDropdownData = service.getNavbarWithDropdownData();
		model.addAttribute("navbarWithDropdownData", navbarWithDropdownData);
		List<Product> products = repo.findAll();
		model.addAttribute("products", products);
		
		 List<Deal> deals = dealservice.getAllDeals();
	        model.addAttribute("deals", deals);
		return "products/AdminProduct";
	}

	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto",productDto);
		
		 List<Category> categories = categoryService.getAllCategories();
	        model.addAttribute("categories", categories);
		return "products/CreateProduct";
	}
	
	@PostMapping("/create")
	public String createProduct(
			@Valid @ModelAttribute ProductDto productDto,
			BindingResult result
			) 
	
	{
		
		
//first image

		if(productDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("productDto","imageFile","The image file is required"));
		}
		
		if(result.hasErrors()) {
			return "products/CreateProduct";
		}
		
		MultipartFile image = productDto.getImageFile();
			Date createdAt = new Date();
			String storageFileName = createdAt.getTime()+"_"+image.getOriginalFilename();
			
			try {
				String uploadDir = "public/images/";
				Path uploadPath = Paths.get(uploadDir);
				
				if(!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
			
			try(InputStream inputStream = image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}catch(Exception ex) {
			System.out.println("Exception : " + ex.getMessage());
		}
			
			
	
//	2nd image
			
			
			
			if(productDto.getImageFile1().isEmpty()) {
				result.addError(new FieldError("productDto","imageFile1","The image file is required"));
			}
			
			if(result.hasErrors()) {
				return "products/CreateProduct";
			}
			
			MultipartFile image1 = productDto.getImageFile1();
				String storageFileName1 = image1.getOriginalFilename();
				
				try {
					String uploadDir = "public/images/";
					Path uploadPath = Paths.get(uploadDir);
					
					if(!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}
				
				try(InputStream inputStream = image1.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName1),
							StandardCopyOption.REPLACE_EXISTING);
				}
			}catch(Exception ex) {
				System.out.println("Exception : " + ex.getMessage());
			}
				
				
//	3rd image
				
				
				
				if(productDto.getImageFile2().isEmpty()) {
					result.addError(new FieldError("productDto","imageFile2","The image file is required"));
				}
				
				if(result.hasErrors()) {
					return "products/CreateProduct";
				}
				
				MultipartFile image2 = productDto.getImageFile2();
					String storageFileName2 = image2.getOriginalFilename();
					
					try {
						String uploadDir = "public/images/";
						Path uploadPath = Paths.get(uploadDir);
						
						if(!Files.exists(uploadPath)) {
							Files.createDirectories(uploadPath);
						}
					
					try(InputStream inputStream = image2.getInputStream()){
						Files.copy(inputStream, Paths.get(uploadDir + storageFileName2),
								StandardCopyOption.REPLACE_EXISTING);
					}
				}catch(Exception ex) {
					System.out.println("Exception : " + ex.getMessage());
				}		
				
				
							
//	4th image
					
					
					
					if(productDto.getImageFile3().isEmpty()) {
						result.addError(new FieldError("productDto","imageFile3","The image file is required"));
					}
					
					if(result.hasErrors()) {
						return "products/CreateProduct";
					}
					
					MultipartFile image3 = productDto.getImageFile3();
						String storageFileName3 = image3.getOriginalFilename();
						
						try {
							String uploadDir = "public/images/";
							Path uploadPath = Paths.get(uploadDir);
							
							if(!Files.exists(uploadPath)) {
								Files.createDirectories(uploadPath);
							}
						
						try(InputStream inputStream = image3.getInputStream()){
							Files.copy(inputStream, Paths.get(uploadDir + storageFileName3),
									StandardCopyOption.REPLACE_EXISTING);
						}
					}catch(Exception ex) {
						System.out.println("Exception : " + ex.getMessage());
					}					
			
	

						
//	5th image
						
						
						
						if(productDto.getImageFile4().isEmpty()) {
							result.addError(new FieldError("productDto","imageFile4","The image file is required"));
						}
						
						if(result.hasErrors()) {
							return "products/CreateProduct";
						}
						
						MultipartFile image4 = productDto.getImageFile4();
							String storageFileName4 = image4.getOriginalFilename();
							
							try {
								String uploadDir = "public/images/";
								Path uploadPath = Paths.get(uploadDir);
								
								if(!Files.exists(uploadPath)) {
									Files.createDirectories(uploadPath);
								}
							
							try(InputStream inputStream = image4.getInputStream()){
								Files.copy(inputStream, Paths.get(uploadDir + storageFileName4),
										StandardCopyOption.REPLACE_EXISTING);
							}
						}catch(Exception ex) {
							System.out.println("Exception : " + ex.getMessage());
						}					
				
				

			
			Product product = new Product();
			product.setName(productDto.getName());
			product.setBrand(productDto.getBrand());
			
			
			product.setCategory(productDto.getCategory());
			product.setPrice(productDto.getPrice());
			product.setDescription(productDto.getDescription());
			product.setCreatedAt(createdAt);
			product.setImageFileName(storageFileName);
			product.setImageFileName1(storageFileName1);
			product.setImageFileName2(storageFileName2);
			product.setImageFileName3(storageFileName3);
			product.setImageFileName4(storageFileName4);


			product.setRatingCount(0); 
			
			product.calculateDiscountedPrice();
			repo.save(product);
		
			return "redirect:/Admin/Product";
	}


	

	
	@GetMapping("/edit")
	public String showEditPage(
			Model model,
			@RequestParam int id
			) {
		try {
			model.addAttribute("categories", categoryService.getAllCategories());

			Product product = repo.findById(id).get();
			model.addAttribute("product",product);
			
			ProductDto productDto = new ProductDto();
			productDto.setName(product.getName());
			productDto.setBrand(product.getBrand());
		    productDto.setCategory(product.getCategory());		
			productDto.setPrice(product.getPrice());
			productDto.setDescription(product.getDescription());

			model.addAttribute("productDto",productDto);
			

		}
		catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/Product";
		}
		return "products/EditProduct";
	}

	@PostMapping("/edit")
	public String updateProduct(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute ProductDto productDto,
			BindingResult result, RedirectAttributes redirectAttributes
			) {
		
		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product",product);
			
			if(result.hasErrors()) {
				return "products/EditProduct";
			}
						
			if(!productDto.getImageFile().isEmpty()) {
				//for deleting the old image 1
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
				try {
					Files.delete(oldImagePath);
				}
				catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				//save the new image 1
				MultipartFile image = productDto.getImageFile();
				Date createdAt = new Date();
				String storageFileName = createdAt.getTime()+"_"+image.getOriginalFilename();
				
				try(InputStream inputStream = image.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
			}
				product.setImageFileName(storageFileName);
		}
					
			if(!productDto.getImageFile1().isEmpty()) {
				//for deleting the old image 2
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName1());
				try {
					Files.delete(oldImagePath);
				}
				catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				//save the new image 2
				MultipartFile image1 = productDto.getImageFile1();
				String storageFileName1 = image1.getOriginalFilename();
				
				try(InputStream inputStream1 = image1.getInputStream()){
					Files.copy(inputStream1, Paths.get(uploadDir + storageFileName1),
							StandardCopyOption.REPLACE_EXISTING);
			}
				product.setImageFileName1(storageFileName1);
		}
						
			if(!productDto.getImageFile2().isEmpty()) {
				//for deleting the old image 3
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName2());
				try {
					Files.delete(oldImagePath);
				}
				catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				//save the new image 3
				MultipartFile image2 = productDto.getImageFile2();
				String storageFileName2 = image2.getOriginalFilename();
				
				try(InputStream inputStream2 = image2.getInputStream()){
					Files.copy(inputStream2, Paths.get(uploadDir + storageFileName2),
							StandardCopyOption.REPLACE_EXISTING);
			}
				product.setImageFileName2(storageFileName2);
		}
						
			if(!productDto.getImageFile3().isEmpty()) {
				//for deleting the old image 4
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName3());
				try {
					Files.delete(oldImagePath);
				}
				catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				//save the new image 4
				MultipartFile image3 = productDto.getImageFile3();
				String storageFileName3 = image3.getOriginalFilename();
				
				try(InputStream inputStream3 = image3.getInputStream()){
					Files.copy(inputStream3, Paths.get(uploadDir + storageFileName3),
							StandardCopyOption.REPLACE_EXISTING);
			}
				product.setImageFileName3(storageFileName3);
		}
						
			if(!productDto.getImageFile4().isEmpty()) {
				//for deleting the old image 5
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName4());
				try {
					Files.delete(oldImagePath);
				}
				catch(Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				//save the new image 5
				MultipartFile image4 = productDto.getImageFile4();
				String storageFileName4 = image4.getOriginalFilename();
				
				try(InputStream inputStream4 = image4.getInputStream()){
					Files.copy(inputStream4, Paths.get(uploadDir + storageFileName4),
							StandardCopyOption.REPLACE_EXISTING);
			}
				product.setImageFileName4(storageFileName4);
		}
			
			product.setName(productDto.getName());
			product.setBrand(productDto.getBrand());
			product.setCategory(productDto.getCategory());	
			product.setPrice(productDto.getPrice());
			product.setDescription(productDto.getDescription());

			repo.save(product);
		}
		catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		redirectAttributes.addAttribute("categoryName", productDto.getCategory().getName());
		return "redirect:/Admin/Product";

	}


	@GetMapping("/delete")
	public String deleteProduct(
		@RequestParam int id
		) {
		try {
			Product product = repo.findById(id).get();
			
			//for deleting the product image 1
			Path imagePath = Paths.get("public/images/" + product.getImageFileName());
			try {
				Files.delete(imagePath);
			}
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			
			//for deleting the product image 2
			Path imagePath1 = Paths.get("public/images/" + product.getImageFileName1());
			try {
				Files.delete(imagePath1);
			}
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			
			//for deleting the product image 3
			Path imagePath2 = Paths.get("public/images/" + product.getImageFileName2());
			try {
				Files.delete(imagePath2);
			}
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			
			//for deleting the product image 4
			Path imagePath3 = Paths.get("public/images/" + product.getImageFileName3());
			try {
				Files.delete(imagePath3);
			}
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			//for deleting the product image 5
			Path imagePath4 = Paths.get("public/images/" + product.getImageFileName4());
			try {
				Files.delete(imagePath4);
			}
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			
			repo.delete(product);
		}
		catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		return "redirect:/Admin/Product";
	}
	
	
	 @GetMapping("/All Products")
		public String showProductListAdmin(Model model) {	
		 Map<NavLink, List<DropdownItem>> navbarWithDropdownData = service.getNavbarWithDropdownData();
		 model.addAttribute("navbarWithDropdownData", navbarWithDropdownData);
			List<Product> products = repo.findAll();
			model.addAttribute("products", products);
			
			List<Deal> deals = dealservice.getAllDeals();
	        model.addAttribute("deals", deals);
			
			return "products/AdminProduct";
		}
	 
	 
	 @GetMapping("/managePriceRanges")
	    public String showPriceRangeManagementPage(Model model) {
	        List<PriceRange> priceRanges = priceRangeService.getAllPriceRanges();
	        model.addAttribute("priceRanges", priceRanges);
	        model.addAttribute("newPriceRange", new PriceRange()); // Form object for adding new price range
	        return "admin/ManagePriceRanges";
	    }

	    @PostMapping("/addPriceRange")
	    public String addPriceRange(@ModelAttribute("newPriceRange") PriceRange priceRange) {
	        priceRangeService.savePriceRange(priceRange);
	        return "redirect:/Admin/managePriceRanges";
	    }

  
	    
	    @GetMapping("/editPriceRange/{id}")
	    public String getEditPriceRangeForm(@PathVariable Long id, Model model) {
	        PriceRange priceRange = priceRangeService.getPriceRangeById(id);
	        model.addAttribute("priceRange", priceRange);
	        return "admin/editPriceRange"; // Ensure this view name matches your Thymeleaf template name
	    }

	    @PostMapping("/editPriceRange")
	    public String editPriceRange(@ModelAttribute PriceRange priceRange) {
	        priceRangeService.savePriceRange(priceRange); // Ensure this method exists in your service
	        return "redirect:/Admin/managePriceRanges"; // Ensure this view name matches your Thymeleaf template name
	    }
	    
	    
	    

	    @GetMapping("/deletePriceRange/{id}")
	    public String deletePriceRange(@PathVariable int id) {
	        priceRangeService.deletePriceRange(id);
	        return "redirect:/Admin/managePriceRanges";
	    }
	 
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	   

 
	 
	    
	    
	    @GetMapping("/manageDiscounts")
	    public String manageDiscounts(Model model) {
	        List<Discount> discounts = discountService.getAllDiscounts();
	        model.addAttribute("discounts", discounts);
	        model.addAttribute("discount", new Discount());
	        return "admin/manageDiscounts";
	    }

	    @PostMapping("/addDiscount")
	    public String addDiscount(@ModelAttribute Discount discount, RedirectAttributes redirectAttributes) {
	        discountService.saveDiscount(discount);
	        redirectAttributes.addFlashAttribute("message", "Discount added successfully!");
	        return "redirect:/Admin/manageDiscounts";
	    }

	    @GetMapping("/editDiscount/{id}")
	    public String editDiscount(@PathVariable Long id, Model model) {
	        Discount discount = discountService.getDiscountById(id);
	        if (discount == null) {
	            return "redirect:/Admin/manageDiscounts";
	        }
	        model.addAttribute("discount", discount);
	        return "admin/editDiscount";
	    }

	    @PostMapping("/updateDiscount")
	    public String updateDiscount(@ModelAttribute Discount discount, RedirectAttributes redirectAttributes) {
	        discountService.updateDiscount(discount);
	        redirectAttributes.addFlashAttribute("message", "Discount updated successfully!");
	        return "redirect:/Admin/manageDiscounts";
	    }

	    @PostMapping("/deleteDiscount")
	    public String deleteDiscount(@RequestParam Long id, RedirectAttributes redirectAttributes) {
	        discountService.deleteDiscount(id);
	        redirectAttributes.addFlashAttribute("message", "Discount deleted successfully!");
	        return "redirect:/Admin/manageDiscounts";
	    }

	 
	 
	
}

