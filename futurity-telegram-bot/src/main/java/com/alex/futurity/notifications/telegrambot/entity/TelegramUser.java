package com.alex.futurity.notifications.telegrambot.entity;

import javax.persistence.*;

@Entity
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userId; // id from other microservice
    @Column(nullable = false, unique = false)
    private String telegramChatId;
    @Column(nullable = false)
    private String username;
    private boolean enabledNotifications;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabledNotifications() {
        return enabledNotifications;
    }

    public void setEnabledNotifications(boolean enabledNotifications) {
        this.enabledNotifications = enabledNotifications;
    }


    public static TelegramUserBuilder builder() {
        return new TelegramUserBuilder();
    }

    public static final class TelegramUserBuilder {
        private Long id;
        private String userId; // id from other microservice
        private String telegramChatId;
        private String username;
        private boolean enabledNotifications;

        private TelegramUserBuilder() {
        }

        public TelegramUserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TelegramUserBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public TelegramUserBuilder telegramChatId(String telegramChatId) {
            this.telegramChatId = telegramChatId;
            return this;
        }

        public TelegramUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public TelegramUserBuilder enabledNotifications(boolean enabledNotifications) {
            this.enabledNotifications = enabledNotifications;
            return this;
        }

        public TelegramUser build() {
            TelegramUser telegramUser = new TelegramUser();
            telegramUser.setId(id);
            telegramUser.setUserId(userId);
            telegramUser.setTelegramChatId(telegramChatId);
            telegramUser.setUsername(username);
            telegramUser.setEnabledNotifications(enabledNotifications);
            return telegramUser;
        }
    }
}
