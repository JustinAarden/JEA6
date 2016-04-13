; //All Javascipt logic
(function () {
    var wsUrl = "ws://localhost:8080/socket/api";
    function createWebSocket(host) {
        if (!window.WebSocket) {
            alert("window.WebSocket not supported");
        } else {
            var ws = new WebSocket(host);
            ws.onopen = function () {
                console.log("opening ws connection");
            };
            ws.onmessage = function (msg) {
                console.log('message: ');
                console.log(msg.data);
            };
            return ws;
        }
    }

    function getUserById(users, id) {
        for (i in users) {
            if (users[i].id == id) {
                return users[i];
            }
        }
        return null;
    }

    function getUserByName(users, name) {
        for (i in users) {
            if (users[i].name == name) {
                return users[i];
            }
        }
        return null;
    }

    var params = {};
    if (location.search != undefined) {
        var parts = location.search.substring(1).split('&');
        for (var i = 0; i < parts.length; i++) {
            var nv = parts[i].split('=');
            if (!nv[0])
                continue;
            params[nv[0]] = nv[1] || true;
        }
    } else {
        alert("location.search isn't supported in your webbrowser, this will result in errors");
    }

    var app = angular.module('Kwetter', ['ngResource']);
    app.factory("userFactory", ['$resource', '$http', function ($resource, $http) {
        $http.defaults.withCredentials = true;
        return $resource("http://localhost:8080/resources/rest/users/:id", {});
    }]);

    app.controller("Kwetter_profile", ['$scope', 'userFactory', function ($scope, userFactory) {
        //override .onmessage()
        socket = createWebSocket(wsUrl);

        socket.onmessage = function (msg) {
            if (msg.data == "new tweet") {
                alert("New Tweet");
                loadData();
            } else {
                console.log(msg);
            }
        }

        function loadData() {
            userFactory.query(function (data) {
                console.log("Loaded users");
                console.log(data);
                $scope.users = data;
                $scope.currentUser = getUserById($scope.users, params.id);
            });
            $scope.s1_switch = "tweets"; //(options are: tweets, following and followers)
            //this is needed because we've get users by id on profile.html
            $scope.getUserById = function (id) {
                return getUserById($scope.users, id);
            };
        }
        ;
        loadData();
    }]);
    app.controller("Kwetter_home", ['$scope', 'userFactory', function ($scope, userFactory) {
        userFactory.query(function (data) {
            console.log("Loaded users");
            console.log(data);
            $scope.users = data;
        });
        $scope.submitLogin = function () {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/resources/rest/api/login",
                data: {
                    username: $("input[id='name']").val(),
                    password: $("input[id='password']").val()
                }, xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                location.href = data;
            }).error(function (data) {
                console.log("error");
                alert(data.responseText);
            });
        };


        $scope.getUsernames = function () {
            var usernames = [];
            for (i in $scope.users) {
                if ($scope.users[i].name)
                    usernames.push($scope.users[i].name);
            }
            return usernames;
        };

        $scope.getName = function (id) {
            return  $scope.users[id].name;
        };


        $scope.addTweet = function (tweet) {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/resources/rest/addtweet/" + params.id+"/"+ $scope.tweetText
            })};

    }]);
    app.controller("Kwetter_loggedin", ['$scope', 'userFactory', function ($scope, userFactory) {
        socket = createWebSocket(wsUrl);
        socket.onmessage = function (msg) {
            if (msg.data == "new tweet") {
                reloadTweets();
            } else {
                console.log(msg);
            }
        };
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/resources/rest/api/isLoggedIn/" + params.id,
            xhrFields: {
                withCredentials: true
            }
        }).success(function (data) {
            console.log("logged in success");
            console.log(data);
        }).error(function (data) {
            console.log("logged in error");
            if (data.responseJSON) {
                alert(data.responseJSON.message);
                if (data.responseJSON.id) {
                    location.href = "?id=" + data.responseJSON.id;
                } else {
                    location.href = "index.html";
                }
            }
        });
        var latesttweet = "";
        $scope.getLatestTweet = function () {
            return latesttweet;
        };

        function reloadTweets() {
            userFactory.query(function (data) {
                console.log("Loaded users");
                console.log(data);
                $scope.users = data;
                $scope.currentUser = getUserById($scope.users, params.id);

                $scope.getMyTweetsAndFromFollowedAccounts = function () {
                    var timeline = [];
                    for (i in $scope.currentUser.tweets) {
                        timeline.push($scope.currentUser.tweets[i]);
                        latesttweet=$scope.currentUser.tweets[i];
                    }
                    for (i in $scope.currentUser.following) {
                        var user = getUserById($scope.users, $scope.currentUser.following[i]);
                        for (j in user.tweets) {
                            timeline.push(user.tweets[j]);

                        }
                    }

                    timeline.sort(function (a, b) {
                        return a.datum == b.datum ? 0 : a.datum < b.datum ? 1 : -1;


                    });
                    return timeline;
                };

                //this is needed because we've data-ng-src="{{getUserById(followedUserID).image}}" in line 105 on loggedin.html
                $scope.getUserById = function (id) {
                    return getUserById($scope.users, id);
                };
            });
        }
        reloadTweets();

        $scope.logout = function () {
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/resources/rest/api/logout",
                xhrFields: {
                    withCredentials: true
                }
            }).success(function () {
                location.href = "index.html";
            }).error(function (data) {
                console.log("error");
                alert(data.responseText);
            });
        };
/*        $scope.submitTweet = function () {
            var tweet = {
                tweetText: $scope.tweetText,
            };
            userFactory.save({id: params.id}, tweet).$promise.finally(function () {
                getUsers();
            });
            $scope.tweetText = "";
        };*/
        $scope.submitTweet = function () {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/resources/rest/addtweet/" + params.id+"/"+ $scope.tweetText
            })
        };
    }]);
}());