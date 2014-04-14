
$("#searchbox").data('oldVal', $("#searchbox").val());
$("#searchbox").bind("propertychange keyup input paste", function() {
	if ($("#searchbox").data('oldVal') != $("#searchbox").val()) {
		// Updated stored value
		$("#searchbox").data('oldVal', $("#searchbox").val());
		// run the search						
		doSearch();
	}
});

$('#indexlink').on('click', function(e) {
    e.preventDefault();
	doIndex();
});
