//提交回复
function postCommentToQuestion() {
    let questionId = $("#questionId").val();
    let content = $("#questionComment").val();
    post(questionId,1,content);
}
function postCommentToComment(e) {
    let commentId = e.getAttribute("data-id");
    let content = $("#input-"+commentId).val();
    post(commentId,2,content)
}
function post(targetId,type,content) {
    $.ajax({
        type:"post",
        url:"/comment",
        contentType:"application/json",
        dataType:"json",
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success:function (result) {
            if(result.code==200){
                window.location.reload();
            }else{
                if(result.code==2003){
                    let conf = confirm(result.message);
                    if(conf){
                        window.open("https://gitee.com/oauth/authorize?client_id=9de7b0e384f6b58f6a44c5b4152d05587fe19a5f3df5937b9115bcd4e75e20de&redirect_uri=http://localhost:8080/login&response_type=code");
                        window.localStorage.setItem("isClose",true);
                    }
                }else{
                    alert(result.message)
                }
            }
        }
    })
}

//展开二级评论
function collapseComments(e) {
    let id = e.getAttribute("data-id");
    let subCommentContainer = $("#comment-"+id);
    subCommentContainer.toggleClass("in");


    if(subCommentContainer.hasClass("in")&&subCommentContainer.children().length==2) {
        $.getJSON("/comment/"+id,function (data) {
            $.each(data.data,function (index,comment) {

                let mediaLeftElement = $("<div/>",{
                    "class":"media-left",
                }).append($("<img/>",{
                    "class":"media-object img-rounded",
                    "src":comment.user.avatarUrl
                }));
                let mediaBodyElement = $("<div/>",{
                    "class":"media-body",
                }).append($("<h5/>",{
                    "class":"media-heading",
                    html:comment.user.name
                })).append($("<div/>",{
                    html:comment.content
                })).append($("<div/>",{
                    "class":"comment-tools",
                })).append($("<span/>",{
                    "class":"pull-right",
                    html:moment(comment.gmtCreate).format("YYYY-MM-DD HH:MM")
                }));

                let mediaElement = $("<div/>",{
                    "class":"media",
                }).append(mediaLeftElement)
                    .append(mediaBodyElement)
                    .append($("<hr/>",{
                        "class":"subCommentHr"
                    }));
                let commentElement = $("<div/>",{
                    "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12",
                }).append(mediaElement);
                subCommentContainer.append(commentElement);

            });
        })
    }
}

function selectTag(e) {
    let value = e.getAttribute("data-tag");
    let pre = $("#tag").val();
    if(pre.indexOf(value) == -1){
        if(pre){
            $("#tag").val(pre+","+value);
        }else{
            $("#tag").val(value);
        }
    }
}