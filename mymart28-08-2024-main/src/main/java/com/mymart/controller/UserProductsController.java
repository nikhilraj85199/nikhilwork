package com.mymart.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mymart.model.Category;
import com.mymart.model.Deal;
import com.mymart.model.Discount;
import com.mymart.model.Product;
import com.mymart.model.ProductDto;
import com.mymart.model.Rating;
import com.mymart.model.User;
import com.mymart.repository.ProductsRepository;
import com.mymart.repository.RatingRepository;
import com.mymart.service.CategoryService;
import com.mymart.service.DealService;
import com.mymart.service.DiscountService;
import com.mymart.service.FilterService;
import com.mymart.service.PriceRangeService;
import com.mymart.service.ProductService;
import com.mymart.service.RatingService;
import com.mymart.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/User")
public class UserProductsController {

	private final ProductsRepository repo;
	private final DealService dealService;
	private final FilterService filterService;
	private final UserService userService;
	private final RatingRepository ratingRepository;
	private final ProductService productService;
	private final CategoryService categoryService;

	@Autowired
	private DiscountService discountService;

	@Autowired
	private final RatingService ratingService;

	@Autowired
	private final PriceRangeService priceRangeService;

	@Autowired
	public UserProductsController(ProductsRepository repo, DealService dealService, FilterService filterService,
			UserService userService, RatingRepository ratingRepository, ProductService productService,
			CategoryService categoryService, RatingService ratingService, PriceRangeService priceRangeService) {
		this.repo = repo;
		this.dealService = dealService;
		this.filterService = filterService;
		this.userService = userService;
		this.ratingRepository = ratingRepository;
		this.productService = productService;
		this.categoryService = categoryService;
		this.ratingService = ratingService;
		this.priceRangeService = priceRangeService;
	}

	
	/*
	 * @GetMapping("{categoryName}") public String
	 * displayProductsByCategory(@PathVariable String categoryName, Model model) {
	 * Category category = categoryService.getCategoryByName(categoryName);
	 * 
	 * if (category == null) { return "error"; }
	 * 
	 * List<Product> products = productService.getProductsByCategory(category);
	 * List<Deal> deals = dealService.getAllDeals();
	 * 
	 * Map<Integer, Double> averageRatings = new HashMap<>(); Map<Integer, Integer>
	 * ratingCounts = new HashMap<>(); Map<Integer, String> ratingColors = new
	 * HashMap<>();
	 * 
	 * for (Product product : products) { double averageRating =
	 * ratingService.calculateAverageRating(product); Map<String, Integer> counts =
	 * getProductRatingsAndReviewsCount(product.getId());
	 * 
	 * averageRatings.put(product.getId(), averageRating);
	 * ratingCounts.put(product.getId(), counts.get("ratingCount"));
	 * ratingColors.put(product.getId(),
	 * ratingService.determineRatingColor(averageRating, false)); // Pass false //
	 * for // average // rating } model.addAttribute("showFilterBar", false);
	 * model.addAttribute("deals", deals); model.addAttribute("category", category);
	 * model.addAttribute("products", products);
	 * model.addAttribute("averageRatings", averageRatings);
	 * model.addAttribute("ratingCounts", ratingCounts);
	 * model.addAttribute("ratingColors", ratingColors);
	 * 
	 * return "products/UserProduct"; }
	 * 
	 * 
	 * @GetMapping("/All Products") public String showProductList(Model model) {
	 * List<Product> products = repo.findAll(); List<Deal> deals =
	 * dealService.getAllDeals(); List<Category> categories =
	 * categoryService.getAllCategories(); List<Discount> discounts =
	 * discountService.getAllDiscounts();
	 * 
	 * Map<Integer, Double> averageRatings = new HashMap<>(); Map<Integer, Integer>
	 * ratingCounts = new HashMap<>(); Map<Integer, String> ratingColors = new
	 * HashMap<>();
	 * 
	 * for (Product product : products) { double averageRating =
	 * ratingService.calculateAverageRating(product); Map<String, Integer> counts =
	 * getProductRatingsAndReviewsCount(product.getId());
	 * 
	 * averageRatings.put(product.getId(), averageRating);
	 * ratingCounts.put(product.getId(), counts.get("ratingCount"));
	 * ratingColors.put(product.getId(),
	 * ratingService.determineRatingColor(averageRating, false)); }
	 * 
	 * model.addAttribute("showFilterBar", true); model.addAttribute("products",
	 * products); model.addAttribute("averageRatings", averageRatings);
	 * model.addAttribute("ratingCounts", ratingCounts); model.addAttribute("deals",
	 * deals); model.addAttribute("ratingColors", ratingColors);
	 * model.addAttribute("categories", categories);
	 * 
	 * List<Category> allCategories = categoryService.getAllCategories();
	 * Map<String, Integer> categoryProductCounts = new HashMap<>();
	 * 
	 * for (Category category : allCategories) { int count =
	 * productService.countProductsByCategory(category);
	 * categoryProductCounts.put(category.getName(), count); }
	 * 
	 * model.addAttribute("showFilterBar", true); model.addAttribute("categories",
	 * allCategories); model.addAttribute("categoryProductCounts",
	 * categoryProductCounts); model.addAttribute("priceRanges",
	 * priceRangeService.getPriceRanges());
	 * 
	 * List<String> brands =
	 * products.stream().map(Product::getBrand).distinct().collect(Collectors.toList
	 * ()); model.addAttribute("brands", brands);
	 * 
	 * model.addAttribute("discounts", discounts);
	 * 
	 * // Optional: Load some default products model.addAttribute("products",
	 * productService.getAllProducts());
	 * 
	 * return "products/UserProduct"; }
	 * 
	 * 
	 * 
	 * @GetMapping("/viewproduct") public String showEditPage1(Model
	 * model, @RequestParam int id,
	 * 
	 * @RequestParam(defaultValue = "latestReviews") String sortField, Principal
	 * principal) { try { model.addAttribute("categories",
	 * categoryService.getAllCategories());
	 * 
	 * Product product = repo.findById(id).orElse(null); if (product == null) {
	 * return "redirect:/Products"; }
	 * 
	 * model.addAttribute("product", product);
	 * 
	 * ProductDto productDto = new ProductDto();
	 * productDto.setName(product.getName());
	 * productDto.setBrand(product.getBrand());
	 * productDto.setCategory(product.getCategory());
	 * productDto.setPrice(product.getPrice());
	 * productDto.setDescription(product.getDescription());
	 * model.addAttribute("productDto", productDto);
	 * 
	 * List<Rating> reviews = ratingRepository.findAllByProduct(product,
	 * ratingService.getSortSpecification(sortField));
	 * 
	 * model.addAttribute("reviews", reviews); model.addAttribute("sortField",
	 * sortField); model.addAttribute("ratingService", ratingService);
	 * 
	 * double averageRating = ratingService.calculateAverageRating(product);
	 * model.addAttribute("averageRating", averageRating);
	 * 
	 * Map<String, Integer> counts = getProductRatingsAndReviewsCount(id);
	 * model.addAttribute("ratingCount", counts.get("ratingCount"));
	 * model.addAttribute("reviewCount", counts.get("reviewCount"));
	 * 
	 * String ratingColor = ratingService.determineRatingColor(averageRating,
	 * false); model.addAttribute("ratingColor", ratingColor);
	 * 
	 * if (principal != null) { String username = principal.getName(); User
	 * currentUser = userService.findByEmail(username); Rating userRating =
	 * ratingRepository.findByUserAndProduct(currentUser, product);
	 * model.addAttribute("userRating", userRating != null ? userRating.getRating()
	 * : 0); }
	 * 
	 * } catch (Exception ex) { System.out.println("Exception: " + ex.getMessage());
	 * return "redirect:/Products"; } return "index"; }
	 */
	@PostMapping("/rateProduct")
	public String rateProduct(@RequestParam("productId") int productId, @RequestParam("rating") double rating,
			@RequestParam("review") String review, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			redirectAttributes.addFlashAttribute("error", "Please log in to submit.");
			return "redirect:/User/viewproduct?id=" + productId;
		}

		User currentUser = userService.getCurrentUser();
		Product product = productService.getProductById(productId);

		if (product == null || currentUser == null) {
			redirectAttributes.addFlashAttribute("error", "Invalid product or user.");
			return "redirect:/User/viewproduct?id=" + productId;
		}

		Rating existingRating = ratingRepository.findByUserAndProduct(currentUser, product);
		LocalDateTime dateTime = LocalDateTime.now();

		if (existingRating != null) {
			existingRating.setRating(rating);
			existingRating.setReview(review);
			existingRating.setDateTime(dateTime);
			ratingRepository.save(existingRating);
		} else {
			Rating newRating = new Rating();
			newRating.setUser(currentUser);
			newRating.setProduct(product);
			newRating.setRating(rating);
			newRating.setReview(review);
			newRating.setDateTime(dateTime);
			ratingRepository.save(newRating);
		}

		ratingService.rateProduct(currentUser, product, rating);
		redirectAttributes.addFlashAttribute("success", "Rating and review submitted successfully.");
		return "redirect:/User/viewproduct?id=" + productId;
	}

	public Map<String, Integer> getProductRatingsAndReviewsCount(int productId) {
		Product product = productService.getProductById(productId);
		if (product == null) {
			return Map.of("ratingCount", 0, "reviewCount", 0);
		}
		List<Rating> ratings = product.getRatings();
		long ratingCount = ratings.size();
		long reviewCount = ratings.stream()
				.filter(rating -> rating.getReview() != null && !rating.getReview().isEmpty()).map(Rating::getUser)
				.distinct().count();

		return Map.of("ratingCount", (int) ratingCount, "reviewCount", (int) reviewCount);
	}

	@PostMapping("/deleteRating")
	public String deleteRating(@RequestParam("productId") int productId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			redirectAttributes.addFlashAttribute("error", "Please log in to delete rating.");
			return "redirect:/User/viewproduct?id=" + productId;
		}
		User currentUser = userService.getCurrentUser();
		Product product = productService.getProductById(productId);
		if (product == null || currentUser == null) {
			redirectAttributes.addFlashAttribute("error", "Please log in to delete rating.");
			return "redirect:/User/viewproduct?id=" + productId;
		}
		Rating userRating = ratingRepository.findByUserAndProduct(currentUser, product);
		if (userRating != null) {
			ratingRepository.delete(userRating);
			redirectAttributes.addFlashAttribute("success", "Rating deleted successfully.");
		} else {
			redirectAttributes.addFlashAttribute("error", "No rating found to delete.");
		}
		return "redirect:/User/viewproduct?id=" + productId;
	}

	/*@GetMapping("/filterByCategory")
	public String filterByCategory(
			@RequestParam(value = "selectedCategories", required = false) List<String> selectedCategories,
			@RequestParam(value = "priceRange", required = false) String priceRange,
			@RequestParam(value = "selectedBrands", required = false) List<String> selectedBrands,
			@RequestParam(value = "selectedDiscounts", required = false) List<Integer> selectedDiscounts, // Added
																											// parameter
			Model model) {

		List<Product> filteredProducts;
		List<Category> allCategories = categoryService.getAllCategories();
		List<String> allBrands = productService.getAllBrands(); // Get all distinct brands
		List<Discount> allDiscounts = discountService.getAllDiscounts(); // Fetch discounts from DiscountService
		Map<String, Integer> categoryProductCounts = new HashMap<>();

		// Populate the product count for each category
		for (Category category : allCategories) {
			int count = productService.countProductsByCategory(category);
			categoryProductCounts.put(category.getName(), count);
		}

		// Filtering by category
		if (selectedCategories == null || selectedCategories.isEmpty()) {
			filteredProducts = productService.getAllProducts();
		} else {
			List<Category> categories = categoryService.getCategoriesByName(selectedCategories);
			filteredProducts = productService.getProductsByCategories(categories);
		}

		// Filtering by price range
		if (priceRange != null && !priceRange.isEmpty()) {
			String[] range = priceRange.split("-");
			double minPrice = Double.parseDouble(range[0]);
			double maxPrice = range[1].equals("above") ? Double.MAX_VALUE : Double.parseDouble(range[1]);

			filteredProducts = filteredProducts.stream()
					.filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
					.collect(Collectors.toList());
		}

		// Filtering by brand
		if (selectedBrands != null && !selectedBrands.isEmpty()) {
			filteredProducts = filteredProducts.stream().filter(product -> selectedBrands.contains(product.getBrand()))
					.collect(Collectors.toList());
		}

		// Filtering by discount
		if (selectedDiscounts != null && !selectedDiscounts.isEmpty()) {
			filteredProducts = filteredProducts.stream()
					.filter(product -> product.getDeal() != null
							&& selectedDiscounts.stream().anyMatch(d -> product.getDeal().getDiscount() >= d))
					.collect(Collectors.toList());
		}

		model.addAttribute("showFilterBar", true);
		model.addAttribute("products", filteredProducts);
		model.addAttribute("categories", allCategories);
		model.addAttribute("categoryProductCounts", categoryProductCounts);
		model.addAttribute("priceRanges", priceRangeService.getPriceRanges());
		model.addAttribute("selectedCategories", selectedCategories);
		model.addAttribute("priceRange", priceRange);
		model.addAttribute("brands", allBrands);
		model.addAttribute("selectedBrands", selectedBrands);
		model.addAttribute("selectedDiscounts", selectedDiscounts); 
		model.addAttribute("discounts", allDiscounts); 

		return "index";*/
	}

