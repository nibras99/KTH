$(document).ready(function() {
    $.getJSON("/comments", "recipe=" + recipeTitle,
        function(returnedData) {
            $(returnedData).each(function(index) {
                insertComment(returnedData[index]);
            });
    });
});
