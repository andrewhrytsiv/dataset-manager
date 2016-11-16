(function () {
    'use strict';
    var app = angular.module('client_app');

    app.controller('DashboardController', function ($scope, $http, $mdDialog, $location, AuthenticationService) {
        $scope.datasets = [];
        $scope.$on('loadDatasetsEvent', function(){
            loadDatasets()
        });
        loadDatasets();

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

        function loadDatasets(){
            $http.get('/api/protected/dashboard/datasets')
                .success(function (response) {
                    console.log("response-->"+JSON.stringify(response));
                    $scope.datasets =  response;
                })
                .error(function (response, status) {
                    $scope.datasets = [];
                    if(status == 401){
                        AuthenticationService.logout();
                        $location.path('/login');
                    }else{
                        console.log('Error status: ' + resp.status);
                    }
                });
        };
    });

    app.directive('uploadFileComponent', uploadFileComponent);

    //<File,URL loader dialog>
    function DatasetLoaderDialog($scope,$rootScope, $mdDialog,$location, $http, Upload,AuthenticationService) {
        $scope.datasetLoading = false;

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
                $scope.datasetLoading = true;
                $http.post('/api/protected/dashboard/urlupload',{ dataset_url : $scope.datasetUrl, file_type : $scope.fileType})
                    .success(function (response) {
                        $scope.datasetLoading = false;
                        $mdDialog.cancel();
                    })
                    .error(function (response, status) {
                        $scope.datasetLoading = false;
                        $mdDialog.cancel();
                        showAlert(response);
                    });
            }else{
                var fileToLoad = $scope.files[0];
                $scope.datasetLoading = true;
                Upload.upload({
                    url: 'api/protected/dashboard/fileupload?type='+$scope.fileType,
                    file: fileToLoad,
                    // data: { file: fileToLoad,'type':$scope.fileType},
                    progress: function (e) {
                    }
                }).then(function (resp) {
                    $scope.datasetLoading = false;
                    $rootScope.$broadcast('loadDatasetsEvent', 'Please, load datasets!');
                    $mdDialog.cancel();
                },function (resp) {
                    $scope.datasetLoading = false;
                    $mdDialog.cancel();
                    if(resp.status == 401){
                        AuthenticationService.logout();
                        $location.path('/login');
                    }else{
                        $mdDialog.cancel();
                        showAlert(resp);
                    }
                }, function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    // console.log('progress: ' + progressPercentage+'%');
                });
            }
        };

        function showAlert(response) {
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#dashboard-container-id')))
                    .clickOutsideToClose(true)
                    .title('Uploading error:')
                    .textContent(response.error_message)
                    .ariaLabel('Alert Dialog Demo')
                    .ok('OK')
                    .targetEvent(response)
            );
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
