package dev.be.serviceuser.controller.response;

import dev.be.serviceuser.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private Integer id;
    private String profileImgUrl;
    private String description;

    public static ProfileResponse fromProfile(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getProfileImgUrl(),
                profile.getDescription()
        );
    }
}
