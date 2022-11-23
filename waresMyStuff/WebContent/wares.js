/**
 * 
 */

// When a tab is selected (unique is defined), change its background to white
// and turn on its associated form.
// If unique is not defined, make the div with "highlight" class tab white and the
// first form visible. If there is no "highlight" class, highlight the first tab
function unstack(unique) {
	// new version
	var highlightThisDiv = null;
	if (typeof (unique) === 'undefined') {
		// find a "highlight" div
		highlightThisDiv = document.querySelector(".highlight");
		if (highlightThisDiv == null || highlightThisDiv.tagName != 'DIV') {
			// otherwise, use the first ".tab"
			highlightThisDiv = document.querySelector(".tab");
			// if we don't find a valid tab div
			if (highlightThisDiv == null || highlightThisDiv.tagName != 'DIV') {
				highlightThisDiv = null
			}
		}
	}
	var divs = document.getElementsByTagName("div");
	for (var i = 0; i < divs.length; i++) {
		// tabs
		if (divs[i].id.substring(0, 6) == "tabbed") {
			if (divs[i] == highlightThisDiv) {
				// highlight the first tab and make the first form visible
				// by making unique the suffix of the first div
				unique = divs[i].id.substring(6);
			}
			if (divs[i].id == "tabbed" + unique) {
				divs[i].style.backgroundColor = "white";
			} else {
				if (divs[i].classList.contains("link"))
					divs[i].style.backgroundColor = "lightblue";
				else
					divs[i].style.backgroundColor = "linen";
			}
		}
	}
	for (var i = 0; i < divs.length; i++) {
		// overlays
		if (divs[i].id.substring(0, 7) == "overlay") {
			if (divs[i].id == "overlay" + unique) {
				divs[i].style.display = "block";
			} else {
				divs[i].style.display = "none";
			}
		}
	}
}