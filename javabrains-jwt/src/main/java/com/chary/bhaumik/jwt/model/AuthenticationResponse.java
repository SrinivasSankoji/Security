package com.chary.bhaumik.jwt.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String jwt;

}
