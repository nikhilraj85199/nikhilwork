package com.mymart.controller;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mymart.ProductFilter;
import com.mymart.model.Aboutus;
import com.mymart.model.CardItem;
import com.mymart.model.CarouselImage;
import com.mymart.model.Category;
import com.mymart.model.Consumerpolicy;
import com.mymart.model.Customersupport;
import com.mymart.model.Deal;
import com.mymart.model.Discount;
import com.mymart.model.DropdownItem;
import com.mymart.model.Filter;
import com.mymart.model.Mailus;
import com.mymart.model.MovingCards;
import com.mymart.model.NavLink;
import com.mymart.model.Popup;
import com.mymart.model.PopupDto;
import com.mymart.model.Product;
import com.mymart.model.ProductDto;
import com.mymart.model.Rating;
import com.mymart.model.Roaddress;
import com.mymart.model.User;
import com.mymart.model.carousel;
import com.mymart.repository.Aboutus1Repository;
import com.mymart.repository.AboutusRepository;
import com.mymart.repository.ConsumerpolicyRepository;
import com.mymart.repository.CustomersupportRepository;
import com.mymart.repository.DownloadappsRepository;
import com.mymart.repository.KeepintouchRepository;
import com.mymart.repository.MailusRepository;
import com.mymart.repository.MovingCardsRepository;
import com.mymart.repository.NavLinkRepository;
import com.mymart.repository.PopupRepository;
import com.mymart.repository.ProductsRepository;
import com.mymart.repository.RatingRepository;
import com.mymart.repository.RoaddressRepository;
import com.mymart.repository.UserRepository;
import com.mymart.repository.carouselRepository;
import com.mymart.service.CardItemService;
import com.mymart.service.CarouselImageService;
import com.mymart.service.CategoryService;
import com.mymart.service.DealService;
import com.mymart.service.DiscountService;
import com.mymart.service.FilterService;
import com.mymart.service.NavBarService;
import com.mymart.service.PriceRangeService;
import com.mymart.service.ProductService;
import com.mymart.service.RatingService;
import com.mymart.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    private NavLinkRepository NavLinkRepository;
    @Autowired
	 private Aboutus1Repository aboutus1Repository;
    @Autowired
	private CardItemService cardItemService;
	
    @Autowired
	private CarouselImageService carouselImageService;
	 @Autowired
	    private AboutusRepository aboutusRepository;
	 
	 
	    @Autowired
		private carouselRepository crepo;

	    @Autowired
	    private ConsumerpolicyRepository consumerpolicyRepository;

	    @Autowired
	    private CustomersupportRepository customersupportRepository;
	    
	    @Autowired
	    private DownloadappsRepository downloadappsRepository;

	    @Autowired
	    private KeepintouchRepository keepintouchRepository;
	    
	    @Autowired
	    private MailusRepository mailusRepository;
	    
	    @Autowired
	    private RoaddressRepository roaddressRepository;
	    
	    @Autowired
		private MovingCardsRepository cardrepo;
	    @Autowired
		PopupRepository repo;
	   
	    private final ProductsRepository prepo;
	private final NavBarService service; // Inject your service
	
	
    public HomeController(NavBarService service , ProductsRepository prepo, DealService dealService, FilterService filterService,
		 RatingRepository ratingRepository, ProductService productService,
			CategoryService categoryService, RatingService ratingService, PriceRangeService priceRangeService) {
        this.service = service;
        this.prepo = prepo;
		this.dealService = dealService;
		this.filterService = filterService;
		
		this.ratingRepository = ratingRepository;
		this.productService = productService;
		this.categoryService = categoryService;
		this.ratingService = ratingService;
		this.priceRangeService = priceRangeService;
    }
	
    private final DealService dealService;
	private final FilterService filterService;
	
	private final RatingRepository ratingRepository;
	private final ProductService productService;
	private final CategoryService categoryService;

	@Autowired
	private DiscountService discountService;

	@Autowired
	private final RatingService ratingService;

	@Autowired
	private final PriceRangeService priceRangeService;

	
	
	@GetMapping({"/", "User/{categoryName}", "User/filterByCategory", "User/viewproduct"})
	public String index(
	        @PathVariable(required = false) String categoryName,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String brand,
	        @RequestParam(required = false) String category,
	        @RequestParam(required = false) Double price,
	        @RequestParam(required = false) boolean ajax,
	        Model model,
	        @RequestParam(value = "selectedCategories", required = false) List<String> selectedCategories,
	        @RequestParam(value = "priceRange", required = false) String priceRange,
	        @RequestParam(value = "selectedBrands", required = false) List<String> selectedBrands,
	        @RequestParam(value = "selectedDiscounts", required = false) List<Integer> selectedDiscounts,
	        @RequestParam(value = "id", required = false, defaultValue = "-1") int id,
	        @RequestParam(defaultValue = "latestReviews") String sortField,
	        Principal principal) {

	    // Common data for the homepage
	    List<NavLink> allNavLinks = service.getAllNavLinks();
	    Map<NavLink, List<DropdownItem>> navbarWithDropdownData = service.getNavbarWithDropdownData();
	    List<CarouselImage> images = carouselImageService.getAllImages();
	    List<Popup> image = repo.findAll();
	    List<CardItem> carditem = cardItemService.getAllCardItems();
	    List<MovingCards> card = cardrepo.findAll();

	    // Data for the footer from the database
	    List<Aboutus> aboutusList = aboutus1Repository.findAll();
	    List<Consumerpolicy> consumerpolicyList = consumerpolicyRepository.findAll();
	    List<Customersupport> customersupportList = customersupportRepository.findAll();
	    List<com.mymart.model.Downloadapps> downloadappsList = downloadappsRepository.findAll();
	    List<com.mymart.model.Keepintouch> keepintouchList = keepintouchRepository.findAll();
	    List<Mailus> mailusList = mailusRepository.findAll();
	    List<Roaddress> roaddressList = roaddressRepository.findAll();
	    List<carousel> carousel = crepo.findAll();

	    // Add common data to the model
	    model.addAttribute("images", images);
	    model.addAttribute("image", image);
	    model.addAttribute("carditem", carditem);
	    model.addAttribute("card", card);
	    model.addAttribute("aboutusList", aboutusList);
	    model.addAttribute("consumerpolicyList", consumerpolicyList);
	    model.addAttribute("customersupportList", customersupportList);
	    model.addAttribute("downloadappsList", downloadappsList);
	    model.addAttribute("keepintouchList", keepintouchList);
	    model.addAttribute("mailusList", mailusList);
	    model.addAttribute("roaddressList", roaddressList);
	    model.addAttribute("carousel", carousel);
	    model.addAttribute("allNavLinks", allNavLinks);
	    model.addAttribute("navbarWithDropdownData", navbarWithDropdownData);
	    model.addAttribute("googleLink", "https://www.google.com");
	    model.addAttribute("cardItems", cardItemService.getAllCardItems());

	    // Default values
	    List<Product> products = new ArrayList<>();
	    boolean showFilterBar = false;

	    if (categoryName != null) {
	        Category category1 = categoryService.getCategoryByName(categoryName);

	        if (category1 != null) {
	            products = productService.getProductsByCategory(category1);
	            showFilterBar = true; // Show filters if a category is selected
	        } else {
	            return "error"; // Return error page if the category is not found
	        }
	    } else if (selectedCategories != null && !selectedCategories.isEmpty()) {
	        List<Category> categories = categoryService.getCategoriesByName(selectedCategories);
	        products = productService.getProductsByCategories(categories);
	        showFilterBar = true; // Show filters if selected categories are not empty
	    } else {
	        // Products and filter bar are not shown if no category is selected
	        products = new ArrayList<>(); // Ensure products are empty
	    }

	    List<Deal> deals = dealService.getAllDeals();

	    Map<Integer, Double> averageRatings = new HashMap<>();
	    Map<Integer, Integer> ratingCounts = new HashMap<>();
	    Map<Integer, String> ratingColors = new HashMap<>();

	    for (Product product : products) {
	        double averageRating = ratingService.calculateAverageRating(product);
	        Map<String, Integer> counts = getProductRatingsAndReviewsCount(product.getId());

	        averageRatings.put(product.getId(), averageRating);
	        ratingCounts.put(product.getId(), counts != null ? counts.getOrDefault("ratingCount", 0) : 0);
	        ratingColors.put(product.getId(), ratingService.determineRatingColor(averageRating, false));
	    }

	    model.addAttribute("showFilterBar", showFilterBar); // Conditional display of filter bar
	    model.addAttribute("products", products);
	    model.addAttribute("deals", deals);
	    model.addAttribute("averageRatings", averageRatings);
	    model.addAttribute("ratingCounts", ratingCounts);
	    model.addAttribute("ratingColors", ratingColors);

	    List<Category> allCategories = categoryService.getAllCategories();
	    List<String> allBrands = productService.getAllBrands();
	    List<Discount> allDiscounts = discountService.getAllDiscounts();
	    Map<String, Integer> categoryProductCounts = new HashMap<>();

	    // Populate the product count for each category
	    for (Category category1 : allCategories) {
	        int count = productService.countProductsByCategory(category1);
	        categoryProductCounts.put(category1.getName(), count);
	    }

	    model.addAttribute("categories", allCategories);
	    model.addAttribute("categoryProductCounts", categoryProductCounts);
	    model.addAttribute("priceRanges", priceRangeService.getPriceRanges());
	    model.addAttribute("selectedCategories", selectedCategories);
	    model.addAttribute("priceRange", priceRange);
	    model.addAttribute("brands", allBrands);
	    model.addAttribute("selectedBrands", selectedBrands);
	    model.addAttribute("selectedDiscounts", selectedDiscounts);
	    model.addAttribute("discounts", allDiscounts);

	    // Handle product details view
	    if (id != -1) {
	        try {
	            model.addAttribute("categories", categoryService.getAllCategories());

	            Product product = prepo.findById(id).orElse(null);
	            if (product == null) {
	                return "redirect:/User"; // Redirect to the homepage if product not found
	            }

	            model.addAttribute("product", product);

	            ProductDto productDto = new ProductDto();
	            productDto.setName(product.getName());
	            productDto.setBrand(product.getBrand());
	            productDto.setCategory(product.getCategory());
	            productDto.setPrice(product.getPrice());
	            productDto.setDescription(product.getDescription());
	            model.addAttribute("productDto", productDto);

	            List<Rating> reviews = ratingRepository.findAllByProduct(product,
	                    ratingService.getSortSpecification(sortField));

	            model.addAttribute("reviews", reviews);
	            model.addAttribute("sortField", sortField);
	            model.addAttribute("ratingService", ratingService);

	            double averageRating = ratingService.calculateAverageRating(product);
	            model.addAttribute("averageRating", averageRating);

	            Map<String, Integer> counts = getProductRatingsAndReviewsCount(id);
	            model.addAttribute("ratingCount", counts.get("ratingCount"));
	            model.addAttribute("reviewCount", counts.get("reviewCount"));

	            String ratingColor = ratingService.determineRatingColor(averageRating, false);
	            model.addAttribute("ratingColor", ratingColor);

	            if (principal != null) {
	                String username = principal.getName();
	                User currentUser = userService.findByEmail(username);
	                Rating userRating = ratingRepository.findByUserAndProduct(currentUser, product);
	                model.addAttribute("userRating", userRating != null ? userRating.getRating() : 0);
	            }
	        } catch (Exception ex) {
	            System.out.println("Exception: " + ex.getMessage());
	            return "redirect:/User"; // Redirect to the homepage if an exception occurs
	        }
	    }

	 // Return the HTML template name
	    return "index";
	
	}

	
	private Map<String, Integer> getProductRatingsAndReviewsCount(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	
	
	 @GetMapping("/Admin/Home")
	    public String adminPagess(Model model) {

		 return "redirect:/admin-page";
	    }
	
	
	 @GetMapping("/About")

	    public String showAboutusPageabout(Model model)

	    {
		 Map<NavLink, List<DropdownItem>> navbarWithDropdownData = service.getNavbarWithDropdownData();
		 model.addAttribute("navbarWithDropdownData", navbarWithDropdownData);
	    	NavLink navlink = NavLinkRepository.findById(3L).orElse(null);

	        // Check if aboutus is not null
	        if (navlink != null) {
	            // Add the specific About Us data to the model
	            model.addAttribute("navlink", navlink);
	            return "Aboutus"; // Assuming "aboutus" is the name of the view for displaying About Us details
	        } else {
	            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
	            return "redirect:/Aboutus";
	}
	    
	    }


	
	@GetMapping("/aboutus/1")//footer//

    public String showAboutmymartPage(Model model)

    {

    	Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "aboutmymart"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/aboutmymart";
}
}
	@GetMapping("/aboutus/2")//footer//

    public String showCareersPage(Model model)

    {

    	Aboutus aboutus = aboutusRepository.findById(2L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "careers"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/careers";
}
}

	
	
	@GetMapping("/aboutus/3")

    public String showPressreleasesPage(Model model)

    {

    	Aboutus aboutus = aboutusRepository.findById(3L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "pressreleases"; // Assuming "pressreleases" is the name of the view for displaying About Us details
        } else {
            // If pressreleases is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/pressreleases";
}
}
	
	@GetMapping("/Keepintouch/1")
	public String Keepintouch() {
		return "redirect:https://www.google.com/";
	}
	@GetMapping("/Keepintouch/2")
	public String Keepintouch1() {
		return "redirect:https://in.linkedin.com/";
	}
	@GetMapping("/Keepintouch/3")
	public String Keepintouch2() {
		return "redirect:https://www.facebook.com/";
	}

	@GetMapping("/Keepintouch/4")
	public String Keepintouch3() {
		return "redirect:https://x.com/?lang=en";
	}

	@GetMapping("/Keepintouch/5")
	public String Keepintouch4() {
		return "redirect:https://www.instagram.com/";
	}
	
	
	
	@GetMapping("/aboutus/retail")

    public String showpressreleasePage(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "retail"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	
	
	@GetMapping("/consumerpolicy/1")

    public String showconsumerpolicy(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "security"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	
	@GetMapping("/consumerpolicy/2")

    public String showconsumerpolicypage(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "privacy"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	
	@GetMapping("/consumerpolicy/3")

    public String showconsumerpolicyPage(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "grievance"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	
	@GetMapping("/customersupport/1")

    public String showFeedbackPage(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "feedback"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}

	@GetMapping("/customersupport/2")

    public String showContactusPage(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(2L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "contactus"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}

	@GetMapping("/customersupport/3")

    public String showBecomeaSellerPage(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(3L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "becomeaseller"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}

	
	@GetMapping("/customersupport/4")

    public String showcustomersupport(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "sellerfaqs"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	
	
	@GetMapping("/conditions")

    public String showconditions(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "conditions"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	@GetMapping("/intrest")

    public String showcintrestbasedads(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "intrestbasedads"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	
	@GetMapping("/cancelreturn")

    public String showcancelreturn(Model model)

    {

        Aboutus aboutus = aboutusRepository.findById(1L).orElse(null);

        // Check if aboutus is not null
        if (aboutus != null) {
            // Add the specific About Us data to the model
            model.addAttribute("aboutus", aboutus);
            return "cancelreturn"; // Assuming "aboutus" is the name of the view for displaying About Us details
        } else {
            // If aboutus is null, redirect to a generic About Us page or handle it accordingly
            return "redirect:/index";
}
}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
//	@GetMapping("/profile")
//    public String ProfilePage() {
//    	return "login/user";
//    }
	
	
	@GetMapping("/profile")
    public String ProfilePage() {
    	return "user/user";
    }
	@GetMapping("/signout")
	 public String logout(HttpServletRequest request) {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/"; // Redirect to the root URL (http://localhost:8080) after logout
    }
	
	
	@GetMapping("/editpopup")
	public String showEditpopup(
			Model model,@RequestParam int id
			) {
		
		try {
			Popup popup=repo.findById(id).get();
			model.addAttribute("popup", popup);
			
			PopupDto popupDto=new PopupDto();
			model.addAttribute("popupDto", popupDto);
			popupDto.setPopupdata(popup.getPopupdata());
			popupDto.setPopupofferdata(popup.getPopupofferdata());
			popupDto.setPopupmarqueedata(popup.getPopupmarqueedata());
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception is : "+e.getMessage());
			return "redirect:/popup";
		}
		
		return "popup/Editpopup";
		
	}
	
	@PostMapping("/editpopup")
	public String updatePopup(
			Model model,@RequestParam int id,
			@Valid @ModelAttribute PopupDto popupDto,
			BindingResult result
			) {
		
		try {
			
			Popup popup = repo.findById(id).get();
			model.addAttribute("popup", popup);
			
			if (result.hasErrors()) {
				return "popup/Editpopup";
				
			}
			
			//code for deleting old images
			if (!popupDto.getPopupimage().isEmpty()) {
				String uploadDir ="public/images/";
				Path oldImagePath = Paths.get(uploadDir+popup.getPopupimage());
				try {
					//deletes old image
					Files.delete(oldImagePath);
				} catch (Exception e) {
					System.out.println("Exception is : "+e.getMessage());
				}
				//Saving New Image
				MultipartFile image = popupDto.getPopupimage();
				
				String storageFileName  =image.getOriginalFilename();
				
				try(InputStream inputStream= image.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
				}
				popup.setPopupimage(storageFileName);
				
			}
			
			popup.setPopupdata(popupDto.getPopupdata());
			popup.setPopupofferdata(popupDto.getPopupofferdata());
			popup.setPopupmarqueedata(popupDto.getPopupmarqueedata());
			
			
			repo.save(popup);
			
		} catch (Exception e) {
			System.out.println("Exception : "+e.getMessage());
		}
		
		return "redirect:/admin-page";
		
	}
	
	@GetMapping("/addpopupimage")
	public String ShowCreate(Model m) {
	PopupDto popupDto = new PopupDto();
	m.addAttribute("popupDto",popupDto);
		return "popup/createpopup";
	}
	
	
	
	
	@PostMapping("/addpopupimage")
	public String EditCreate(@Valid @ModelAttribute PopupDto popupDto,BindingResult result) {
		
		
		
		if(popupDto.getPopupimage().isEmpty()) {
			result.addError(new FieldError("popupDto","popupimage","this field is required"));
		}
		
	
		
		if(result.hasErrors()) {
			return "popup/createpopup";
		}
		
		MultipartFile image1 = popupDto.getPopupimage();
		String StoreImage = image1.getOriginalFilename();
		
		try {
			String uploadDir = "public/images/";
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try (InputStream inputStream = image1.getInputStream()){
				Files.copy(inputStream,Paths.get(uploadDir+StoreImage),StandardCopyOption.REPLACE_EXISTING);
				
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		 Popup popup=new Popup();
		
		 popup.setPopupdata(popupDto.getPopupdata());
		 popup.setPopupofferdata(popupDto.getPopupofferdata());
		 popup.setPopupmarqueedata(popupDto.getPopupmarqueedata());
		 popup.setPopupimage(StoreImage);
		
		repo.save(popup);
		return "redirect:/admin-page";
	}
	
	
	
	@GetMapping("/deletepopupimage")
	public String deleteProduct(
			@RequestParam int id
			) {
		
		try {
			Popup popup = repo.findById(id).get(); 
			Path imagePath = Paths.get("public/images"+popup.getPopupimage());
			
			try {
				//To delete image
				Files.delete(imagePath);
				
			} catch (Exception e) {
				System.out.println("Exception : "+e.getMessage());
			}
			//To delete product except image
			repo.delete(popup);
			
		} catch (Exception e) {
			System.out.println("Exception : "+e.getMessage());
		}
		
		return "redirect:/admin-page";
		
	}

	@GetMapping("/showpopup")
    public String showPopup(Model model) {
        List<Popup> showPopup = repo.findAll();
        model.addAttribute("showPopup", showPopup);
        return "popup/showPopup";

    }

	
	}

