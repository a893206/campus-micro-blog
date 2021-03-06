(function (window, undefined) {
    var PopupLogin = Base.getClass('main.component.PopupLogin');
    var PopupUpload = Base.getClass('main.component.PopupUpload');
    var ActionUtil = Base.getClass('main.util.Action');

    Base.ready({
        initialize: fInitialize,
        binds: {
            //.表示class #表示id
            'click .js-login': fClickLogin,
            'click .js-share': fClickShare
        },
        events: {
            'click div.content': fClickContent,
            'click button.click-like': fClickLike,
            'click button.click-dislike': fClickDisLike
        }
    });

    function fInitialize() {
        if (window.loginpop > 0) {
            fClickLogin();
        }
    }

    function fClickShare() {
        var that = this;
        PopupUpload.show({
            listeners: {
                done: function () {
                    //alert('login');
                    window.location.reload();
                }
            }
        });
    }

    function fClickLogin() {
        var that = this;
        PopupLogin.show({
            listeners: {
                login: function () {
                    //alert('login');
                    window.location.reload();
                },
                register: function () {
                    //alert('reg');
                    window.location.reload();
                }
            }
        });
    }

    function fClickContent(oEvent) {
        var oEl = $(oEvent.currentTarget);
        window.location = $.trim(oEl.attr('data-url'));
    }

    function fClickLike(oEvent) {
        var that = this;
        var oEl = $(oEvent.currentTarget);
        var sId = $.trim(oEl.attr('data-id'));
        // 不存在Id || 正在提交 ，则忽略
        if (!sId || that.actioning) {
            return;
        }
        that.actioning = true;
        ActionUtil.like({
            microBlogId: sId,
            call: function (oResult) {
                oEl.find('span.count').html(oResult.msg);
                if (oEl.hasClass('pressed')) {
                    oEl.removeClass('pressed');
                } else {
                    oEl.addClass('pressed');
                }
                oEl.parent().find('.click-dislike').removeClass('pressed');
            },
            error: function (res) {
                if (res.code === 1 && res.msg === '用户未登录') {
                    fClickLogin();
                    return;
                }
                alert('出现错误，请重试');
            },
            always: function () {
                that.actioning = false;
            }
        });
    }

    function fClickDisLike(oEvent) {
        var that = this;
        var oEl = $(oEvent.currentTarget);
        var sId = $.trim(oEl.attr('data-id'));
        // 不存在Id || 正在提交 ，则忽略
        if (!sId || that.actioning) {
            return;
        }
        that.actioning = true;
        ActionUtil.dislike({
            microBlogId: sId,
            call: function (oResult) {
                if (oEl.hasClass('pressed')) {
                    oEl.removeClass('pressed');
                } else {
                    oEl.addClass('pressed');
                }
                var oLikeBtn = oEl.parent().find('.click-like');
                oLikeBtn.removeClass('pressed');
                oLikeBtn.find('span.count').html(oResult.msg);
            },
            error: function (res) {
                if (res.code === 1 && res.msg === '用户未登录') {
                    fClickLogin();
                    return;
                }
                alert('出现错误，请重试');
            },
            always: function () {
                that.actioning = false;
            }
        });
    }

})(window);