package com.oriun.oriun.Controllers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.oriun.oriun.Models.ConfirmationTokenModel;
import com.oriun.oriun.Models.UserModel;
import com.oriun.oriun.Repositories.ConfirmationTokenRepository;
import com.oriun.oriun.Security.Encoder;
import com.oriun.oriun.Services.ConfirmationTokenService;
import com.oriun.oriun.Services.EmailSenderService;
import com.oriun.oriun.Services.UserService;
import java.util.Date;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.server.ResponseStatusException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import org.springframework.security.crypto.password.PasswordEncoder;

//OTH
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@RestController
public class UserController {
	@Autowired
    UserService userService;
	Encoder encoder; 

	@Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailSenderService emailSenderService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/")
    public String inicio(){
        return "Hola esta funcionando";
    }
	
	@GetMapping("/user")
    public ArrayList<UserModel> obtenerUsuarios(){
        return userService.getUsers();
    }

	@PostMapping("/modreg")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity registermod(@RequestParam("user") String user_name, @RequestParam("password") String password,@RequestParam("email") String email) {
		UserModel user= new UserModel();
		//user.setPASSWORD(passwordEncoder.encode(password));
		user.setPASSWORD((password));
		user.setENABLED(true);
		encoder= new Encoder();
		user.setPASSWORD((encoder.encode(password)));
		System.out.println(user.getPASSWORD());
		user.setUSER_NAME(user_name);
        user.setEMAIL(email);
		user.setROL_NAME("Moderador");
		Optional<UserModel> us=userService.getUser(user_name);
		if(us.isPresent()){
			return new ResponseEntity<>(
			"Este nombre ya esta en uso: "+user.getUSER_NAME()+" rol: "+user.getROL_NAME(),
			HttpStatus.UNPROCESSABLE_ENTITY);
		}else{
			UserModel res=userService.saveUser(user);
			return new ResponseEntity<>(
				"Su registro es exitoso "+user.getUSER_NAME()+" rol: "+user.getROL_NAME(),
				HttpStatus.OK);
		}
		
	}

	@PostMapping("/userreg")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity register(@RequestParam("user") String user_name, @RequestParam("password") String password,@RequestParam("email") String email) {
		UserModel user= new UserModel();
		//user.setPASSWORD(passwordEncoder.encode(password));
		encoder= new Encoder();
		user.setPASSWORD((encoder.encode(password)));
		user.setEMAIL(email);
		user.setENABLED(false);
		System.out.println(user.getPASSWORD());
		user.setUSER_NAME(user_name);
		user.setROL_NAME("Usuario");
		Optional<UserModel> us=userService.getUser(user_name);
		UserModel us2 = userService.getUserByEmail(email);
		if(us.isPresent()){
			return new ResponseEntity<>(
			"Este nombre ya esta en uso: "+user.getUSER_NAME()+" rol: "+user.getROL_NAME(),
			HttpStatus.UNPROCESSABLE_ENTITY);
		}else{
			if(us2 != null){
				return new ResponseEntity<>(
			"Tu correo ha sido registrado: "+email+"rol: "+user.getROL_NAME(),
			HttpStatus.UNPROCESSABLE_ENTITY);
			}else{
				UserModel res=userService.saveUser(user);
				ConfirmationTokenModel confirmationToken = new ConfirmationTokenModel(user.getUSER_NAME());
				confirmationTokenService.saveTK(confirmationToken);
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(user.getEMAIL());
				mailMessage.setSubject("Completar Registro!");
				mailMessage.setFrom("oriunmail@gmail.com");
				String url="http://localhost:8080/confirm-account?token="+confirmationToken.getCONFIRMATION_TOKEN();
        		String content="< href='"+url+"'>"+url+"</a>";
				String html= ("para comfirmar tu cuenta, ingresa al siguiente enlace : "
				+url);
				mailMessage.setText(html);
				//+"https://oriun.herokuapp.com/confirm-account?token="+confirmationToken.getCONFIRMATION_TOKEN());
				emailSenderService.sendEmail(mailMessage);
				return new ResponseEntity<>(
					"Su registro es exitoso "+user.getUSER_NAME()+" rol: "+user.getROL_NAME(),
					HttpStatus.OK);
			}
		}
		
	}
	



	@RequestMapping(value="/password-request", method= {RequestMethod.POST})
    public ResponseEntity PasswordReset(@RequestParam("email")String email)
    {
        UserModel user=userService.getUserByEmail(email);
		if(user!=null&&user.isENABLED()){
            if(confirmationTokenService.getbyUser(user.getUSER_NAME())!=null){
                confirmationTokenService.DeleteCTbyID(confirmationTokenService.getbyUser(user.getUSER_NAME()).getTOKEN_ID());
            }
			ConfirmationTokenModel confirmationToken = new ConfirmationTokenModel(user.getUSER_NAME());
            confirmationTokenService.saveTK(confirmationToken);
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEMAIL());
			mailMessage.setSubject("Reestablecer contraseña");
			mailMessage.setFrom("oriunmail@gmail.com");
			String url="http://localhost:8080/password-reset?token="+confirmationToken.getCONFIRMATION_TOKEN();
        	String content="< href='"+url+"'>"+url+"</a>";
			String html= ("Para cambiar su contraseña ingrese al siguiente enlace : "+url);
			mailMessage.setText(html);
			emailSenderService.sendEmail(mailMessage);
			return new ResponseEntity<>(
				"Peticion de cambio de contraseña exitoso  "+user.getUSER_NAME(),
				HttpStatus.OK);
		}else{
			return new ResponseEntity<>(
				"error correo invalido", 
				HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }

	@RequestMapping(value="/password-change", method= {RequestMethod.POST})
    public ResponseEntity PasswordChange(@RequestParam("email")String email)
    {
        UserModel user=userService.getUserByEmail(email);
		if(user!=null){
			ConfirmationTokenModel confirmationToken = new ConfirmationTokenModel(user.getUSER_NAME());
            confirmationTokenService.saveTK(confirmationToken);
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEMAIL());
			mailMessage.setSubject("Cambio de contraseña");
			mailMessage.setFrom("oriunmail@gmail.com");
			String url="http://localhost:8080/password-change?token="+confirmationToken.getCONFIRMATION_TOKEN();
        	String content="< href='"+url+"'>"+url+"</a>";
			String html= ("Para realizar el cambio de su contraseña ingrese al siguiente enlace : "+url);
			mailMessage.setText(html);
			emailSenderService.sendEmail(mailMessage);
			return new ResponseEntity<>(
				"Peticion de cambio de contraseña exitoso  "+user.getUSER_NAME(),
				HttpStatus.OK);
		}else{
			return new ResponseEntity<>(
				"error correo invalido", 
				HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }

	@RequestMapping(value="/confirm-password", method= {RequestMethod.POST})
    public ResponseEntity confirmPasswordChange(@RequestParam("token")String confirmationToken,@RequestParam("password")String password)
    {
        ConfirmationTokenModel token = confirmationTokenService.getbyToken(confirmationToken);
		Optional<UserModel> user=userService.getUser(token.getUSER_NAME());
		encoder= new Encoder();
        if(token != null)
        {
			if(user.get().getPASSWORD().equals(encoder.encode(password))){
				{
					return new ResponseEntity<>(
					"error same password", 
					HttpStatus.CONFLICT);
				}
			}
			System.out.println(token.getUSER_NAME());
            userService.updateUserPassword(token.getUSER_NAME(),encoder.encode(password));
            confirmationTokenService.DeleteCTbyID(token.getTOKEN_ID());
            return new ResponseEntity<>(
					"your password recovery is succesfull "+token.getUSER_NAME(), 
					HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(
			"error invalid token", 
			HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }



	@RequestMapping(value="/confirm-account", method= {RequestMethod.POST})
    public ResponseEntity confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        ConfirmationTokenModel token = confirmationTokenService.getbyToken(confirmationToken);
        if(token != null)
        {
			System.out.println(token.getUSER_NAME());
            userService.updateUserState(token.getUSER_NAME());
            confirmationTokenService.DeleteCTbyID(token.getTOKEN_ID());
            return new ResponseEntity<>(
					"your user register is succesfull "+token.getUSER_NAME(), 
					HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(
			"error invalid token", 
			HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

	@PostMapping("/userlog")
	public ResponseEntity login(@RequestBody HashMap<String,Object> user){
                // Note: The parameters are user_name and password
		System.out.println("user"+user.get("user_name").toString()+"password"+user.get("password").toString());
		Optional<UserModel> us=userService.getUser(user.get("user_name").toString());
		//return us.get();
		encoder= new Encoder();
		String pass=((encoder.encode(user.get("password").toString())));
		System.out.println(pass);
		if(us.isPresent()){
			if(us.get().isENABLED()){
			    if(userService.Userisbanned(us.get().getUSER_NAME())){
                    throw new ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE, "usuario baneado");
                }
			    else {
					String token = getJWTToken(user.get("user_name").toString(),us.get().getROL_NAME()); 
					//System.out.println(token.split(" ")[1]);
                    if (us.get().getPASSWORD().equals(pass)) {
                        HashMap<String, Object> result = new HashMap();
                        result.put("TOKEN", token);
                        result.put("USER_NAME", us.get().getUSER_NAME());
                        result.put("ROL_NAME", us.get().getROL_NAME());
                        return new ResponseEntity<>(
                                result, HttpStatus.OK);
                        //return us.get();
                    } else {
                        //throw new MyException("wrong password");
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "invalid password");
                    }
                }
			}else{
				throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "User is Not authenticated");
			}
		}
		else{
			throw new ResponseStatusException(
				HttpStatus.NOT_ACCEPTABLE, "User Not Found");
		}
		
	}
	
	@RequestMapping(value="/userstate", method= {RequestMethod.GET})
    public ResponseEntity Userstate(@RequestParam("user")String username)
    {

		Optional<UserModel>user=userService.getUser(username);
        if(user.isPresent())
        {
            return new ResponseEntity<>(
					user.get().isENABLED(), 
					HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(
			"error user not found", 
			HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @PutMapping("/disuser")
	public ResponseEntity  Stopuser(@RequestParam("user")String username){
		if(userService.disUser(username)){
			return new ResponseEntity<>("Usuario desactivado",
					HttpStatus.OK );
		}
		else{
			return new ResponseEntity<>("Usuario no encontrado",
					HttpStatus.NOT_FOUND );
		}
	}
	@PutMapping("/banuser")
	public ResponseEntity  banuser(@RequestParam("user")String username){
		if(userService.banUser(username)){
			return new ResponseEntity<>(username+" recibio un baneo",
					HttpStatus.OK );
		}
		else{
			return new ResponseEntity<>("Usuario no encontrado",
					HttpStatus.NOT_FOUND );
		}
	}
	@PutMapping("/opuuser")
	public ResponseEntity  chanceuser(@RequestParam("user")String username){
		if(userService.chanceUser(username)){
			Optional<UserModel> us=userService.getUser(username);
			UserModel us2 = us.get();
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(us2.getEMAIL());
			mailMessage.setSubject("Strike reduction!");
			mailMessage.setFrom("oriunmail@gmail.com");
			String content=("Su cuenta ahora tiene "+us2.getNBANNED()+" strikes, si tiene menos de 3 strikes su baneo es de maximó 24 horas");
			mailMessage.setText(content);
			emailSenderService.sendEmail(mailMessage);
			return new ResponseEntity<>(username+" tiene un strike menos",
					HttpStatus.OK );
		}
		else{
			return new ResponseEntity<>("Usuario no encontrado",
					HttpStatus.NOT_FOUND );
		}
	}
	@GetMapping("/Usersba")
	public List<String> UsersBanned(@RequestParam("init") int init, @RequestParam("size")int size){
		return userService.UsersBanned(init,size);
	}


	//aca el mio
	/*@ResponseStatus(HttpStatus.OK)
	public ResponseEntity login(@RequestParam("user") String user_name, @RequestParam("password") String password) {
		Optional<UserModel> us=userService.getUser(user_name);
		//return us.get();
		System.out.println("user"+user_name+"password"+password);
		if(us.isPresent()){
			String token = getJWTToken(user_name);
			if(us.get().getPASSWORD().equals(password)){
				//us.get().setUSER_NAME(user_name);
				//us.get().setTOKEN(token);
				return new ResponseEntity<>(
				token + "your user is"+us.get().getUSER_NAME()+" role"+us.get().getROL_NAME(), 
				HttpStatus.OK);
				//return us.get();
			}else{
				//throw new MyException("wrong password");
				throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "invalid user or password");
			}
		}
		else{
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "User Not Found");
		}
		
	}
	/*@PostMapping("user")
	@ResponseStatus(HttpStatus.OK)
	public UserModel login(@RequestParam("user") String user_name, @RequestParam("password") String password) {
		System.out.println(user_name);
		
		Optional<UserModel> us=userService.getUser(user_name);
		if(us.isPresent()){
			String token = getJWTToken(user_name);
			if(us.get().getPASSWORD()==password){
				us.get().setUSER_NAME(user_name);
				us.get().setTOKEN(token);
				return us.get();
			}else{
				//throw new MyException("wrong password");
				throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "invalid user or password");
			}
		}
		else{
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "User Not Found");
		}
		
	}*/

	@GetMapping("/userinfo")
	public ResponseEntity getuser(@RequestParam("token")String token){  
		
		String secretKey = "mySecretKey"; 
		try{
		Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody(); 
		return new ResponseEntity<>(claims,
					HttpStatus.OK );
		}
		catch(SignatureException e){ 
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "User Not Found");
		}
		
	}
	
	private String getJWTToken(String user_name, String user_role) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER"); 
		
		String token = Jwts
				.builder()
				.setId(user_name)
				.setSubject(user_role) 
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
}

