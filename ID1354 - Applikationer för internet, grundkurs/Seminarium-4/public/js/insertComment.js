function insertComment(commentData)
{
    $(".comments").append('\
        <div class="comment" data-comment-id="' + commentData["id"] + '">\
            <div class="row">\
                <div class="col col-lg-8 offset-lg-2">\
                    <h4>' + commentData["name"] + '</h4>\
                </div>\
            </div>\
            <div class="row">\
                <div class="col col-lg-7 col-md-10 col-sm-9 col-8 offset-lg-2">\
                    <p>' + commentData["comment"] + '</p>\
                </div>\
            </div>\
        </div>\
    ');

    if (logged_in && user_id == commentData["user_id"])
    {
        $(".comment .row").last().append('\
        <div class="col col-lg-1 col-md-2 col-sm-3 col-3 delete-comment">\
                <form method="post">\
                    ' + csrf_field + '\
                    <input type="hidden" name="comment_id" value="' + commentData["id"] + '">\
                    <button type="submit" class="btn btn-outline-danger pull-right">DELETE</button>\
                </form>\
            </div>\
        ');
    }
}
