/**
 * Created by oscar on 27/02/16.
 */
var app = angular.module("app", []);
const url = "http://192.168.1.36:4567";
//const url = "http://localhost:4567";

app.controller("Controller", ["$scope", "$http", "server", function ($scope, $http, server) {
    server.retrieve()
        .success(function(data) {
            $scope.btDevices = data;
        });

    $scope.bf = 50;
    $scope.lr = 50;

    $scope.startDiscovery = function() {
        server.startDiscovery()
            .success(function(data) {
                $scope.btDevices = data;
            });
    }

    $scope.connectDevice = function(index) {
        server.connectDevice(index)
            .success(function(data) {
                if(data == "Success") {
                    var id = "#" + index;
                    $(id + "_conn").prop("disabled", true);
                    $(id + "_text").prop("disabled", false);
                    $(id + "_send").prop("disabled", false);
                    $(id + "_disconn").prop("disabled", false);
                }
            });
    }

    $scope.disconnectDevice = function(index) {
        server.disconnectDevice(index)
            .success(function(data) {
                if(data == "Success") {
                    var id = "#" + index;
                    $(id + "_conn").prop("disabled", false);
                    $(id + "_text").prop("disabled", true);
                    $(id + "_send").prop("disabled", true);
                    $(id + "_disconn").prop("disabled", true);
                }
            });
    }

    $scope.sendCommand = function(value, value2) {
        var commandLine;
        commandLine = value + "," + value2;
        $http.post(url + "/devices/command", commandLine)
            .success(function (data) {
                if(data == "Success");
                else if (data == "Error");
            });
    }
}]);

app.service("server", ["$http", function($http) {
    //const url = "http://localhost:4567";
    const url = "http://192.168.1.36:4567";
    var self = this;

    self.retrieve = function() {
        return $http.get(url + "/devices");
    }

    self.startDiscovery = function() {
        return $http.get(url + "/discover");
    }

    self.connectDevice = function(index) {
        return $http.get(url + "/devices/" + index + "/connect");
    }

    self.disconnectDevice = function(index) {
        return $http.get(url + "/devices/" + index + "/disconnect");
    }

    self.sendMessage = function(index, text) {
        return $http.post(url + "/devices/" + index + "/sendMessage", text);
    }

    self.sendByte = function(dato) {
        return $http.post(url + "/devices/dato", dato);
    }
}]);