$(document).ready(function() {
    $(".comment-create form").submit(function(e) {
        e.preventDefault();

        // $.post tillät mig inte hantera errors korrekt. Därav $.ajax.
        // Kan såklart ha missat någonting, men fick detta att funka och det är fortfarande ajax så!
        $.ajax({
            url: "/comment/store/" + recipeTitle,
            type: 'POST',
            data: $(this).serialize(),
            success: function(data) {
                // Vi lyckades, yey
                insertComment(data.success);
                $(".comment-create form textarea").val("");
            },
            error: function(jqXhr, json, errorThrown)
            {
                // kommer endast ha ett fel, därav lite hårdkodat.
                var error = jqXhr.responseJSON.comment[0];
                $('#comment-errors').append('<h5 class="text-danger">' + error + '</h5>');
            }
        });
    });
});
