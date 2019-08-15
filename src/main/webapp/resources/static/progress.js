$(document).ready(function () {
   $(document).on("click", ".user_name", startProgress);
   $(document).on("submit", "form", startProgress);
   $(document).on("click", ".tool", startProgress);
});
function startProgress() {
    var pr = $(".progress_hidden");
    var wr = $(".progress_wr_hidden");
    pr.removeClass("progress_hidden");
    pr.addClass("progress");
    wr.removeClass("progress_wr_hidden");
    wr.addClass("progress_wr");

}