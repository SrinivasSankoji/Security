package com.chary.bhaumik.jwt.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.chary.bhaumik.jwt.model.AuthenticationRequest;
import com.chary.bhaumik.jwt.model.AuthenticationResponse;
import com.chary.bhaumik.jwt.model.SIOPSResponse;
import com.chary.bhaumik.jwt.util.JWTUtil;

@RestController
public class JWTController 
{
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	@Lazy
	UserDetailsService userDetailsService;
	
	@Autowired
	JWTUtil jwtUtil;
	
	@GetMapping("/hello")
	/**@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header",defaultValue = "Bearer ") })
	@ApiOperation(value = "", authorizations = { @Authorization(value="JWT") })**/
	public ResponseEntity<SIOPSResponse> hello(HttpServletRequest httpServletRequest)
	{
		String jwtToken = "";
		final String authorizationHeader = httpServletRequest.getHeader("Authorization");
		jwtToken = authorizationHeader.substring(7);
		if (jwtUtil.validateToken(jwtToken)) {
			SIOPSResponse siopsResponse = new SIOPSResponse();
			siopsResponse.setMessage("SUCCESS");
			siopsResponse.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
			siopsResponse.setStatus(200);
			siopsResponse.setPath(
					(String) httpServletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
			siopsResponse.setResponsePayload("Success");
			return new ResponseEntity<SIOPSResponse>(siopsResponse, HttpStatus.OK);
		}
		return null;
	}
	
	@PostMapping("/authenticate")
	//@ApiOperation(value = "", authorizations = { @Authorization(value="JWT") })
	public ResponseEntity<SIOPSResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest,
			HttpServletRequest httpServletRequest) throws Exception
	{
		try {
		UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String jwtToken=jwtUtil.generateToken(userDetails);
		
		Map<String, UserDetails> tokenMap=new HashMap<>();
		tokenMap.put(jwtToken, userDetails);
		Map<Long,Map<String, UserDetails>> refreshMap=new HashMap<>();
		refreshMap.put(System.currentTimeMillis(), tokenMap);
		SIOPSResponse siopsResponse=new SIOPSResponse();
		siopsResponse.setMessage("SUCCESS");
		siopsResponse.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
		siopsResponse.setStatus(200);
		siopsResponse.setPath((String) httpServletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
		siopsResponse.setResponsePayload(new AuthenticationResponse(jwtToken));
		return new ResponseEntity<SIOPSResponse>(siopsResponse,HttpStatus.OK);
		}
		catch (BadCredentialsException e) 
		{
			throw new BadCredentialsException("Incorrect User Name or Password");
		}
		
	}
}
