package com.example.test.Service;

import com.example.test.Entity.AuthenticationType;
import com.example.test.Entity.Role;
import com.example.test.Entity.Users;
import com.example.test.Exception.UserNotFoundException;
import com.example.test.Repository.RoleRepository;
import com.example.test.Repository.UserRepository;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserServices {

	@Autowired
	private UserRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;

	private final RoleRepository roleRepository;

	@Autowired
	public UserServices(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}


	public List<Users> listAll() {
		return repo.findAll();
	}

//	public void register(Users user, String siteURL, Model model)
//			throws UnsupportedEncodingException, MessagingException {
//		List<Role> roles= roleRepository.findRoleByName("MEMBER");
//		String encodedPassword = passwordEncoder.encode(user.getPassword());
//		user.setPassword(encodedPassword);
//		user.setRoleList(roles);
//		String randomCode = RandomString.make(64);
//		user.setVerificationCode(randomCode);
//		user.setStatus(0);
//		user.setAuthenticationType(AuthenticationType.DATABASE);
//
//		repo.save(user);
//
//		sendVerificationEmail(user, siteURL);
//	}

	public void registerUser(Users user)throws UnsupportedEncodingException, MessagingException {
		List<Role> roles= roleRepository.findRoleByName("MEMBER");
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setRoleList(roles);
		user.setPassword(encodedPassword);
		user.setStatus(0);
		user.setAuthenticationType(AuthenticationType.DATABASE);


		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);

		System.out.println("Verification code: " +randomCode);

		repo.save(user);

	}

//	private void sendVerificationEmail(Users user, String siteURL)
//			throws MessagingException, UnsupportedEncodingException {
//		String toAddress = user.getEmail();
//		String fromAddress = "thanhdatdo99cnh@gmail.com";
//		String senderName = "Công ty thiết bị vật tư y tế";
//		String subject = "Please verify your registration";
//		String content = "Dear [[name]],<br>"
//				+ "Please click the link below to verify your registration:<br>"
//				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
//				+ "Thank you,<br>"
//				+ "Công ty thiết bị y tế.";
//
//		MimeMessage message = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//
//		helper.setFrom(fromAddress, senderName);
//		helper.setTo(toAddress);
//		helper.setSubject(subject);
//
//		content = content.replace("[[name]]", user.getUsername());
//		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
//
//		System.out.println(user.getVerificationCode());
//
//		content = content.replace("[[URL]]", verifyURL);
//
//		System.out.println(verifyURL);
//		helper.setText(content, true);
//
//		mailSender.send(message);
//
//		System.out.println("Email has been sent");
//	}
	
	public boolean verify(String verificationCode) {
		Users user = repo.findByVerificationCode(verificationCode);
		if (user == null || user.getStatus()==1) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setStatus(1);
			repo.save(user);
			return true;
		}
	}

	public void updateAuthenticationType(Users users, AuthenticationType type) {
		if (!users.getAuthenticationType().equals(type)){
			repo.updateAuthenticationType(users.getId(), type);
		}
	}


	public Users getUserByEmail(String email) {
		return repo.findByEmail(email);
	}

	public void addNewUserUponOAuthLogin(String name, String email,
										 AuthenticationType authenticationType) {
//            Customer customer = new Customer();
		Users users = new Users();
		users.setEmail(email);
		users.setUsername(name);
		users.setAuthenticationType(authenticationType);
		users.setPhone("");
		users.setStatus(1);

		repo.save(users);
	}

	public void save(Users userInForm){
		Users usersInDB = repo.findById(userInForm.getId()).get();

		if(!userInForm.getPassword().isEmpty()){
			String encodedPassword = passwordEncoder.encode(userInForm.getPassword());
			userInForm.setPassword(encodedPassword);
		}else {
			userInForm.setPassword(usersInDB.getPassword());
		}

		userInForm.setAuthenticationType(usersInDB.getAuthenticationType());
		userInForm.setResetPasswordToken(usersInDB.getResetPasswordToken());

		repo.save(userInForm);
	}

	public String updateResetPasswordToken(String email) throws UserNotFoundException {
		Users users = repo.findByEmail(email);
		if(users != null){
			String token = RandomString.make(30);
			users.setResetPasswordToken(token);
			repo.save(users);

			return token;
		}else {
			throw new UserNotFoundException("Could not find any user with the email" + email);
		}
	}

	public Users getByResetPasswordToken(String token) {
		return repo.findByResetPasswordToken(token);
	}

	public void updatePassword(String token, String newPassword) throws UserNotFoundException {
		Users user = repo.findByResetPasswordToken(token);
		if (user == null) {
			throw new UserNotFoundException("No user found: invalid Token");
		}
		user.setPassword(newPassword);
		user.setResetPasswordToken(null);
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);


		repo.save(user);
	}

	public boolean isEmailUnique(String email) {
		Users users = repo.findByEmail(email);
		return users == null;
	}
}
