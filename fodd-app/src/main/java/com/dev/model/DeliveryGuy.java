package com.dev.model;


import com.dev.enumm.DeliveryStatus;

public class DeliveryGuy extends BaseUser {

    private String phone;
    private DeliveryStatus status;

    private DeliveryGuy(Builder builder) {
        super(builder.id, builder.name, builder.username, builder.password);
        this.phone = builder.phone;
        this.status = DeliveryStatus.AVAILABLE;
    }

    public String getPhone() {
        return phone;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DeliveryGuy{" + "id=" + id + ", name='" + name + "', username='" + username +
                "', status=" + status + '}';
    }

    public static class Builder
    {
        private int id;
        private String name;
        private String username;
        private String password;
        private String phone;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public DeliveryGuy build() {
            return new DeliveryGuy(this);
        }
    }
}


