package com.predators.security;

import com.predators.security.model.SignInRequest;
import com.predators.security.model.SignInResponse;

public interface AuthenticationService {

    SignInResponse authenticate(SignInRequest request);
}
