package com.library.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.library.binding.Cart;
import com.library.binding.SignUpUser;
import com.library.enums.UserAdmin;
import com.library.mysql.DbController;

@Controller
public class CartController {

	@RequestMapping(value = "/cart" , method = {RequestMethod.POST , RequestMethod.GET})
	public ModelAndView cart(HttpSession session) {
		
		ModelAndView view = new ModelAndView("cart_window") ;
		
		DbController db = new DbController() ;
		SignUpUser signed = (SignUpUser) session.getAttribute("signed_user"); 
		
		
		// check user info for security
		if(signed == null) {
			ModelAndView sign_view = new ModelAndView("signin_window") ;
			return sign_view ;
		}
		
		
		// get books from user's cart
		Cart cart = db.get_cart(signed) ;
				
		// send books in cart to front end 
		view.addObject("cart", cart) ;
		view.addObject("admin_rights", signed.getUserAdmin().equals(UserAdmin.ADMIN) ? 1 : 0 ) ;
		view.addObject("user_name", signed.getFirstName()) ;
				
		return view ;
	}
	
	

	@RequestMapping(value = "/cart/delete" , method = {RequestMethod.POST , RequestMethod.GET})
	public ModelAndView delete(@RequestParam("isbn") Integer isbn , HttpSession session) {
		
		SignUpUser signed = (SignUpUser) session.getAttribute("signed_user"); 
		// check user info for security
		if(signed == null) {
			ModelAndView sign_view = new ModelAndView("signin_window") ;
			return sign_view ;
		}
		
		DbController db = new DbController() ;

		
		//TODO delete order from user 
		db.delete_from_cart(signed, isbn);
		
		ModelAndView view = new ModelAndView(new RedirectView("/Library/cart")) ;
		
		view.addObject("admin_rights", signed.getUserAdmin().equals(UserAdmin.ADMIN) ? 1 : 0 ) ;
		view.addObject("user_name", signed.getFirstName()) ;
		
		return view ;
	}
	
}
