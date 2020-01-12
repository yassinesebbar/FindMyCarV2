package com.example.findmycarv2;

import com.google.android.gms.maps.model.LatLng;

public class CarLocation {

    private String lat;
    private String lon;
    private String street;
    private String imagePath;
    private String dateTime;

    private CarLocation(){}

    public static class Builder{

        private String lat;
        private String lon;
        private String street;
        private String imagePath;
        private String dateTime;

        public Builder setLocationInfo(String lat, String lon){

            this.lat = lat;
            this.lon = lon;

            return this;
        }

        public Builder setAddress(String address){
            this.street = address;
            return this;
        }

        public Builder setImagePath(String imagepath){
            this.imagePath = imagepath;
            return this;
        }

        public Builder setTimeLocation(String dateTime){
            this.dateTime = dateTime;
            return this;
        }

        public CarLocation build(){
            CarLocation carLocation = new CarLocation();

            carLocation.lat = lat;
            carLocation.lon = lon;
            carLocation.street = street;
            carLocation.imagePath = imagePath;
            carLocation.dateTime = dateTime;

            return carLocation;
        }

    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getStreet() {
        return street;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDateTime() {
        return dateTime;
    }
}

