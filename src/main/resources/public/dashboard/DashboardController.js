(function () {
    'use strict';

    var app = angular.module('client_app');
/*
    app.config(['flowFactoryProvider', function (flowFactoryProvider) {
            flowFactoryProvider.defaults = {
                target: '/api/dashboard/upload',
                permanentErrors: [500, 501],
                maxChunkRetries: 1,
                chunkRetryInterval: 5000,
                simultaneousUploads: 1
            };
    }]);
    */

    app.controller('DashboardController', function($scope, $http, $mdDialog){
        $scope.showAdvanced = function(ev) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '/dashboard/upload_dataset_file_dialog.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            }).then(function(answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function() {
                    $scope.status = 'You cancelled the dialog.';
                });
        };
    });

    function DialogController($scope, $mdDialog) {
        $scope.hide = function() {
            $mdDialog.hide();
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.answer = function(answer) {
            $mdDialog.hide(answer);
        };
    }
})();
