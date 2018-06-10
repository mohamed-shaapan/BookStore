package com.library.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.library.binding.Order;
import com.library.binding.SignUpUser;
import com.library.enums.UserAdmin;
import com.library.mysql.DbController;

@Controller
public class MyOrdersController {

	@RequestMapping(value = "/orders" , method = {RequestMethod.POST , RequestMethod.GET})
	public ModelAndView orders(HttpSession session) {
		
		
		
		ModelAndView orders_view = new ModelAndView("my_orders_window");
		
		
		SignUpUser signed = (SignUpUser) session.getAttribute("signed_user");
		// check user info for security
		if(signed == null) {
			ModelAndView sign_view = new ModelAndView("signin_window") ;
			return sign_view ;
		}
		DbController db = new DbController() ;
		
		// get orders from database 
		List<Order> orders = db.get_user_order(signed) ; 
		
		// send orders to front end 
		orders_view.addObject("orders", orders) ;
		orders_view.addObject("admin_rights", signed.getUserAdmin().equals(UserAdmin.ADMIN) ? 1 : 0 ) ;
		orders_view.addObject("user_name", signed.getFirstName()) ;
		
		return orders_view;
	}
}
