$(document).ready(function() {
    // Måste använd body pga att vi appendar delete knappen efter page load.
    $('body').on('submit', '.delete-comment form', function(e) {
        e.preventDefault();

        var thisHTML = $(this);

        // $.post tillät mig inte hantera errors korrekt. Därav $.ajax.
        // Kan såklart ha missat någonting, men fick detta att funka och det är fortfarande ajax så!
        $.ajax({
            url: "/comment/destroy/" + recipeTitle,
            type: 'POST',
            data: $(this).serialize(),
            success: function(data) {
                // Vi lyckades, yey
                $(thisHTML).closest(".comment").remove();
            },
            error: function(jqXhr, json, errorThrown)
            {
                var error = "";
                if (typeof jqXhr.responseJSON.user_id !== 'undefined') {
                    error = jqXhr.responseJSON.user_id;
                }
                else if (typeof jqXhr.responseJSON.comment_id[0] !== 'undefined') {
                    error = jqXhr.responseJSON.comment_id[0];
                }
                console.log(error);
                // kommer endast ha ett fel, därav lite hårdkodat.
                // var error = jqXhr.responseJSON.comment[0];
                $('#comment-errors').append('<h5 class="text-danger">' + error + '</h5>');
            }
        });
    });
});
