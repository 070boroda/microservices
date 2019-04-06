package com.zelenko.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="jwt.config")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtConfiguration {

    private String loginUrl = "/login/**";
    @NestedConfigurationProperty
    private Header header = new Header();
    private int duration = 3600;
    private String secret = "tokensecret";
    private String type = "encrypted";

    public static class Header{
        private String name = "Autorization";
        private String prefix = "Bearer";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }
}
