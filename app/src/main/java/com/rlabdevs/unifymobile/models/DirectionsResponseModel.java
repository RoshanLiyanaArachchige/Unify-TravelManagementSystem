package com.rlabdevs.unifymobile.models;

import java.util.List;

public class DirectionsResponseModel {
    private String status;
    private List<Route> routes;

    public String getStatus() {
        return status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        private OverviewPolyline overviewPolyline;
        private List<Leg> legs;

        public OverviewPolyline getOverviewPolyline() {
            return overviewPolyline;
        }

        public List<Leg> getLegs() {
            return legs;
        }
    }

    public static class OverviewPolyline {
        private String points;

        public String getPoints() {
            return points;
        }
    }

    public static class Leg {
        private Distance distance;
        private Duration duration;
        private String startAddress;
        private String endAddress;

        public Distance getDistance() {
            return distance;
        }

        public Duration getDuration() {
            return duration;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public String getEndAddress() {
            return endAddress;
        }
    }

    public static class Distance {
        private String text;
        private int value;

        public String getText() {
            return text;
        }

        public int getValue() {
            return value;
        }
    }

    public static class Duration {
        private String text;
        private int value;

        public String getText() {
            return text;
        }

        public int getValue() {
            return value;
        }
    }
}
