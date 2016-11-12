(function () {
    'use strict';
    var app = angular.module('client_app');

    app.controller('DashboardController', function ($scope, $http, $mdDialog) {
        $scope.showDatasetLoaderDialog = function (ev) {
            $mdDialog.show({
                controller: DatasetLoaderDialog,
                templateUrl: '/dashboard/upload.dataset.dialog.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            }).then(function (answer) {
                $scope.status = 'You said the information was "' + answer + '".';
            }, function () {
                $scope.status = 'You cancelled the dialog.';
            });
        };
    });

    app.directive('uploadFileComponent', uploadFileComponent);

    //<File,URL loader dialog>
    function DatasetLoaderDialog($scope, $mdDialog,$location, $http, Upload,AuthenticationService) {
        $scope.fileTypes = [
            {label: 'xlsx', value: 'xlsx'},
            {label: 'csv', value: 'csv', isDisabled: true},
            {label: 'html', value: 'html', isDisabled: true}
        ];
        $scope.fileType = 'xlsx';

        $scope.hide = function () {
            $mdDialog.hide();
        };

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.answer = function (answer) {
            $mdDialog.hide(answer);
        };

        $scope.uploadDataset = function () {
            if($scope.switcher !== undefined && $scope.switcher.url){
                $http.post('/api/protected/dashboard/urlupload',{ dataset_url : $scope.datasetUrl, file_type : $scope.fileType})
                    .success(function (response) {
                        $mdDialog.cancel();
                    })
                    .error(function (response, status) {
                        // showAlert(response)
                    });
            }else{
                var fileToLoad = $scope.files[0];
                Upload.upload({
                    url: 'api/protected/dashboard/fileupload',
                    file: fileToLoad,
                    // data: { file: fileToLoad,'type':$scope.fileType},
                    progress: function (e) {
                    }
                }).then(function (resp) {
                    $mdDialog.cancel();
                },function (resp) {
                    $mdDialog.cancel();
                    if(resp.status == 401){
                        AuthenticationService.logout();
                        $location.path('/');
                    }else{
                        console.log('Error status: ' + resp.status);
                    }
                }, function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    console.log('progress: ' + progressPercentage+'%');
                });
            }


        };
    }

    function uploadFileComponent() {
        var directive = {
            restrict: 'E',
            templateUrl: '/dashboard/upload.file.template.html',
            link: uploadFileComponentLink
        };
        return directive;
    }

    function uploadFileComponentLink($scope, element, attrs) {
        var input = $(element[0].querySelector('#fileInput'));
        var button = $(element[0].querySelector('#uploadButton'));
        var textInput = $(element[0].querySelector('#textInput'));

        if (input.length && button.length && textInput.length) {
            button.click(function (e) {
                input.click();
            });
            textInput.click(function (e) {
                input.click();
            });
        }

        input.on('change', function (e) {
            var files = e.target.files;
            $scope.files = files;
            if (files[0]) {
                $scope.fileName = files[0].name;
            } else {
                $scope.fileName = null;
            }
            $scope.$apply();
        });
    }

    //</File,URL loader dialog>

})();
