// AngularJS
var FeedReader = angular.module('FeedReader', ['ngSanitize']);

FeedReader.controller('FeedController', function($scope, $http, $window) {
	$scope.error = null;
	$scope.feeds = [];
	$scope.feedSelected = null;
	$scope.feedItemsLoading = false,
	$scope.feedItems = [];
	$scope.feedItemPages = [];
	$scope.feedLoading = false;
	
	$scope.addForm = function()  {
		$scope.feeds.push({
			"id" : -1,
			"name" : "",
			"url" : "",
			"description" : "",
			"selected": false,
			"unread": 0,
			"sync": false
		})
	}
	
	$scope.saveFeed = function(feed, index) {
		feed.sync = true;
		if (feed.id == -1) {
			$http.put($window.webservicesUrl["feed"], feed).success(function(data) {
				$scope.feeds[index] = data
				$scope.feeds[index].sync = false;
				$scope.error = null;
			}).error(function() {
				$scope.feeds[index].sync = false;
				$scope.error = "Erreur dans l'ajout d'un flux \"" + feed.name + "\"";
			});
		} else {
			$http.post($window.webservicesUrl["feed"] + "?id=" + feed.id, feed).success(function(data) {
				$scope.feeds[index] = data
				$scope.feeds[index].sync = false;
				$scope.error = null;
			}).error(function() {
				$scope.feeds[index].sync = false;
				$scope.error = "Erreur dans la mise à jour d'un flux : \"" + feed.name + "\"";
			});
		}
	}
	
	$scope.removeFeed = function(feed, index) {
		feed.sync = true;
		$http.delete($window.webservicesUrl["feed"] + "?id=" + feed.id).success(function(data) {
			$scope.feeds.splice(index, 1);
			feed.sync = false;
			$scope.error = null;
		}).error(function() {
			feed.sync = false;
			$scope.error = "Erreur dans la suppression du flux \"" + feed.name + "\"";
		});
	}
	
	$scope.setReaded = function(url, $index) {
		$http.post(url).success(function(data) {
			$scope.feedItems[$index].readed = data.readed;
			$scope.feedItems[$index].url.reverseReaded = data.reverseReaded;
		});
		return;
	}
	
	$scope.loadFeedItem = function(url, feed, page) {
		$scope.feedItemsLoading = true;
		if (feed == undefined) {
			feed = $scope.currentFeed;
		} else {
			$scope.currentFeed = feed;
		}
		
		for(var i = 0; i < $scope.feeds.length; i++) {
			$scope.feeds[i].selected = ($scope.feeds[i].id == feed.id);
		}
		
		if (page == undefined) {
			page = 1;
		}
		url = url.replace('{{feed.id}}', feed.id).replace('{{page}}', page);
		$http.get(url).success(function(feedItemInfo) {
			$scope.feedItems = feedItemInfo.feedItems;
			$scope.feedItemPages = [];
			for(var i = 1; i <= feedItemInfo.pages; i++) {
				$scope.feedItemPages.push({"id": i,"selected": i == page})
			}
			$scope.feedItemsLoading = false;
		})
	}
	
	$http.get($window.webservicesUrl["feed"]).success(function(feeds) { 
		$scope.feeds = feeds;
		for(var i = 0; i < $scope.feeds.length; i++) {
			$scope.feeds[i].selected = false;
		}
		$scope.feedLoading = true;
	});
	
//	alert($window.webkitNotifications.checkPermission());
//	$window.webkitNotifications.createNotification('icon.png', 'Notification', 'Activation des notifications');
//	// Notification
//	if ($window.webkitNotifications) {
//		if ($window.webkitNotifications.checkPermission() == 0) {
//			$window.webkitNotifications.createNotification('icon.png', 'Notification', 'Activation des notifications');
//		} else { 
//			$window.webkitNotifications.requestPermission();
//		}
//	}

	
	// Create our websocket object with the address to the websocket
	var ws = new WebSocket("ws://" + $window.location.hostname +":" + $window.location.port + "/feedreader/live/feed");
	ws.onopen = function(){  
		console.log("Socket has been opened!");
	};
	
	ws.onmessage = function(message) {
		var data = angular.fromJson(message.data);
		$scope.$apply(function() {
			if (data.type == "feeds") {
				$scope.feeds = data.feeds;
				
				// Reséléction du flux afficher
				for(var i = 0; i < $scope.feeds.length; i++) {
					$scope.feeds[i].selected = ($scope.feeds[i].id == $scope.currentFeed.id);
				}
				
				for(var i = 0; i < data.feedItems.length; i++) {
					for(var j = 0; j < $scope.feedItems.length; j++) {
						// Si les éléments sont trouvé
						if ($scope.feedItems[j].id == data.feedItems[i].id) {
							$scope.feedItems[j].enclosure = data.feedItems[i].enclosure;
							$scope.feedItems[j].feedItemId = data.feedItems[i].feedItemId;
							$scope.feedItems[j].link = data.feedItems[i].link;
							$scope.feedItems[j].readed = data.feedItems[i].readed;
							$scope.feedItems[j].summary = data.feedItems[i].summary;
							$scope.feedItems[j].title = data.feedItems[i].title;
							$scope.feedItems[j].url = data.feedItems[i].url;
							break;
						}
					}
				}
			} else if (data.type == "feedItems") {
				alert("Mise à jours items");
				// Notification
//				if ($window.webkitNotifications) {
//					if (window.webkitNotifications.checkPermission() == 0) {
//						window.webkitNotifications.createNotification('', 'Notification', 'Activation des notifications');
//					} else {
//					    window.webkitNotifications.requestPermission();
//					}
//				}
			}
		})
	};
	
})

// Bootstrap
$(document).ready(function() {
	$(document).on('click', '#feed-one .nav-tabs a', function (e) {
		var self = $(this);
		if (self.hasClass('tab-open')) {
			return true;
		} else {
			e.preventDefault();
			var href = self.attr("href");
			var parent = self.parents('.panel-collapse')
			parent.find('.resume, .link').hide();
			parent.find(".active").removeClass('active')
			$(href).show();
			console.log(href);
			self.parent().addClass('active');
		}
	});
	
	$(document).on('click', '#feed-one .nav-tabs a.tab-link', function (e) {
		var link = $(this).parents('.panel-collapse').find('.link');
		var href = link.data('href');
		if (link.find('iframe').length == 0) {
			link.append($('<iframe src="' + href + '"></iframe>'))
		}
	});
	
//	$(document).on('click', '#feed-one .nav-tabs a.tab-resume', function (e) {
//		if($('iframe').length > 1) {
//			$(this).parents('.panel-collapse').find('.link').html('');
//		}
//	});
//
//	$(document).on('show.bs.collapse', '.panel-heading', function() {
//		alert("test");
//	});
//	
	$(document).on('show.bs.collapse', '#feed-one .panel', function() {
		var self = $(this);
		if (self.find('.glyphicon-star-empty').length > 0) {
			var urlReaded = self.data('readed');
			$.ajax({
				"url" : urlReaded,
				"type": "POST",
				"dataType": "json",
			})
		}
	});
})
