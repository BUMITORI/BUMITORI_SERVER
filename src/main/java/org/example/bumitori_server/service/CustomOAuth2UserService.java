package org.example.bumitori_server.service;

import org.example.bumitori_server.dto.CustomOAuth2User;
import org.example.bumitori_server.dto.GoogleResponse;
import org.example.bumitori_server.dto.OAuth2Response;
import org.example.bumitori_server.dto.UserProfileDto;
import org.example.bumitori_server.entity.Role;
import org.example.bumitori_server.entity.UserAccount;
import org.example.bumitori_server.repository.UserAccountRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final UserAccountRepository userAccountRepository;

  public CustomOAuth2UserService(UserRepository userRepository, UserAccountRepository userAccountRepository) {
    this.userRepository = userRepository;
    this.userAccountRepository = userAccountRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);
    System.out.println(oAuth2User);

    String provider = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Response oAuth2Response = null;
    oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

    String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    UserAccount existData = userAccountRepository.findByUsername(username);

    if (existData == null) {

      UserAccount userAccount = new UserAccount();
      userAccount.setUsername(username);
      userAccount.setEmail(oAuth2Response.getEmail());

      userAccountRepository.save(userAccount);

      UserProfileDto userProfileDto = new UserProfileDto();
      userProfileDto.setUsername(username);
      userProfileDto.setName(oAuth2Response.getName());
      userProfileDto.setRole(Role.STUDENT);
      userProfileDto.setEmail(oAuth2Response.getEmail());


      return new CustomOAuth2User(userProfileDto);
    } else {

      existData.setEmail(oAuth2Response.getEmail());

      userAccountRepository.save(existData);

      UserProfileDto userProfileDto = new UserProfileDto();
      userProfileDto.setUsername(username);
      userProfileDto.setName(oAuth2Response.getName());
      userProfileDto.setRole(Role.STUDENT);
      userProfileDto.setEmail(oAuth2Response.getEmail());

      return new CustomOAuth2User(userProfileDto);
    }
  }
}