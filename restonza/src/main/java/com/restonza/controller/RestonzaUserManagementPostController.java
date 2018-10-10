/**
 * 
 */
package com.restonza.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.UserDetails;
import com.restonza.dao.repository.UserDetailsRepository;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.RestonzaRestResponseVO;
import com.restonza.vo.UserDetailsVO;

/**
 * @author flex-grow developers
 * Class used for operation based on user management
 *
 */
@RestController
@RequestMapping("/restonza")
public class RestonzaUserManagementPostController {
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	

	/**
	 * post request for adding user
	 * @param UserDetailsVO
	 * @return
	 */
	@PostMapping("/addUser")
	public @ResponseBody RestonzaRestResponseVO executeAddUser(@RequestBody UserDetailsVO userDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		UserDetails userDetails = new UserDetails();
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(userDetailsVO)) {
				if (userDetailsRepository.isExist(userDetailsVO.getUsername()) < 1) {
					userDetails = userDetails.prepareUserDetails(userDetails, userDetailsVO);
					userDetailsRepository.save(userDetails);
					restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully added user details");
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Username already exist");
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Error while adding user");
			}
			
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Error while adding user");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for updating user
	 * @param UserDetailsVO
	 * @return
	 */
	@PostMapping("/updateUser")
	public @ResponseBody RestonzaRestResponseVO executeUpdateUser(@RequestBody UserDetailsVO userDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		UserDetails userDetails = new UserDetails();
		String checkNullFields[] = {"id", "password", "email", "address", "phonenumber"};
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(userDetailsVO, checkNullFields)) {
				userDetails = userDetailsRepository.findOne(Integer.parseInt(userDetailsVO.getId()));
				userDetails.setPassword(userDetailsVO.getPassword());
				userDetails.setEmail(userDetailsVO.getEmail());
				userDetails.setAddress(userDetailsVO.getAddress());
				userDetails.setPhonenumber(userDetailsVO.getPhonenumber());
				userDetailsRepository.save(userDetails);
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully updated user details");
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Error while updating user");
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Error while updating user");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting user
	 * @param UserDetailsVO
	 * @return
	 */
	@PostMapping("/getUser")
	public @ResponseBody RestonzaRestResponseVO executeGetUser(@RequestBody UserDetailsVO userDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String checkNullFields[] = {"id"};
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(userDetailsVO, checkNullFields)) {
				UserDetails userDetails = userDetailsRepository.getUserDetails(Integer.parseInt(userDetailsVO.getId()));
				userDetailsVO = new UserDetailsVO(userDetails.getFullname(), userDetails.getEmail(), userDetails.getAddress(), userDetails.getPhonenumber());
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", userDetailsVO);
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Error while updating user");
		}
		return restonzaRestResponseVO;
	}
	
	//user manage controller methods
	/**
	 * used for getting all users for particular hotel
	 * @param userDetailsVO
	 * @return
	 */
	@PostMapping("/getAllUsers")
	public @ResponseBody RestonzaRestResponseVO executeGetAllUser(@RequestBody UserDetailsVO userDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String checkNullFields[] = {"id","hotel_id"};
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(userDetailsVO, checkNullFields)) {
				List<UserDetails> userDetails = userDetailsRepository.getAllUsersDetails(Integer.parseInt(userDetailsVO.getId()),Integer.parseInt(userDetailsVO.getHotel_id()));
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", userDetails);
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Error while fetching all user");
		}
		return restonzaRestResponseVO;
	}

	/**
	 * update user entry
	 * @param userDetailsVO
	 * @return
	 */
	@PostMapping("/updateUserEntry")
	public @ResponseBody RestonzaRestResponseVO executeUpdateUserEntry(@RequestBody UserDetailsVO userDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String checkNullFields[] = {"id","password","address","salary","phonenumber","email","dateofjoining", "user_role", "status"};
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(userDetailsVO, checkNullFields)) {
				int id = Integer.parseInt(userDetailsVO.getId());
				String password = userDetailsVO.getPassword();
				String address = userDetailsVO.getAddress();
				double salary = Double.parseDouble(userDetailsVO.getSalary());
				String phonenumber = userDetailsVO.getPhonenumber();
				String email = userDetailsVO.getEmail();
				String user_role = userDetailsVO.getUser_role();
				boolean status = userDetailsVO.getStatus().equals("true") ? true : false;
				userDetailsRepository.updateUser(id, password, address, salary, phonenumber, email, user_role, status);
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully updated user");
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Error while fetching all user");
		}
		return restonzaRestResponseVO;
	}
	/**
	 * delete user
	 * @param id
	 * @return
	 */
	@PostMapping("/deleteUser/{id}")
	public @ResponseBody RestonzaRestResponseVO executeDeleteUser(@PathVariable("id") String id) {
		userDetailsRepository.delete(Integer.parseInt(id));
		return new RestonzaRestResponseVO("success", "Successfully deleted user");
	}

}
