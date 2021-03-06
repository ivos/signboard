/*!
 * Custom JavaScript
 */
/*
 * scroll aid
 */
function fixLinksWithFixedNavbar() {
	if ($(document).width() > 979) {
		var hash = window.location.hash;
		if (hash != "") {
			$(document).scrollTop(
					($(hash).offset().top) - $(".navbar-fixed-top").height());
		}
		var locationHref = window.location.protocol + '//'
				+ window.location.host + $(window.location).attr('pathname');
		var anchorsList = $('a').get();
		for (i = 0; i < anchorsList.length; i++) {
			var hash = anchorsList[i].href.replace(locationHref, '');
			if (hash[0] == "#" && hash != "#") {
				var originalOnClick = anchorsList[i].onclick;
				setNewOnClick(originalOnClick, hash);
			}
		}
	}

	function setNewOnClick(originalOnClick, hash) {
		anchorsList[i].onclick = function() {
			$(originalOnClick);
			$(document).scrollTop(
					($(hash).offset().top) - $(".navbar-fixed-top").height());
			return false;
		};
	}
}

!function($) {
	$(function() {
		var $window = $(window)
		// scrollaid
		$('.scrollaid').affix({
			offset : {
				top : function() {
					return $window.width() <= 980 ? 220 : 170
				}
			}
		})
		// fix links with fixed navbar
		fixLinksWithFixedNavbar();
	})
}(window.jQuery)

/*
 * typeahead
 */
jQuery(".typeahead").typeahead({
	source : function(query, process) {
		var dtset = jQuery(jQuery(this).get(0)['$element'].get(0)).data();
		return Seam.createBean(dtset.bean)[dtset.method](query, process);
	},
	items : 6
});

/*
 * datepicker
 */
jQuery(".datepicker").datepicker({
	autoclose : true,
	todayBtn : true,
	todayHighlight : true
});

/*
 * combobox
 */
jQuery(document).ready(function() {
	jQuery('.combobox').combobox();
});
