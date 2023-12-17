package yu.favourite.base.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InstagramAccessToken {
    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "user_id")
    private String userId;
}