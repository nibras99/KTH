$(document).ready(function() {
    function fetchFromServer() {
        $.ajax({
            url: "/comment/poll/" + recipeTitle,
            type: 'GET',
            success: function(data) {
                // Vi lyckades, yey
                var data = JSON.parse(data);

                if (data[0] == "store")
                {
                    // Så att insändaren inte får dubbel kommentar
                    if (data[1].user_id != user_id)
                    {
                        insertComment(data[1]);
                    }
                }
                else if (data[0] == "destroy")
                {
                    $('div[data-comment-id="' + data[1] + '"]').remove();
                }

                fetchFromServer();
            },
            error: function(jqXhr, json, errorThrown)
            {
                // Vet inte riktigt vad den skulle kunna returna för fel... aja
                fetchFromServer();
            }
        });
    }
    fetchFromServer();
});
